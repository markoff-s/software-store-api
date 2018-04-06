package com.da.softwarestore.service;

import com.da.softwarestore.model.software.Category;
import com.da.softwarestore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAll() {
        return categoryRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
