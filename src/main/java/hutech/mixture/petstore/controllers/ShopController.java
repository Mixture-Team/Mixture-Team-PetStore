package hutech.mixture.petstore.controllers;

import hutech.mixture.petstore.service.ProductService;
import hutech.mixture.petstore.models.CategoryParent;
import hutech.mixture.petstore.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import hutech.mixture.petstore.services.CategoryParentService;
import hutech.mixture.petstore.services.CategoryService;
import hutech.mixture.petstore.service.ProductService;

import java.util.List;
import java.util.Optional;

@Controller
public class ShopController {

    private final ProductService productService;


    private final CategoryService categoryService;
    private final CategoryParentService categoryParentService;
    @Autowired
    public ShopController(ProductService productService, CategoryService categoryService, CategoryParentService categoryParentService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.categoryParentService = categoryParentService;
    }
/*    @GetMapping("/cua-hang")
    public String shop(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("categoryParents", categoryParentService.getAllCategoryParents());
        return "shop";
    }*/
    @GetMapping("/cua-hang")
    public String shop(@RequestParam(value = "categoryParent", required = false) String categoryParent, Model model) {
        List<Product> products;
        if (categoryParent != null && !categoryParent.isEmpty()) {
            products = productService.getProductsByCategoryParent(categoryParent);
        } else {
            products = productService.getAllProducts();
        }
        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("categoryParents", categoryParentService.getAllCategoryParents());
        return "shop";
    }


    @GetMapping("/thong-tin-san-pham/{productId}")
    public String shopSingle(@PathVariable Long productId, Model model) {
        Optional<Product> productOptional = productService.getProductById(productId);
        Product product = productOptional.orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + productId));
        model.addAttribute("product", product);
        return "shop-single";
    }


}
