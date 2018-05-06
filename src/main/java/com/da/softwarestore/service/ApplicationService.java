package com.da.softwarestore.service;

import com.da.softwarestore.common.archive.ApplicationArchiveReader;
import com.da.softwarestore.common.archive.ArchiveEntry;
import com.da.softwarestore.common.exception.ArgumentValidationException;
import com.da.softwarestore.common.exception.InvalidArchiveException;
import com.da.softwarestore.security.AuthenticationFacade;
import com.da.softwarestore.model.security.User;
import com.da.softwarestore.model.software.*;
import com.da.softwarestore.repository.ApplicationRepository;
import com.da.softwarestore.repository.ArchiveRepository;
import com.da.softwarestore.repository.BigImageRepository;
import com.da.softwarestore.repository.UserRepository;
import com.da.softwarestore.util.ResourceManager;
import org.apache.el.parser.SimpleCharStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private static final String DESCRIPTION_FILE_NOT_FOUND = "Description file not found!";
    private static final String INVALID_DESCRIPTION_FILE_FORMAT = "Invalid description file format!";

    @Value("${archive.description-file.name}")
    private String descriptionFileName;

    @Value("${archive.description-file.application-name-param-name}")
    private String descriptionFileApplicationNameParamName;

    @Value("${archive.description-file.package-param-name}")
    private String descriptionFilePackageParamName;

    @Value("${archive.description-file.small-image-param-name}")
    private String descriptionFileSmallImageParamName;

    @Value("${archive.description-file.big-image-param-name}")
    private String descriptionFileBigImageParamName;

    @Value("${archive.description-file.param-value-separator}")
    private String descriptionFileParamValueSeparator;

    @Value("${application.default-small-image-relative-path}")
    private String defaultSmallImageRelativePath;

    @Value("${application.default-big-image-relative-path}")
    private String defaultBigImageRelativePath;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BigImageRepository bigImageRepository;

    /*@Autowired
    private AuthenticationFacade authenticationFacade;*/

    @Autowired
    private ApplicationArchiveReader applicationArchiveReader;

    @Autowired
    private ArchiveRepository archiveRepository;

    @Autowired
    private ResourceManager resourceManager;

    public Page<Application> getAll(Pageable pageable) {
        return applicationRepository.findAll(pageable);
    }

    public Application GetById(Long id) {
        return applicationRepository.findById(id).orElse(null);
    }

    public Page<Application> getByCategoryId(Long categoryId, Pageable pageable) {
        return applicationRepository.findByCategoryId(categoryId, pageable);
    }

    @Transactional
    // don't forget about cache eviction!!!
    public Application createApplication(ApplicationCreationRequest request) {
        validateRequest(request);

        List<ArchiveEntry> archiveEntries = applicationArchiveReader.read(request.getArchive());
        ArchiveEntry archiveDescriptionEntry = getEntryByFileName(archiveEntries, descriptionFileName);
        if (archiveDescriptionEntry == null)
            throw new InvalidArchiveException(DESCRIPTION_FILE_NOT_FOUND);

        ArchiveDescription archiveDescription = getArchiveDescription(archiveDescriptionEntry);
        if (StringUtils.isEmpty(archiveDescription.getApplicationName()) ||
                StringUtils.isEmpty(archiveDescription.getPackageName()))
            throw new InvalidArchiveException(INVALID_DESCRIPTION_FILE_FORMAT);

        ArchiveEntry smallImageEntry = getEntryByFileName(archiveEntries, archiveDescription.getSmallImageName());
        SmallImage smallImage = smallImageEntry != null
                ? new SmallImage(smallImageEntry.getBytes())
                : new SmallImage(resourceManager.getImage(defaultSmallImageRelativePath));

        Application application = new Application(archiveDescription.getApplicationName(),
                archiveDescription.getPackageName(),
                request.getDescription(),
                smallImage,
                request.getCategory(),
                getUser());
        applicationRepository.save(application);

        ArchiveEntry bigImageEntry = getEntryByFileName(archiveEntries, archiveDescription.getBigImageName());
        BigImage  bigImage = bigImageEntry != null
            ? new BigImage(bigImageEntry.getBytes(), application)
            : new BigImage(resourceManager.getImage(defaultBigImageRelativePath), application);
        bigImageRepository.save(bigImage);

        Archive archive = new Archive(request.getArchive(), application);
        archiveRepository.save(archive);

        return application;
    }

    private User getUser() {
//        return userRepository.findByUserName(authenticationFacade.getAuthentication().getName());
        return userRepository.findByUserName("user");
    }

    private ArchiveDescription getArchiveDescription(ArchiveEntry archiveDescriptionEntry) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(archiveDescriptionEntry.getBytes());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            List<String> descriptionLines = reader
                    .lines()
                    .collect(Collectors.toList());

            return ArchiveDescription.of(getDescriptionFileParamValue(descriptionLines, descriptionFileApplicationNameParamName),
                    getDescriptionFileParamValue(descriptionLines, descriptionFilePackageParamName),
                    getDescriptionFileParamValue(descriptionLines, descriptionFileSmallImageParamName),
                    getDescriptionFileParamValue(descriptionLines, descriptionFileBigImageParamName));

        } catch (IOException e) {
            throw new InvalidArchiveException(INVALID_DESCRIPTION_FILE_FORMAT, e);
        }
    }

    private String getDescriptionFileParamValue(List<String> descriptionLines, String paramName) {
        String paramValue = descriptionLines.stream()
                .filter(line -> line.startsWith(paramName))
                .findFirst()
                .orElse(null);

        if (!StringUtils.isEmpty(paramValue)) {
            int index = paramValue.indexOf(descriptionFileParamValueSeparator);
            if (index != -1)
                paramValue = paramValue.substring(index + 1).trim();
        }

        return paramValue;
    }

    private ArchiveEntry getEntryByFileName(List<ArchiveEntry> archiveEntries, String fileName) {
        return archiveEntries.stream()
                .filter(entry -> entry.getName().equalsIgnoreCase(fileName))
                .findFirst()
                .orElse(null);
    }

    private void validateRequest(ApplicationCreationRequest request) {
        Assert.notNull(request, "Request must not be null");
        Assert.notNull(request.getCategory(), "Category must not be null");
        Assert.notNull(request.getArchive(), "Archive must not be null");
    }
}
