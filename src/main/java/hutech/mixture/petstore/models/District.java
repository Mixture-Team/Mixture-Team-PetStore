package hutech.mixture.petstore.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "_district")
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "District name is required")
    private String districtName;

    @NotBlank
    @Column(name = "fee")
    private double fee;

    @ManyToOne
    @JoinColumn(name = "province_id")
    @JsonBackReference
    private Province province;

    @OneToMany(mappedBy = "district")
    @JsonBackReference
//    private Set<Cart> carts = new HashSet<>();
    private List<Cart> carts;
}
