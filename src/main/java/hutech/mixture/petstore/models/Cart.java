package hutech.mixture.petstore.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "date_begin")
    private LocalDateTime dateBegin;

    @NotBlank
    @Column(name = "date_end")
    private LocalDateTime dateEnd;

    @NotBlank
    @Column(name = "payment_methods")
    private String paymentMethods;

    @NotBlank
    @Column(name = "order_status")
    private String orderStatus;

    @NotBlank
    @Column(name = "total_price")
    private double totalPrice;

    @NotBlank
    @Column(name = "address")
    private String address;

    @NotBlank
    @Column(name = "trading_code")
    private String tradingCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(name = "_cart_product",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();
}
