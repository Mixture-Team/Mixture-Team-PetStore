package hutech.mixture.petstore.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_category_parent")
public class CategoryParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Parent category name is required")
    private String name;

    @NotBlank
    @Column(name = "link")
    private String link;

    @NotBlank
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @OneToMany(mappedBy = "parent")
    private Set<Category> categories = new HashSet<>();
}
