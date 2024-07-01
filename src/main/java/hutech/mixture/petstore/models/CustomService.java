package hutech.mixture.petstore.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "custom_service")  // Corrected table name (snake_case)
public class CustomService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)  // Added NotNull constraint
    private String name;

    @Column(name = "link")
    private String link;


    @Column(name = "is_deleted")
    private boolean deleted;

    @OneToMany(mappedBy = "customService", cascade = CascadeType.ALL)  // Eager fetching for related details
    private Set<ServiceDetail> serviceDetails = new HashSet<>();

}
