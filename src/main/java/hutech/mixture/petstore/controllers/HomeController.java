package hutech.mixture.petstore.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class HomeController {

    @GetMapping("/trang-chu")
    public String index() {
        return "/home/about";
    }

//    @GetMapping("/gioi-thieu")
//    public String about() {
//        return "/home/about";
//    }

    @GetMapping("/lien-he")
    public String contact() {
        return "/home/contact";
    }
}
