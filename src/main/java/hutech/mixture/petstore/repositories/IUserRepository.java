package hutech.mixture.petstore.repositories;


import hutech.mixture.petstore.enums.AuthenticationType;
import hutech.mixture.petstore.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<Object> findByPhone(String phone);

    Optional<User> findByResetPasswordToken(String resetPasswordToken);


    @Modifying
    @Query("UPDATE User u SET u.authenticationType = ?2 WHERE u.username = ?1")
    void updateAuthenticationType(String username, AuthenticationType authenticationType);
}
