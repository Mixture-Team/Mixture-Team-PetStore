package hutech.mixture.petstore.service;

import hutech.mixture.petstore.models.Product;
import hutech.mixture.petstore.models.Province;
import hutech.mixture.petstore.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProvinceService {
   private final ProvinceRepository provinceRepository;

    public List<Province> getAllProvinces() {
        return provinceRepository.findAll();
    }

    public Optional<Province> getProvinceById(Long id) {
        return provinceRepository.findById(id);
    }


    public Optional<Province> findById(Long id) {
        return provinceRepository.findById(id);
    }
}
