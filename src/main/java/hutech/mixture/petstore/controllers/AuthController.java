package hutech.mixture.petstore.controllers;

import hutech.mixture.petstore.models.User;
import hutech.mixture.petstore.repositories.IUserRepository;
import hutech.mixture.petstore.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller // Đánh dấu lớp này là một Controller trong Spring MVC.
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final RememberMeServices rememberMeServices;

    @GetMapping("/login")
    public String login(Model model,
                        @RequestParam(required = false) String error,
                        @RequestParam(required = false) String message,
                        HttpSession session) {

        if (error != null) {
            if (message != null) {
                model.addAttribute("error", true);
                model.addAttribute("errorMessage", message);
            }

            // Kiểm tra và xử lý lỗi OAuth2 từ session
            Object oauth2Error = session.getAttribute("oauth2Error");
            if (oauth2Error != null) {
                model.addAttribute("errorMessage", oauth2Error.toString());
                // Xóa thông báo lỗi sau khi đã hiển thị
                session.removeAttribute("oauth2Error");
            }
        }

        return "auth/login";
    }
    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new User()); // Thêm một đối tượng User mới vào model
        return "auth/register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user, // Validate đối tượng User
                           @NotNull BindingResult bindingResult, // Kết quả của quá trình validate
                           Model model) {
        if (userService.emailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email đã tồn tại");
        }
        if (userService.usernameExists(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Tên người dùng đã tồn tại");
        }
        if (userService.phoneExists(user.getPhone())) {
            bindingResult.rejectValue("phone", "error.user", "Số điện thoại đã tồn tại");
        }
        if (bindingResult.hasErrors()) { // Kiểm tra nếu có lỗi validate
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "auth/register"; // Trả về lại view "register" nếu có lỗi
        }
        userService.save(user); // Lưu người dùng vào cơ sở dữ liệu
        userService.setDefaultRole(user.getUsername()); // Gán vai trò mặc định cho người dùng
        return "redirect:/auth/login"; // Chuyển hướng người dùng tới trang "login"
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(){
        return "auth/forgotPassword";
    }

    // Xử lý yêu cầu đặt lại mật khẩu
    @PostMapping("/forgot-password")
    public String forgotPasswordSubmit(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            userService.forgotPassword(email);
            redirectAttributes.addFlashAttribute("successMessage", "Mã xác thực đã được gửi qua email của bạn.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("email", email); // Lưu lại email đã nhập để hiển thị lại trên form
            return "redirect:/auth/forgot-password";
        }
        return "redirect:/auth/verify-token";
    }

    @GetMapping("/verify-token")
    public String verifyToken() {
        return "auth/verifyToken";
    }

    @PostMapping("/verify-token")
    public String verifyTokenSubmit(@RequestParam("resetPasswordToken") String resetPasswordToken,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (userService.verifyResetPasswordToken(resetPasswordToken)) {
                redirectAttributes.addAttribute("resetPasswordToken", resetPasswordToken);
            }
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/auth/verify-token";
        }
        return "redirect:/auth/reset-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("resetPasswordToken") String resetPasswordToken, Model model) {
        model.addAttribute("resetPasswordToken", resetPasswordToken);
        return "auth/resetPassword";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("resetPasswordToken") String resetPasswordToken,
                                @RequestParam("newPassword") String newPassword,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.setNewPassword(resetPasswordToken,  newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Mật khẩu đã được đặt lại thành công.");
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/auth/reset-password?verificationCode=" + resetPasswordToken;
        }
    }
}
