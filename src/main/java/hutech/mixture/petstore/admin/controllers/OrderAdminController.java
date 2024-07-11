package hutech.mixture.petstore.admin.controllers;

import hutech.mixture.petstore.models.Cart;
import hutech.mixture.petstore.models.Product;
import hutech.mixture.petstore.services.CartService;
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
public class OrderAdminController {
    private final CartService cartService;

    @GetMapping("/orders")
    public String getAllOrders(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "15") int size,
                                 @RequestParam(required = false) String phone,
                                 Model model){
        Pageable pageable = PageRequest.of(page,size);
        Page<Cart> carts;

        if(phone != null && !phone.isEmpty()){
            carts = cartService.searchCartsByPhone(phone,pageable);
        } else {
            carts = cartService.getAllCartForAdmin(pageable);
        }
        model.addAttribute("carts",carts);
        model.addAttribute("phone",phone);
        return "/admin/management/order/list-orders";
    }

    @GetMapping("/orders/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               Model model) {
        Cart cart = cartService.getCartById(id);
        model.addAttribute("cart", cart);
        return "/admin/management/order/update-order";
    }
    // Process the form for updating a product
    @PostMapping("/orders/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid Cart cart,
                                BindingResult result){
        if (result.hasErrors()) {
            cart.setId(id);
            return "/admin/management/order/update-order";
        }
        Cart existingCart = cartService.getCartById(id);
        cart.setDistrict(existingCart.getDistrict());
        cartService.updateCart(cart);
        return "redirect:/admin/orders";
    }

}
