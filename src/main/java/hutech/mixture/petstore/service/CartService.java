package hutech.mixture.petstore.service;

import hutech.mixture.petstore.models.CartItem;
import hutech.mixture.petstore.models.Product;
import hutech.mixture.petstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CartService {
    // Danh sách các mặt hàng trong giỏ hàng
    private List<CartItem> cartItems = new ArrayList<>();

    // Repository để tương tác với cơ sở dữ liệu sản phẩm
    @Autowired
    private ProductRepository productRepository;

    // Phương thức để thêm sản phẩm vào giỏ hàng
    public void addToCart(Long productId, int quantity) {
        // Tìm sản phẩm trong cơ sở dữ liệu
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm: " + productId));

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        for (CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                // Nếu đã có, cập nhật số lượng và tổng giá
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        // Nếu chưa có, thêm một mục mới vào giỏ hàng
        cartItems.add(new CartItem(product, quantity));
    }

    // Phương thức để lấy danh sách các mặt hàng trong giỏ hàng
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    // Phương thức để xóa một mặt hàng khỏi giỏ hàng dựa trên productId
    public void removeFromCart(Long productId) {
        cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
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
}
