package hutech.mixture.petstore.controllers;

import hutech.mixture.petstore.models.User;
import hutech.mixture.petstore.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/profile")
    public String viewProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username); // Retrieve the current user's information
        model.addAttribute("user", user);
        return "user/profile";
    }

    // Đường dẫn để hiển thị trang chỉnh sửa thông tin
    @GetMapping("/profile/edit/{id}")
    public String editProfile(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user/edit-profile";
    }

    @PostMapping("/profile/edit/{id}")
    public String updateProfile(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("user") User user,
                                BindingResult bindingResult,
                                Model model) {
        User currentUser = userService.findById(id);
        // Kiểm tra và xử lý lỗi
        if (!currentUser.getEmail().equals(user.getEmail()) && userService.emailExists(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email đã tồn tại");
        }
//        if (!currentUser.getPhone().equals(user.getPhone()) && userService.phoneExists(user.getPhone())) {
//            bindingResult.rejectValue("phone", "error.user", "Số điện thoại đã tồn tại");
//        }
        // Nếu có lỗi, trả về trang chỉnh sửa với thông báo lỗi
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "user/edit-profile"; // Ensure the view name matches
        }
        // Cập nhật thông tin người dùng nếu không có lỗi
        userService.updateUser(user);
        // Sau khi cập nhật thành công, điều hướng về trang thông tin cá nhân
        return "user/profile";
    }
}

