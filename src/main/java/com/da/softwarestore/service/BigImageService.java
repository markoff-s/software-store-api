package com.da.softwarestore.service;

import com.da.softwarestore.model.software.BigImage;
import com.da.softwarestore.repository.BigImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BigImageService {

    @Autowired
    private BigImageRepository bigImageRepository;

    public BigImage getByApplicationId(Long applicationId) {
        return bigImageRepository.findByApplicationId(applicationId);
    }
}
