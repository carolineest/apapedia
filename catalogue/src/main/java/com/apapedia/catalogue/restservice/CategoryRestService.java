package com.apapedia.catalogue.restservice;

import java.util.List;
import java.util.UUID;

import com.apapedia.catalogue.model.Category;

public interface CategoryRestService {
    List<Category> getAllCategory();
    Category getCategoryById(UUID id);
}
