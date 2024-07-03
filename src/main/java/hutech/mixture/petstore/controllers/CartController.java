package hutech.mixture.petstore.controllers;

import hutech.mixture.petstore.models.District;
import hutech.mixture.petstore.models.Province;
import hutech.mixture.petstore.repository.DistrictRepository;
import hutech.mixture.petstore.repository.ProvinceRepository;
import hutech.mixture.petstore.service.Cart_cartService;
import hutech.mixture.petstore.service.DistrictService;
import hutech.mixture.petstore.service.ProductService;
import hutech.mixture.petstore.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private Cart_cartService cartService;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProvinceService provinceService;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private DistrictService districtService;

    @GetMapping
    public String showCart(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("cartTotalPrice", cartService.calculateCartTotalPrice());

        return "/cart/cart";
    }

    @RequestMapping(value = "/add", method = {RequestMethod.GET, RequestMethod.POST})
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.addToCart(productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
    @GetMapping("/pay")
    public String pay(Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("cartTotalPrice", cartService.calculateCartTotalPrice());
        model.addAttribute("provinces", provinceService.getAllProvinces());
        model.addAttribute("districts", districtService.getAllDistricts());
        return "/cart/pay";
    }

//    @GetMapping("/getFee")
//    @ResponseBody
//    public String getShippingFee(@RequestParam Long districtId) {
//        Optional<District> optionalDistrict = districtService.findById(districtId);
//        if (optionalDistrict.isPresent()) {
//            BigDecimal fee = BigDecimal.valueOf(optionalDistrict.get().getFee()); // Lấy giá trị fee từ Optional<Province>
//            return fee.toString(); // Trả về phí vận chuyển dưới dạng chuỗi
//        } else {
//            return "0"; // Trả về giá trị mặc định nếu không tìm thấy Province
//        }
//    }

    @GetMapping("/count")
    @ResponseBody
    public Map<String, Integer> getCartCount() {
        int count = cartService.getCartItems().size();
        Map<String, Integer> response = new HashMap<>();
        response.put("count", count);
        return response;
    }
    @GetMapping("/districts/{provinceId}")
    @ResponseBody
    public List<District> getDistrictsByProvince(@PathVariable Long provinceId) {
        return districtService.getDistrictsByProvinceId(provinceId);
    }

}
