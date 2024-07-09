package hutech.mixture.petstore.repository;

import hutech.mixture.petstore.models.Cart;
import hutech.mixture.petstore.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
}
