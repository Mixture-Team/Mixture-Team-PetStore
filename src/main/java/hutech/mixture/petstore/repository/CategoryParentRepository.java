package hutech.mixture.petstore.repository;

import hutech.mixture.petstore.models.CategoryParent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryParentRepository extends JpaRepository<CategoryParent, Long> {
}
