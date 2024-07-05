package hutech.mixture.petstore.services;

import hutech.mixture.petstore.models.Category;
import hutech.mixture.petstore.models.CategoryParent;
import hutech.mixture.petstore.models.PriceRange;
import hutech.mixture.petstore.models.Product;
import hutech.mixture.petstore.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    // Lấy tất cả sản phẩm từ cơ sở dữ liệu
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> getProductByCategoryId(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public Page<Product> getProductByCategoryParentId(Long categoryParentId, Pageable pageable) {
        return productRepository.findByCategoryParentId(categoryParentId, pageable);
    }

    public Page<Product> searchByPriceAndCatoParent(Long categoryParentId, List<PriceRange> priceRanges, Pageable pageable) {
        Double minPrice = null;
        Double maxPrice = null;

        if (priceRanges != null && !priceRanges.isEmpty()) {
            minPrice = priceRanges.stream().mapToDouble(PriceRange::getMin).min().orElse(0);
            maxPrice = priceRanges.stream().mapToDouble(PriceRange::getMax).max().orElse(Double.MAX_VALUE);
        }

        return productRepository.searchByPriceAndCatoParent(categoryParentId, minPrice, maxPrice, pageable);
    }
    ////

    public Page<Product> searchProduct(String name, Pageable pageable) {
        return productRepository.search(name, pageable);
    }
    ///// search auto
    public List<Product> findProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }


}
