package hutech.mixture.petstore.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_service")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Service name is required")
    private String name;

    @NotBlank
    @Column(name = "link")
    private String link;

    @NotBlank
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @NotBlank
    @Column(name = "img")
    private String img;

    @OneToMany(mappedBy = "service")
    private List<Service_Detail> service_details;
    //private Set<Service_Detail> serviceDetails = new HashSet<>();
}
