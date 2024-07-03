package hutech.mixture.petstore.controllers;

import hutech.mixture.petstore.models.Cart;
import hutech.mixture.petstore.models.CartItem;
import hutech.mixture.petstore.service.CartService;
import hutech.mixture.petstore.service.Cart_cartService;
import hutech.mixture.petstore.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

                              Model model) {
        List<CartItem> cartItems = cart_cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Chuyển hướng nếu giỏ hàng trống
        }

        double totalPrice = cart_cartService.calculateCartTotalPrice(); // Tính tổng tiền sản phẩm

        // Tạo đơn hàng mới và lưu vào cơ sở dữ liệu
        Cart order = cartService.createOrder(customerName, shippingAddress, phoneNumber, notes, paymentMethod, cartItems, totalPrice);

        // Xóa giỏ hàng sau khi đặt hàng thành công
        cart_cartService.clearCart();

        // Chuyển hướng đến trang xác nhận đơn hàng
        model.addAttribute("order", order);
        return "redirect:/payy/confirmation"; // Chuyển hướng đến trang xác nhận đơn hàng
    }

    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Đơn hàng của bạn đã được đặt thành công.");
        return "cart/order-confirmation"; // Trả về view cho trang xác nhận đơn hàng
    }

    @GetMapping("/orders")
    public String showOrderList(Model model) {
        List<Cart> orders = cartService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/order-list"; // Trả về view danh sách đơn hàng
    }

    @GetMapping("/orders/{id}")
    public String showOrderDetails(@PathVariable Long id, Model model) {
        Cart order = cartService.getOrderById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id đơn hàng không hợp lệ:" + id));
        model.addAttribute("order", order);
        return "order/order-details"; // Trả về view chi tiết đơn hàng
    }

    @GetMapping("/orders/new")
    public String showOrderForm(Model model) {
        model.addAttribute("order", new Cart());
        return "order/add-order"; // Trả về view để thêm mới đơn hàng
    }

    @PostMapping("/orders/save")
    public String saveOrder(@ModelAttribute Cart order) {
        cartService.saveOrder(order);
        return "redirect:/payy/orders"; // Chuyển hướng đến danh sách đơn hàng sau khi lưu thành công
    }
}
