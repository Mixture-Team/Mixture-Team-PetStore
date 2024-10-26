package hutech.mixture.petstore.repositories;


import hutech.mixture.petstore.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<Role,Long> {
    Role findRoleById(Long id);
    Role findRoleByName(String name);
}
