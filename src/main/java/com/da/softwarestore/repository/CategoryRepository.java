package com.da.softwarestore.repository;

import com.da.softwarestore.model.software.Category;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@CacheConfig(cacheNames = {"categories"})
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Override
    @Cacheable(key = "{ #root.methodName, #sort}")
    List<Category> findAll(Sort sort);
}
