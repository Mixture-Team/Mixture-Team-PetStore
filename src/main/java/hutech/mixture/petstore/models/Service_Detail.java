package hutech.mixture.petstore.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "_service_detail")
public class Service_Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Service_Detail name is required")
    private String name;

    @NotBlank
    @Column(name = "link")
    private String link;

    @NotBlank
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @NotBlank
    @Column(name = "price")
    private double price;

    @NotBlank
    @Column(name = "img")
    private String img;

    @ManyToOne
    @JoinColumn(name ="service_id")
    private Service service;
}
