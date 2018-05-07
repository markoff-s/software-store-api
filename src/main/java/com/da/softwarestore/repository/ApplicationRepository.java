package com.da.softwarestore.repository;

import com.da.softwarestore.model.software.Application;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = {"applications"})
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Override
    @Cacheable
    @Query(value = "SELECT app FROM Application app " +
            "JOIN FETCH app.category " +
            "JOIN FETCH app.createdBy ",
            countQuery = "SELECT COUNT(app) FROM Application app")
    Page<Application> findAll(Pageable pageable);

    @Override
    @Cacheable
    Optional<Application> findById(Long id);

    @Cacheable
    @Query(value = "SELECT app FROM Application app " +
            "JOIN FETCH app.category " +
            "JOIN FETCH app.createdBy " +
            "WHERE app.category.id = :categoryId",
            countQuery = "SELECT COUNT(app) FROM Application app WHERE app.category.id = :categoryId")
    Page<Application> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Override
    @CacheEvict(allEntries=true)
    Application save(Application app);
}
