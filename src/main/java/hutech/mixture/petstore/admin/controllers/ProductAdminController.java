package hutech.mixture.petstore.admin.controllers;

import hutech.mixture.petstore.models.Product;
import hutech.mixture.petstore.services.CategoryService;
import hutech.mixture.petstore.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ProductAdminController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/products")
    public String getAllProducts(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "15") int size,
                                 Model model){
        Pageable pageable = PageRequest.of(page,size);
        Page<Product> products;
        products = productService.getAllProductForAdmin(pageable);
        model.addAttribute("products",products);
        return "/admin/management/product/list-products";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/admin/management/product/add-product";
    }
    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult result) {
        if (result.hasErrors()) {
            return "/admin/management/product/add-product";
        }
        productService.addProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/admin/management/product/update-product";
    }
    // Process the form for updating a product
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid Product product,
                                BindingResult result,
                                @RequestParam("file") MultipartFile file) {
        if (result.hasErrors()) {
            product.setId(id);
            return "/admin/management/product/update-product";
        }
        productService.updateProduct(product,file);
        return "redirect:/admin/products";
    }
}
