package hutech.mixture.petstore.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "_cart_product")
public class Cart_Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private double total_price;

    @ManyToOne
    @JoinColumn(name ="product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name ="service_detail_id")
    private Service_Detail serviceDetail;


    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
}
