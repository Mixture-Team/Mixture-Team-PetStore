package hutech.mixture.petstore.admin.controllers;

import hutech.mixture.petstore.models.Category;
import hutech.mixture.petstore.models.Role;
import hutech.mixture.petstore.models.User;
import hutech.mixture.petstore.services.RoleService;
import hutech.mixture.petstore.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/users")
    public String getAllUsers(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "15") int size,
                              Model model){
        Pageable pageable = PageRequest.of(page,size);
        Page<User> users;
        users = userService.getAllUsersForAdmin(pageable);
        model.addAttribute("users",users);
        return "/admin/management/user/list-users";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles",roleService.getAllRoles());
        return "/admin/management/user/update-user";
    }
    // Process the form for updating a product
    @PostMapping("/users/update/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @Valid User user,
                                 BindingResult result) {
        if (result.hasErrors()) {
            user.setId(id);
            return "/admin/management/user/update-user";
        }
        userService.updateUser(user);
        return "redirect:/admin/users";
    }
}
