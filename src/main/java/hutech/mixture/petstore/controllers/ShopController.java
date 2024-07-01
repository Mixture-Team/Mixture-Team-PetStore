package hutech.mixture.petstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShopController {
    @GetMapping("/cua-hang")
    public String shop() {
        return "shop";
    }

    @GetMapping("/thong-tin-san-pham")
    public String shopSingle(){
        return "shop-single";
    }
}