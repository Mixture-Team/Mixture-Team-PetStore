package hutech.mixture.petstore.repositories;


import hutech.mixture.petstore.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<Object> findByPhone(String phone);

    Optional<User> findByResetPasswordToken(String resetPasswordToken);
}
