package hutech.mixture.petstore.models;

public class CartItem {
    private Product product;
    private int quantity;
    private double total_price;


    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.total_price = calculateTotalPrice();

    }

    private double calculateTotalPrice() {
        return product.getPromotionPrice() * quantity;
    }


    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.total_price = calculateTotalPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.total_price = calculateTotalPrice();
    }

}
