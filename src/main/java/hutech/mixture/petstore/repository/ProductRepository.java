package hutech.mixture.petstore.repository;

import hutech.mixture.petstore.models.Category;
import hutech.mixture.petstore.models.CategoryParent;
import hutech.mixture.petstore.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long id);

    @Query("SELECT p FROM Product p WHERE p.category.parent.id = :categoryParentId")
    List<Product> findByCategoryParentId(@Param("categoryParentId") Long categoryParentId);
}
