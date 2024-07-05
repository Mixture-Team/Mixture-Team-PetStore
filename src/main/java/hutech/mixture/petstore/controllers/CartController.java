package hutech.mixture.petstore.controllers;

import hutech.mixture.petstore.models.CartItem;
import hutech.mixture.petstore.models.CartItemResponse;
import hutech.mixture.petstore.models.District;
import hutech.mixture.petstore.repository.DistrictRepository;
import hutech.mixture.petstore.repository.ProvinceRepository;
import hutech.mixture.petstore.services.Cart_cartService;
import hutech.mixture.petstore.services.DistrictService;
import hutech.mixture.petstore.services.ProductService;
import hutech.mixture.petstore.services.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, @RequestParam(required = false) String submit) {
        cartService.addToCart(productId, quantity);

        if ("buy".equals(submit)) {
            return "redirect:/cart"; // Điều hướng đến trang giỏ hàng
        } else if ("addToCart".equals(submit)) {
            return "redirect:/cua-hang"; // Điều hướng đến trang cửa hàng
        }
        return "redirect:/"; // Điều hướng mặc định
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

    @GetMapping("/getFee")
    @ResponseBody
    public String getShippingFee(@RequestParam Long districtId) {
        Optional<District> optionalDistrict = districtService.findById(districtId);
        if (optionalDistrict.isPresent()) {
            double fee = optionalDistrict.get().getFee();
            return String.valueOf(fee);
        } else {
            return "0"; // Trả về giá trị mặc định nếu không tìm thấy District
        }
    }

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

    @ResponseBody
    @PostMapping("/api/update")
    public List<CartItemResponse> updateQuantity(@RequestParam long productId, @RequestParam int quantity) {
        System.out.println("ccc");
        cartService.updateQuantity(productId, quantity);
        List<CartItem> carts = cartService.getCartItems();
        return carts.stream().map(CartItemResponse::new).toList();
    }


    @PostMapping("/total")
    public ResponseEntity<Double> getCartTotal() {
        System.out.println();
        double total = cartService.calculateCartTotalPrice();
        return ResponseEntity.ok(total);
    }

}
