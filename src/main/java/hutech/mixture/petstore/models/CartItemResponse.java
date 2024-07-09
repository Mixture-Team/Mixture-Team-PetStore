package hutech.mixture.petstore.models;



import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class CartItemResponse {
    private Long productId;
    private String productName;
    private String productImg;
    private double price;
    private int quantity;
    private double total;

    public CartItemResponse(CartItem cartItem) {
        this.productId = cartItem.getProduct().getId();
        this.productName = cartItem.getProduct().getName();
        this.productImg = cartItem.getProduct().getImg();
        this.price = cartItem.getProduct().getPromotionPrice(); // Sửa để lấy giá khuyến mãi
        this.quantity = cartItem.getQuantity();
        this.total = this.price * this.quantity;;
    }
}