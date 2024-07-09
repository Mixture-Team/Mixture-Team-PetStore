package hutech.mixture.petstore.controllers;

import hutech.mixture.petstore.models.Cart;
import hutech.mixture.petstore.models.CartItem;
import hutech.mixture.petstore.models.Cart_Product;
import hutech.mixture.petstore.services.CartService;
import hutech.mixture.petstore.services.Cart_cartService;
import hutech.mixture.petstore.services.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/payy")
public class PayController {

    @Autowired
    private Cart_cartService cart_cartService;

    @Autowired
    private CartService cartService;

    @Autowired
    private DistrictService districtService;

    @GetMapping("/getFee")
    @ResponseBody
    public double getShippingFee(@RequestParam Long districtId) {
        return districtService.getShippingFeeByDistrictId(districtId);
    }

    @PostMapping("/submit")
    public String submitOrder(@RequestParam String customerName,
                              @RequestParam String shippingAddress,
                              @RequestParam String phoneNumber,
                              @RequestParam String notes,
                              @RequestParam String paymentMethod,
                              @RequestParam Long districtId, // Nhận districtId từ form
                              @RequestParam Double totalShippingPrice, // Nhận giá trị totalPriceSpan từ form
                              Model model ,
                              RedirectAttributes redirectAttributes
                           ) {
        List<CartItem> cartItems = cart_cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Chuyển hướng nếu giỏ hàng trống
        }


        double totalPrice = cart_cartService.calculateCartTotalPrice(); // Tính tổng tiền sản phẩm

        // Tạo đơn hàng mới và lưu vào cơ sở dữ liệu
        Cart order = cartService.createOrder(customerName, shippingAddress, phoneNumber, notes, paymentMethod, cartItems, totalPrice, districtId,totalShippingPrice);

        // Xóa giỏ hàng sau khi đặt hàng thành công
        cart_cartService.clearCart();

        // Chuyển hướng đến trang xác nhận đơn hàng
         model.addAttribute("order", order);
         redirectAttributes.addFlashAttribute("order", order);

        return "redirect:/payy/confirmation"; // Chuyển hướng đến trang xác nhận đơn hàng
    }

    @GetMapping("/confirmation")
    public String orderConfirmation(@ModelAttribute("order") Cart order, Model model) {
        model.addAttribute("message", "Đơn hàng của bạn đã được đặt thành công.");
        if (order != null) {
            List<Cart_Product> cartProducts = order.getCartProducts();
            model.addAttribute("cartProducts", cartProducts);
        }
        return "cart/order-confirmation"; // Trả về view cho trang xác nhận đơn hàng
    }

    @PostMapping("/orders/save")
    public String saveOrder(@ModelAttribute Cart order) {
        cartService.saveOrder(order);
        return "redirect:/payy/orders"; // Chuyển hướng đến danh sách đơn hàng sau khi lưu thành công
    }
}
