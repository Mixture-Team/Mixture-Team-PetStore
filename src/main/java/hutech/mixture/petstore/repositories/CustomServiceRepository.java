package hutech.mixture.petstore.repositories;

import hutech.mixture.petstore.models.CustomService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomServiceRepository extends JpaRepository<CustomService, Long> {
    @Query("SELECT cs FROM CustomService cs WHERE cs.deleted = false")
    List<CustomService> findAllActiveCustomServices();

    CustomService findByLink(String link);
}
