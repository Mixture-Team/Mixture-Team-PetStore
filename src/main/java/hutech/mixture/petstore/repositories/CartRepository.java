package hutech.mixture.petstore.repositories;

import hutech.mixture.petstore.models.Cart;
import hutech.mixture.petstore.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);
    Page<Cart> findByPhoneContaining(String phone, Pageable pageable);
    Cart findByTradingCode(String tradingCode);
}
