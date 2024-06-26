package hutech.mixture.petstore.controllers;

import hutech.mixture.petstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

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
    public String pay(  Model model) {
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("cartTotalPrice", cartService.calculateCartTotalPrice());
        return "/cart/pay";
    }

    @GetMapping("/count")
    @ResponseBody
    public Map<String, Integer> getCartCount() {
        int count = cartService.getCartItems().size();
        Map<String, Integer> response = new HashMap<>();
        response.put("count", count);
        return response;
    }

}
