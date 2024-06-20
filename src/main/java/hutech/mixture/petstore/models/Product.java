package hutech.mixture.petstore.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank
    @Column(name = "nums")
    private int nums;

    @NotBlank
    @Column(name = "price")
    private double price;

    @NotBlank
    @Column(name = "description")
    private String description;

    @NotBlank
    @Column(name = "img")
    private String img;

    @NotBlank
    @Column(name = "link")
    private String link;

    @NotBlank
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @NotBlank
    @Column(name = "discount")
    private double discount;

    @NotBlank
    @Column(name = "promotion_price")
    private double promotionPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<Cart_Product> cart_products;
//    @ManyToMany(mappedBy = "products")
//    private Set<Cart> carts = new HashSet<>();
}
