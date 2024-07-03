package hutech.mixture.petstore.repository;

import hutech.mixture.petstore.models.Category;
import hutech.mixture.petstore.models.CategoryParent;
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
}
