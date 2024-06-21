package hutech.mixture.petstore.controllers;

import hutech.mixture.petstore.models.User;
import hutech.mixture.petstore.repositories.IUserRepository;
import hutech.mixture.petstore.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // Đánh dấu lớp này là một Controller trong Spring MVC.
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final IUserRepository userRepository;
    @GetMapping("/login")
    public String login() {
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
    public String forgotPasswordSubmit(@RequestParam("email") String email, RedirectAttributes redirectAttributes, User user) {
        try {
            userService.forgotPassword(email,user.getResetPasswordToken());
            redirectAttributes.addFlashAttribute("successMessage", "Yêu cầu đặt lại mật khẩu đã được gửi qua email của bạn.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/auth/reset-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam("verificationCode") String verificationCode, Model model) {
        model.addAttribute("verificationCode", verificationCode);
        return "auth/resetPassword";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("verificationCode") String verificationCode,
                                @RequestParam("newPassword") String newPassword,
                                RedirectAttributes redirectAttributes,
                                User user) {
        try {
            userService.setNewPassword(verificationCode,  newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Mật khẩu đã được đặt lại thành công.");
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/auth/reset-password?verificationCode=" + verificationCode;
        }
    }


}
