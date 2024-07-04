package hutech.mixture.petstore.Repository;

import hutech.mixture.petstore.models.ServiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceDetailRepository extends JpaRepository<ServiceDetail, Long> {
}
