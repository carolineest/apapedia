package com.apapedia.catalogue.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apapedia.catalogue.model.Category;
import com.apapedia.catalogue.restservice.CategoryRestService;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryRestController {
    @Autowired
    private CategoryRestService categoryRestService;

    // C10
    @GetMapping("/view-all")
    public List<Category> retrieveAllCategory(){
        return categoryRestService.getAllCategory();
    }
}
