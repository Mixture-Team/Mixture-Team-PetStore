package hutech.mixture.petstore.repository;


import hutech.mixture.petstore.models.Cart_Product;
import org.springframework.data.jpa.repository.JpaRepository;


public interface Cart_ProductRepository extends JpaRepository<Cart_Product, Long> {
}
