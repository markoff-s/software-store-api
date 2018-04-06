package com.da.softwarestore.controller;

import com.da.softwarestore.model.software.Category;
import com.da.softwarestore.service.CategoryService;
import com.da.softwarestore.common.web.EndPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(EndPoints.API)
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping(EndPoints.CATEGORIES)
    List<Category> getAllCategories() {
        return categoryService.getAll();
    }
}
