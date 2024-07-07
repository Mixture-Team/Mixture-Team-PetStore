package hutech.mixture.petstore.repository;

import hutech.mixture.petstore.models.Category;
import hutech.mixture.petstore.models.CategoryParent;
import hutech.mixture.petstore.models.PriceRange;
import hutech.mixture.petstore.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import static javax.swing.text.html.HTML.Tag.SELECT;
import static org.hibernate.grammars.hql.HqlParser.FROM;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryId(Long id, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false")
    Page<Product> findAllByDeletedFalse(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.parent.id = :categoryParentId")
    Page<Product> findByCategoryParentId(@Param("categoryParentId") Long categoryParentId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(:categoryParentId IS NULL OR p.category.parent.id = :categoryParentId) AND " +
            "(:minPrice IS NULL OR p.promotionPrice >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.promotionPrice < :maxPrice)")
    Page<Product> searchByPriceAndCatoParent(
            @Param("categoryParentId") Long categoryParentId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);
    ///////
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> search(@Param("name") String name, Pageable pageable);

    // search auto
    List<Product> findByNameContainingIgnoreCase(String name);

}
