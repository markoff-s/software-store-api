package com.da.softwarestore.repository;

import com.da.softwarestore.model.software.BigImage;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

@CacheConfig(cacheNames = {"bigImages"})
public interface BigImageRepository extends JpaRepository<BigImage, Long> {

    @Cacheable
    BigImage findByApplicationId(Long applicationId);
}
