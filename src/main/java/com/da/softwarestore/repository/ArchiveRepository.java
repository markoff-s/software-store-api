package com.da.softwarestore.repository;

import com.da.softwarestore.model.software.Archive;
import com.da.softwarestore.model.software.BigImage;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

@CacheConfig(cacheNames = {"archives"})
public interface ArchiveRepository extends JpaRepository<Archive, Long> {

    @Cacheable
    Archive findByApplicationId(Long applicationId);
}
