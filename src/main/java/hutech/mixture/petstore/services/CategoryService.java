package hutech.mixture.petstore.services;

import hutech.mixture.petstore.models.Category;
import hutech.mixture.petstore.models.CategoryParent;
import hutech.mixture.petstore.repository.CategoryParentRepository;
import hutech.mixture.petstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategoryById(Long id){
        return categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Không tìm thấy category có id " + id)
        );
    }
    public Page<Category> getAllCategoriesForAdmin(Pageable pageable){
        return categoryRepository.findAll(pageable);
    }
    public List<Category> getAllCategories(){
        return categoryRepository.findAllByDeletedFalse();
    }
    public List<Category> getCategoryParentById(long parentId){
        return categoryRepository.findByParentId(parentId);
    }

    public void addProduct(Category category) {
        category.setDeleted(false);
        categoryRepository.save(category);
    }

    public void updateCategory(Category category) {
        Category existingCategory = categoryRepository.findById(category.getId()).orElseThrow(
                () -> new RuntimeException("Không tìm thấy category có id " + category.getId())
        );
        existingCategory.setName(category.getName());
        existingCategory.setLink(category.getLink());
        existingCategory.setParent(category.getParent());
        existingCategory.setDeleted(category.isDeleted());
        categoryRepository.save(existingCategory);
    }

}