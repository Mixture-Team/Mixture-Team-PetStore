package hutech.mixture.petstore.admin.controllers;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import hutech.mixture.petstore.models.Category;
import hutech.mixture.petstore.models.Product;
import hutech.mixture.petstore.services.CategoryParentService;
import hutech.mixture.petstore.services.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class CategoryAdminController
{
    private final CategoryService categoryService;
    private final CategoryParentService categoryParentService;

    @GetMapping("/categories")
    public String getAllCategories(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   Model model){
        Pageable pageable = PageRequest.of(page,size);
        Page<Category> categories;
        categories = categoryService.getAllCategoriesForAdmin(pageable);
        model.addAttribute("categories", categories);
        return "/admin/management/category/list-categories";
    }

    @GetMapping("/categories/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("categoryParents", categoryParentService.getAllCategoryParents());
        return "/admin/management/category/add-category";
    }
    @PostMapping("/categories/add")
    public String addCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, @NotNull Model model) {
        if (!StringUtils.isBlank(category.getName())) {
            // Kiểm tra nếu tên danh mục bắt đầu bằng số hoặc ký tự đặc biệt
            char firstChar = category.getName().charAt(0);
            if (!Character.isLetter(firstChar)) {
                result.rejectValue("name", "error.name", "Category name first character must be a letter");
            }
        }
        if (result.hasErrors()) {
            model.addAttribute("categoryParents", categoryParentService.getAllCategoryParents());
            return "/admin/management/category/add-category";
        }
        categoryService.addProduct(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        model.addAttribute("categoryParents", categoryParentService.getAllCategoryParents());
        return "/admin/management/category/update-category";
    }
    // Process the form for updating a product
    @PostMapping("/categories/update/{id}")
    public String updateCategory(@PathVariable Long id,
                                @Valid Category category,
                                BindingResult result) {
        if (result.hasErrors()) {
            category.setId(id);
            return "/admin/management/category/update-category";
        }
        categoryService.updateCategory(category);
        return "redirect:/admin/categories";
    }
}
