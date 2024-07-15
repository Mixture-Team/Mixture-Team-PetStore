package hutech.mixture.petstore.services;

import hutech.mixture.petstore.models.PriceRange;
import hutech.mixture.petstore.models.Product;
import hutech.mixture.petstore.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
//    private static String UPLOAD_DIR = "E:\\Dev\\Source\\Project\\Pet-Store\\src\\main\\resources\\static\\uploads\\";
    private static String UPLOAD_DIR = "src/main/resources/static/img/";

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

    public Page<Product> searchProductForAdmin(String name, Pageable pageable) {
        return productRepository.searchForAdmin(name, pageable);
    }
    ///// search auto
    public List<Product> findProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }



    public void addProduct(Product product, MultipartFile file) {
        if(!file.isEmpty()){
            String fileName = saveImage(file);
            product.setImg(fileName);
        }
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

        if(!file.isEmpty()){
            String fileName = saveImage(file);
            existingProduct.setImg(fileName);
        }
        return productRepository.save(existingProduct);
    }
    private String saveImage(MultipartFile file) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Không thể lưu file hình ảnh", e);
        }
    }
}
