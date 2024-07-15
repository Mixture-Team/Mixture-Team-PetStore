package hutech.mixture.petstore.services;

import hutech.mixture.petstore.models.CategoryParent;
import hutech.mixture.petstore.repositories.CategoryParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryParentService {
    @Autowired
    private CategoryParentRepository categoryParentRepository;

    public List<CategoryParent> getAllCategoryParents() {
        return categoryParentRepository.findAll();
    }

}
