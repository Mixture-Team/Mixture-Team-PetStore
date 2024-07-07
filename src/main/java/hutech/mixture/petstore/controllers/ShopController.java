//package hutech.mixture.petstore.controllers;
//
//import hutech.mixture.petstore.models.Product;
//import org.springframework.ui.Model;
//import hutech.mixture.petstore.services.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//import java.util.Optional;
//
//@Controller
//public class ShopController {
//    private final ProductService productService;
//
//    @Autowired
//    public ShopController(ProductService productService) {
//        this.productService = productService;
//    }
//
//    @GetMapping("/cua-hang")
//    public String shop(Model model) {
//        model.addAttribute("products", productService.getAllProducts());
//        return "shop/shop";
//    }
//
//
//    @GetMapping("/thong-tin-san-pham/{productId}")
//    public String shopSingle(@PathVariable Long productId, Model model) {
//        Optional<Product> productOptional = productService.getProductById(productId);
//        Product product = productOptional.orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + productId));
//        model.addAttribute("product", product);
//        return "shop/shop-single";
//    }
//
//
//
//}
