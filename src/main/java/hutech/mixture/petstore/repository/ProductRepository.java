package hutech.mixture.petstore.repository;

import hutech.mixture.petstore.models.CategoryParent;
import hutech.mixture.petstore.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryParent_Name(String categoryParentName);
}
