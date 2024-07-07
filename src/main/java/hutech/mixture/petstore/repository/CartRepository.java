package hutech.mixture.petstore.repository;

import hutech.mixture.petstore.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
