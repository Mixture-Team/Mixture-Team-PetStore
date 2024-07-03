package hutech.mixture.petstore.services;

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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private static String UPLOAD_DIR = "E:\\Dev\\Source\\Project\\Pet-Store\\src\\main\\resources\\static\\img\\";

    public Page<Product> getAllProductForAdmin(Pageable pageable){
        return productRepository.findAll(pageable);
    }
    // Lấy tất cả sản phẩm từ cơ sở dữ liệu
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAllByDeletedFalse(pageable);
    }

    public Page<Product> getProductByCategoryId(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    public Page<Product> getProductByCategoryParentId(Long categoryParentId, Pageable pageable) {
        return productRepository.findByCategoryParentId(categoryParentId, pageable);
    }

    public void addProduct(Product product) {
        product.setPromotionPrice(product.getPrice() - (product.getPrice() * product.getDiscount() / 100));
        productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Không tìm thấy sản phẩm có id " + id)
        );
    }

    public Product updateProduct(Product product, MultipartFile file) {
        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(
                () -> new RuntimeException("Không tìm thấy sản phẩm có id " + product.getId())
        );
        existingProduct.setName(product.getName());
        existingProduct.setNums(product.getNums());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setLink(product.getLink());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setPromotionPrice(product.getPrice() - (product.getPrice() * product.getDiscount() / 100));
        existingProduct.setCategory(product.getCategory());
        existingProduct.setDeleted(product.isDeleted());

        // Xử lý tệp hình ảnh
        if (!file.isEmpty()) {
            try {
                // Lưu tệp hình ảnh vào thư mục lưu trữ
                String fileName = file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.write(path, bytes);

                // Cập nhật đường dẫn hình ảnh trong sản phẩm
                existingProduct.setImg(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý ngoại lệ khi không thể lưu tệp hình ảnh
                // Thông báo cho người dùng hoặc quay lại trạng thái trước đó
            }
        }
        return productRepository.save(existingProduct);
    }
}
