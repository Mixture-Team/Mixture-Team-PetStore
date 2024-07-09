package hutech.mixture.petstore.repository;

import hutech.mixture.petstore.models.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Page<Cart> findByPhoneContaining(String phone, Pageable pageable);
}
