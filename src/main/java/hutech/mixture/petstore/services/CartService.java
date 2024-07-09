package hutech.mixture.petstore.services;

import hutech.mixture.petstore.models.*;
import hutech.mixture.petstore.repositories.UserRepository;
import hutech.mixture.petstore.repository.CartRepository;
import hutech.mixture.petstore.repository.Cart_ProductRepository;
import hutech.mixture.petstore.repository.DistrictRepository;
import hutech.mixture.petstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        order.setOrderStatus("Đang xử lý");
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
        List<Cart_Product> cartProducts = cartProductRepository.findByCartId(order.getId());
        order.setCartProducts(cartProducts);
        return order;
    }

    public Cart saveOrder(Cart order) {
        return cartRepository.save(order);
    }

    public List<Cart> getOrdersForLoggedInUser() {
        Long currentUserId = userService.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(currentUser);
    }
    public Optional<Cart> getOrderById(Long orderId) {
        return cartRepository.findById(orderId);
      
    public Page<Cart> getAllCartForAdmin(Pageable pageable){
        return cartRepository.findAll(pageable);
    }

    public Cart getCartById(Long id){
        return cartRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Không tìm thấy đơn hàng có id " + id)
        );
    }

    @Transactional
    public Cart updateCart(Cart cart){
        Cart existingCart = cartRepository.findById(cart.getId()).orElseThrow(
                () -> new RuntimeException("Không tìm thấy đơn hàng có id " + cart.getId())
        );
        existingCart.setCustomerName(cart.getCustomerName());
        existingCart.setPhone(cart.getPhone());
        existingCart.setNotes(cart.getNotes());
        existingCart.setDateBegin(cart.getDateBegin());
        existingCart.setAddress(cart.getAddress());
        existingCart.setPaymentMethods(cart.getPaymentMethods());
        existingCart.setTotalPrice(cart.getTotalPrice());
        existingCart.setTotalshippingprice(cart.getTotalshippingprice());
        existingCart.setTradingCode(cart.getTradingCode());
        existingCart.setUser(cart.getUser());
        existingCart.setDistrict(cart.getDistrict());

        if(cart.getOrderStatus().equals("Giao hàng thành công") || cart.getOrderStatus().equals("Huỷ")){
            existingCart.setDateEnd(LocalDateTime.now());
        }

        existingCart.setOrderStatus(cart.getOrderStatus());
        return existingCart;
    }

    public Page<Cart> searchCartsByPhone(String phone, Pageable pageable){
        return cartRepository.findByPhoneContaining(phone,pageable);
    }
}
