package com.apapedia.catalogue.restservice;

import java.util.List;
import java.util.UUID;

import com.apapedia.catalogue.model.Catalogue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.apapedia.catalogue.model.Category;
import com.apapedia.catalogue.repository.CategoryDb;

@Service
@Transactional
public class CategoryRestServiceImpl implements CategoryRestService {
    @Autowired
    private CategoryDb categoryDb;
    
    @Override
    public List<Category> getAllCategory() { return categoryDb.findAll(); }
    @Override
    public Category getCategoryById(UUID id){
        for (Category category : getAllCategory()){
            if(category.getId().equals(id)){
                return category;
            }
        }
        return null;
    }
}
