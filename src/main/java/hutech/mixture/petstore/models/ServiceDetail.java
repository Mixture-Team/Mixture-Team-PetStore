package hutech.mixture.petstore.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service_detail")  // Corrected table name (snake_case)
public class ServiceDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)  // Added NotNull constraint
    private String name;

    @Column(name = "link")
    private String link;

    @Column(name = "price")
    private int price;

    @Column(name = "is_deleted")
    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)  // Added NotNull constraint
    private CustomService customService;

}
