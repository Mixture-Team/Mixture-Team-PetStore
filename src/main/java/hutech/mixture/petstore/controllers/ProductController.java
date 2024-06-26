package hutech.mixture.petstore.controllers;

import hutech.mixture.petstore.models.Category;
import hutech.mixture.petstore.models.CategoryParent;
import hutech.mixture.petstore.models.Product;
import hutech.mixture.petstore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "15") int size,
                              Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products;
        List<CategoryParent> categoryParents = categoryParentService.getAllCategoryParents();
        List<Category> categories = categoryService.getAllCategories();

        if (categoryId != null) {
            products = productService.getProductByCategoryId(categoryId,pageable);
        }
        else {
            products = productService.getAllProducts(pageable);
        }
        model.addAttribute("products", products);
        model.addAttribute("categoryParents", categoryParents);
        model.addAttribute("categories",categories);

        model.addAttribute("products", products.getContent());
        model.addAttribute("currentPage", products.getNumber());
        model.addAttribute("totalPages", products.getTotalPages());
        return "/product/shop";
    }

    @GetMapping("/by-category-parent")
    @ResponseBody
    public ResponseEntity<Page<Product>> getProductsByCategoryParent(@RequestParam(required = false) Long categoryParentId,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "15") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products;
        if (categoryParentId == null) {
            products = productService.getAllProducts(pageable);
        } else {
            products = productService.getProductByCategoryParentId(categoryParentId, pageable);
        }
        return ResponseEntity.ok(products);
    }
}
