package hutech.mixture.petstore.Services;

import hutech.mixture.petstore.Repository.CustomServiceRepository;
import lombok.RequiredArgsConstructor;
import hutech.mixture.petstore.models.CustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@SessionScope
@RequiredArgsConstructor
@Transactional
public class CustomServiceService {
    @Autowired
    private CustomServiceRepository customServiceRepository;

    private List<CustomService> customServices = new ArrayList<>();
    public List<CustomService> findAll() {
        return customServiceRepository.findAll();
    }

    public CustomService findById(Long id) {
        Optional<CustomService> customService = customServiceRepository.findById(id);
        return customService.orElse(null);  // Or throw an exception if not found
    }
    public CustomService findByLink(String link) {
        return customServiceRepository.findByLink(link);
    }
    public List<CustomService> findAllActiveCustomServices() {
        return customServiceRepository.findAllActiveCustomServices();
    }


}
