package hutech.mixture.petstore.Services;

import hutech.mixture.petstore.repositories.ServiceDetailRepository;
import hutech.mixture.petstore.models.ServiceDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;
import java.util.Optional;

@Service
@SessionScope
@RequiredArgsConstructor
@Transactional
public class ServiceDetailService {
    @Autowired
    private ServiceDetailRepository serviceDetailRepository;

    private List<ServiceDetail> serviceDetails;
    public List<ServiceDetail> findAll() {
        return serviceDetailRepository.findAll();
    }

    public ServiceDetail findById(Long id) {
        Optional<ServiceDetail> customService = serviceDetailRepository.findById(id);
        return customService.orElse(null);
    }
}
