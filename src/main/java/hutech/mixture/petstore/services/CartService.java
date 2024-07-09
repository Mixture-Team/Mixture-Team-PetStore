package hutech.mixture.petstore.services;

import hutech.mixture.petstore.models.*;
import hutech.mixture.petstore.repositories.UserRepository;
import hutech.mixture.petstore.repository.CartRepository;
import hutech.mixture.petstore.repository.Cart_ProductRepository;
import hutech.mixture.petstore.repository.DistrictRepository;
import hutech.mixture.petstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static hutech.mixture.petstore.VNPay.Config.getRandomNumber;

@Service
public class CartService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private Cart_ProductRepository cartProductRepository;

    @Transactional
    public Cart createOrder(String customerName, String shippingAddress, String phoneNumber, String notes, String paymentMethod, List<CartItem> cartItems, double totalPrice, Long districtId, double totalShippingPrice) {

        // Lấy người dùng hiện tại
        Long currentUserId = userService.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart order = new Cart();
        order.setCustomerName(customerName);
        order.setAddress(shippingAddress);
        order.setPhone(phoneNumber);
        order.setNotes(notes);
        order.setTotalPrice(totalPrice);
        order.setTotalshippingprice(totalShippingPrice);
        order.setPaymentMethods(paymentMethod);

        order.setDateBegin(LocalDateTime.now());
        order.setDateEnd(LocalDateTime.now());
        order.setOrderStatus("đang xử lý");
        order.setTradingCode("0");

        District district = districtRepository.findById(districtId).orElseThrow(() -> new RuntimeException("District not found"));
        order.setDistrict(district);
        order.setUser(currentUser);

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

        return order;
    }

    public Cart saveOrder(Cart order) {
        return cartRepository.save(order);
    }




}
