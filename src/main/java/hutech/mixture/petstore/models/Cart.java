package hutech.mixture.petstore.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
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
    @Column(name = "customer_name")
    private String customerName;

    @NotBlank
    @Column(name = "phone", length = 10, unique = true)
    @Length(min = 10, max = 10, message = "Phone must be 10 characters")
    @Pattern(regexp = "^[0-9]*$", message = "Phone must be number")
    private String phone;

    @Column(name = "notes")
    private String notes;

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

    @ManyToOne
    @JoinColumn(name = "shipping_id")
    private Province province;


    @OneToMany(mappedBy = "cart")
    private List<Cart_Product> cartProducts;



}
