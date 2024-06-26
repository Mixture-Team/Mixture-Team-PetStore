package hutech.mixture.petstore.services;

import hutech.mixture.petstore.models.Category;
import hutech.mixture.petstore.models.CategoryParent;
import hutech.mixture.petstore.repository.CategoryParentRepository;
import hutech.mixture.petstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategory(Long id){
        return categoryRepository.findById(id).orElse(null);
    }
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
    public List<Category> getCategoryParentById(long parentId){
        return categoryRepository.findByParentId(parentId);
    }
}