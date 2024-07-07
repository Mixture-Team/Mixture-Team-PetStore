package hutech.mixture.petstore.repository;

import hutech.mixture.petstore.models.Category;
import hutech.mixture.petstore.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentId(Long parentId);

    @Query("SELECT c FROM Category c WHERE c.isDeleted = false")
    List<Category> findAllByDeletedFalse();
}
