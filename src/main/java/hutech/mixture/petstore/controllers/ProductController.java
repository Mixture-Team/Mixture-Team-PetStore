package hutech.mixture.petstore.controllers;

import hutech.mixture.petstore.models.Category;
import hutech.mixture.petstore.models.CategoryParent;
import hutech.mixture.petstore.models.Product;
import hutech.mixture.petstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import hutech.mixture.petstore.services.CategoryParentService;
import hutech.mixture.petstore.services.CategoryService;

import java.util.List;


@Controller
@RequestMapping("/san-pham")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CategoryParentService categoryParentService;

    @GetMapping
    public String getProducts(@RequestParam(required = false) Long categoryId,
                              @RequestParam(required = false) Long categoryParentId,
                              Model model) {
        List<Product> products;
        List<CategoryParent> categoryParents = categoryParentService.getAllCategoryParents();
        List<Category> categories = categoryService.getAllCategories();
        if (categoryId != null) {
            products = productService.getProductByCategoryId(categoryId);
        }
        else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        model.addAttribute("categoryParents", categoryParents);
        model.addAttribute("categories",categories);
        return "/product/shop";
    }
}
