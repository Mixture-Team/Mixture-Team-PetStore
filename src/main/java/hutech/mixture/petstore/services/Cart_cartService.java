package hutech.mixture.petstore.services;

import hutech.mixture.petstore.models.CartItem;
import hutech.mixture.petstore.models.Product;
import hutech.mixture.petstore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class Cart_cartService {

    // Danh sách các mặt hàng trong giỏ hàng
    private List<CartItem> cartItems = new ArrayList<>();

    // Repository để tương tác với cơ sở dữ liệu sản phẩm
    @Autowired
    private ProductRepository productRepository;

    // Phương thức để thêm sản phẩm vào giỏ hàng
    public boolean addToCart(Long productId, int quantity) {
        // Tìm sản phẩm trong cơ sở dữ liệu
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm: " + productId));

        // Kiểm tra xem có sản phẩm này trong giỏ hàng chưa
        Optional<CartItem> existingItem = cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // Nếu đã có sản phẩm này trong giỏ hàng, cập nhật số lượng và tổng giá
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;
            if (newQuantity > product.getNums()) {
                return false; // Không đủ số lượng sản phẩm
            }
            item.setQuantity(newQuantity);
            item.setTotalPrice(product.getPromotionPrice() * newQuantity); // Cập nhật lại tổng giá
        } else {
            // Nếu chưa có, thêm một mục mới vào giỏ hàng
            if (quantity > product.getNums()) {
                return false; // Không đủ số lượng sản phẩm
            }
            cartItems.add(new CartItem(product, quantity));
        }
        return true; // Thêm sản phẩm vào giỏ hàng thành công
    }

    // Phương thức để lấy danh sách các mặt hàng trong giỏ hàng
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    // Phương thức để xóa một mặt hàng khỏi giỏ hàng dựa trên productId
    public void removeFromCart(Long productId) {
        boolean removed = cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
        if (!removed) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm trong giỏ hàng để xóa: " + productId);
        }
    }

    // Phương thức để xóa toàn bộ các mặt hàng trong giỏ hàng
    public void clearCart() {
        cartItems.clear();
    }

    // Phương thức tính tổng total_price của các mặt hàng trong giỏ hàng
    public double calculateCartTotalPrice() {
        double totalPrice = 0.0;
        for (CartItem item : cartItems) {
            totalPrice += item.getTotal_price();
        }
        return totalPrice;
    }

    public void updateQuantity(Long productId, int quantity) {
        cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
    }



}
