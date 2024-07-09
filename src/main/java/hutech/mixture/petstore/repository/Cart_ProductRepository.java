package hutech.mixture.petstore.repository;


import hutech.mixture.petstore.models.Cart_Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface Cart_ProductRepository extends JpaRepository<Cart_Product, Long> {
    List<Cart_Product> findByCartId(Long cartId);
}
