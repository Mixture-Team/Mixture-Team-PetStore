package hutech.mixture.petstore.service;

import hutech.mixture.petstore.models.Cart;
import hutech.mixture.petstore.models.CartItem;
import hutech.mixture.petstore.models.Cart_Product;
import hutech.mixture.petstore.repository.CartRepository;
import hutech.mixture.petstore.repository.Cart_ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private Cart_ProductRepository cartProductRepository;

    @Transactional
    public Cart createOrder(String customerName, String shippingAddress, String phoneNumber, String notes, String paymentMethod, List<CartItem> cartItems, double totalPrice) {
        Cart order = new Cart();
        order.setCustomerName(customerName);
        order.setAddress(shippingAddress);
        order.setPhone(phoneNumber);
        order.setNotes(notes);
        order.setTotalPrice(totalPrice);
        order.setPaymentMethods(paymentMethod);

        order.setDateBegin(LocalDateTime.now()); // Ngày hiện tại
        order.setDateEnd(LocalDateTime.now()); // Ngày hiện tại
        order.setOrderStatus("đang xử lý"); // Trạng thái đơn hàng
        order.setTradingCode("0"); // Mã giao dịch

        // Lưu đơn hàng
        order = cartRepository.save(order);

        // Lưu chi tiết đơn hàng
        for (CartItem item : cartItems) {
            Cart_Product detail = new Cart_Product();
            detail.setQuantity(item.getQuantity());
            detail.setTotal_price(item.getTotal_price());
            detail.setCart(order);
            detail.setProduct(item.getProduct());
            detail.setServiceDetail(item.getServiceDetail());
            cartProductRepository.save(detail);
        }

        // Các bước xử lý thanh toán thành công có thể được triển khai ở đây,
        // bao gồm tích hợp với cổng thanh toán, xác nhận thanh toán, cập nhật trạng thái đơn hàng, v.v.

        return order;
    }

    public List<Cart> getAllOrders() {
        return cartRepository.findAll();
    }

    public Optional<Cart> getOrderById(Long id) {
        return cartRepository.findById(id);
    }

    public Cart saveOrder(Cart order) {
        return cartRepository.save(order);
    }
}
