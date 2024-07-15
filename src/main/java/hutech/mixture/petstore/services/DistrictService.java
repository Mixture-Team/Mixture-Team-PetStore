package hutech.mixture.petstore.services;

import hutech.mixture.petstore.models.District;

import hutech.mixture.petstore.repositories.DistrictRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DistrictService {
    private final DistrictRepository districtRepository;


    // Method to fetch all districts
    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }

    public Optional<District> findById(Long id) {
        return districtRepository.findById(id);
    }
    public double getShippingFeeByDistrictId(Long districtId) {
        Optional<District> districtOptional = districtRepository.findById(districtId);
        if (districtOptional.isPresent()) {
            return districtOptional.get().getFee();
        } else {
            throw new IllegalArgumentException("Province not found with id: " + districtId);
        }
    }

    public List<District> getDistrictsByProvinceId(Long provinceId) {
        return districtRepository.findByProvinceId(provinceId);
    }
}
