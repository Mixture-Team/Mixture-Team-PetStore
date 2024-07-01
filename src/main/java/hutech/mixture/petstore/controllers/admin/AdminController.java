package hutech.mixture.petstore.controllers.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String index(){
        return "/admin/index";
    }

    @GetMapping("/accounts")
    public String accounts(){
        return "/admin/management/account/list-accounts";
    }

    @GetMapping("/categories")
    public String categories(){
        return "/admin/management/category/list-categories";
    }

    @GetMapping("/orders")
    public String orders(){
        return "/admin/management/order/list-orders";
    }

    @GetMapping("/products")
    public String products(){
        return "/admin/management/product/list-products";
    }

    @GetMapping("/add-products")
    public String addProducts(){
        return "/admin/management/product/add-product";
    }
}
