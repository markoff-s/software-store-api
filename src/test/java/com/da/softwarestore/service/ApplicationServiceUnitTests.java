package com.da.softwarestore.service;

import com.da.softwarestore.model.software.ApplicationCreationRequest;
import com.da.softwarestore.model.software.Category;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ApplicationServiceUnitTests {

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    ApplicationService applicationService;

    @Before
    public void setUp() {
        applicationService = new ApplicationService();
    }

    @Test
    public void createApplicationShouldThrowExceptionWhenRequestIsNull() {
        thrown.expect(IllegalArgumentException.class);
        ApplicationCreationRequest request = null;

        applicationService.createApplication(request);
    }

    @Test
    public void createApplicationShouldThrowExceptionWhenCategoryIsNull() {
        thrown.expect(IllegalArgumentException.class);
        ApplicationCreationRequest request =
                ApplicationCreationRequest.of(null, "Description", new byte[]{});

        applicationService.createApplication(request);
    }

    @Test
    public void createApplicationShouldThrowExceptionWhenArchiveIsNull() {
        thrown.expect(IllegalArgumentException.class);
        ApplicationCreationRequest request =
                ApplicationCreationRequest.of(new Category("xz"), "Description", null);

        applicationService.createApplication(request);
    }
}