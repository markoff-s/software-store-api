package com.da.softwarestore.model.software;

import com.da.softwarestore.model.security.User;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ApplicationUnitTests {

    private final String APPLICATION_NAME = "Application Name";
    private final String PACKAGE_NAME = "Package Name";
    private final String PACKAGE_DESCRIPTION = "Package Description";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @Mock
    SmallImage smallImage;

    @Mock
    public Category category;

    @Mock
    User user;

    @Test
    public void createWhenNameIsNullShouldThrowException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Name must not be empty");
        new Application(null, PACKAGE_NAME, PACKAGE_DESCRIPTION, smallImage, category, user);
    }

    @Test
    public void createWhenNameIsEmptyShouldThrowException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Name must not be empty");
        new Application("", PACKAGE_NAME, PACKAGE_DESCRIPTION, smallImage, category, user);
    }

    @Test
    public void createWhenPackageNameIsNullShouldThrowException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Package name must not be empty");
        new Application(APPLICATION_NAME, null, PACKAGE_DESCRIPTION, smallImage, category, user);
    }

    @Test
    public void createWhenPackageNameIsEmptyShouldThrowException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Package name must not be empty");
        new Application(APPLICATION_NAME, "", PACKAGE_DESCRIPTION, smallImage, category, user);
    }

    @Test
    public void createWhenSmallImageIsNullShouldThrowException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Small image must not be null");
        new Application(APPLICATION_NAME, PACKAGE_NAME, PACKAGE_DESCRIPTION, null, category, user);
    }

    @Test
    public void createWhenCategoryIsNullShouldThrowException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Category must not be null");
        new Application(APPLICATION_NAME, PACKAGE_NAME, PACKAGE_DESCRIPTION, smallImage, null, user);
    }

    @Test
    public void createWhenUserIsNullShouldThrowException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("User must not be null");
        new Application(APPLICATION_NAME, PACKAGE_NAME, PACKAGE_DESCRIPTION, smallImage, category, null);
    }
}