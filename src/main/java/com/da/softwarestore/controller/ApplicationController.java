package com.da.softwarestore.controller;

import com.da.softwarestore.common.exception.ArgumentValidationException;
import com.da.softwarestore.common.web.ContentTypes;
import com.da.softwarestore.model.software.*;
import com.da.softwarestore.service.ApplicationService;
import com.da.softwarestore.service.ArchiveService;
import com.da.softwarestore.service.BigImageService;
import com.da.softwarestore.service.CategoryService;
import com.da.softwarestore.common.web.EndPoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping(EndPoints.API)
public class ApplicationController {

    @Value("${api.default-page}")
    private int defaultPage;

    @Value("${api.default-page-size}")
    private int defaultPageSize;

    @Autowired
    private ApplicationService softwareApplicationService;

    @Autowired
    private BigImageService bigImageService;

    @Autowired
    private ArchiveService archiveService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping(EndPoints.APPLICATIONS)
    Page<Application> getApplications(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "dir", required = false) String dir) {
        return softwareApplicationService.getAll(createPageRequest(page, size, sort, dir));
    }

    @GetMapping(EndPoints.APPLICATIONS + "/{appId}")
    Application getApplicationById(@PathVariable Long appId) {
        return softwareApplicationService.GetById(appId);
    }

    @GetMapping(EndPoints.CATEGORIES + "/{categoryId}" + EndPoints.APPLICATIONS)
    Page<Application> getApplicationsByCategoryId(@PathVariable Long categoryId,
                                                  @RequestParam(value = "page", required = false) Integer page,
                                                  @RequestParam(value = "size", required = false) Integer size,
                                                  @RequestParam(value = "sort", required = false) String sort,
                                                  @RequestParam(value = "dir", required = false) String dir) {
        return softwareApplicationService.getByCategoryId(categoryId, createPageRequest(page, size, sort, dir));
    }

    @PostMapping(EndPoints.APPLICATIONS)
    ResponseEntity<?> createApplication(@RequestParam("categoryId") long categoryId,
                                        @RequestParam(value = "description", required = false) String description,
                                        @RequestParam("file") MultipartFile file) {

        Category category = categoryService.getById(categoryId);
        if (category == null)
            throw new ArgumentValidationException("category");

        if (file == null)
            throw new ArgumentValidationException("file");

        byte[] archive;
        try {
            archive = file.getBytes();
            if (archive.length == 0)
                throw new ArgumentValidationException("file");
        } catch (IOException e) {
            throw new ArgumentValidationException("file", e);
        }

        ApplicationCreationRequest request = ApplicationCreationRequest.of(category, description, archive);
        Application app = softwareApplicationService.createApplication(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/applications/" + app.getId());
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.LOCATION);
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(EndPoints.APPLICATIONS + "/{appId}" + EndPoints.BIG_IMAGE)
    ResponseEntity<byte[]> getBigImageByApplicationId(@PathVariable Long appId) {
        BigImage bigImage = bigImageService.getByApplicationId(appId);
        if (bigImage != null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, ContentTypes.IMAGE_PNG);
            return new ResponseEntity<>(bigImage.getBytes(), httpHeaders, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(EndPoints.APPLICATIONS + "/{appId}" + EndPoints.ARCHIVE)
    ResponseEntity<byte[]> getArchiveByApplicationId(@PathVariable Long appId) {
        Archive archive = archiveService.getByApplicationId(appId);
        if (archive != null) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, ContentTypes.APPLICAION_ZIP);
            return new ResponseEntity<>(archive.getBytes(), httpHeaders, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private Pageable createPageRequest(Integer page, Integer size, String sort, String dir) {
        if (page == null)
            page = Integer.valueOf(defaultPage);

        if (size == null)
            size = Integer.valueOf(defaultPageSize);

        if (!StringUtils.isEmpty(sort)) {
            return PageRequest.of(
                    page,
                    size,
                    !StringUtils.isEmpty(dir) && dir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                    sort);
        }

        return PageRequest.of(page, size);
    }
}
