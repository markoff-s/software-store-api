package com.da.softwarestore.service;

import com.da.softwarestore.model.software.Archive;
import com.da.softwarestore.repository.ArchiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArchiveService {

    @Autowired
    private ArchiveRepository archiveRepository;

    public Archive getByApplicationId(Long applicationId) {
        return archiveRepository.findByApplicationId(applicationId);
    }
}
