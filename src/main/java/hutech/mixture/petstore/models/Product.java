package hutech.mixture.petstore.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    @NotNull
    @Column(name = "nums")
    private int nums;

    @NotNull
    @Column(name = "price")
    private double price;

    @NotBlank(message = "Description is required")
    @Column(name = "description")
    private String description;

    @Column(name = "img")
    private String img;

        @NotBlank(message = "Link is required")
        @Column(name = "link")
        private String link;

    @NotNull
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @NotNull
    @Column(name = "discount")
    private double discount;

    @NotNull
    @Column(name = "promotion_price")
    private double promotionPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Cart_Product> cart_products;
//    @ManyToMany(mappedBy = "products")
//    private Set<Cart> carts = new HashSet<>();
}
