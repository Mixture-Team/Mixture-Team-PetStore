package hutech.mixture.petstore.models;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Product product;
    private Province province;
    private District district;
    private Service_Detail serviceDetail;
    private int quantity;
    private double total_price; // Giá tổng cộng sản phẩm
    private double shipping_price; // Phí vận chuyển
    private double totalPrice; // Tổng tiền tạm thời bao gồm cả sản phẩm và phí vận chuyển


    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.total_price = calculateTotalPrice();
        this.shipping_price = calculateShippingPrice();
        this.totalPrice = calculateTotalPriceWithShipping();

    }
//     Phương thức tính phí vận chuyển
    private double calculateShippingPrice() {
        if (district!= null) {
            return district.getFee();
        }
        return 0; // Trường hợp không có tỉnh thành được chọn
    }

//     Phương thức tính tổng giá sản phẩm cộng phí vận chuyển
    private double calculateTotalPriceWithShipping() {
        return calculateTotalPrice() + calculateShippingPrice();
    }
    private double calculateTotalPrice() {
        return product.getPromotionPrice() * quantity;
    }
    public void setProduct(Product product) {
        this.product = product;
        this.total_price = calculateTotalPrice();
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.total_price = calculateTotalPrice();
    }

}
