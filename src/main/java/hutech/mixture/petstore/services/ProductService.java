package hutech.mixture.petstore.service;

import hutech.mixture.petstore.models.Category;
import hutech.mixture.petstore.models.CategoryParent;
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
}
