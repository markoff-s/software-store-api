package com.da.softwarestore.configuration;

import com.da.softwarestore.model.software.*;
import com.da.softwarestore.model.security.Authority;
import com.da.softwarestore.model.security.AuthorityName;
import com.da.softwarestore.model.security.User;
import com.da.softwarestore.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final int APPLICATIONS_PER_CATEGORY = 5;

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ApplicationRepository applicationRepository;
    private BigImageRepository bigImageRepository;
    private ArchiveRepository archiveRepository;

    @Autowired
    public DatabaseLoader(AuthorityRepository authorityRepository,
                          UserRepository userRepository,
                          CategoryRepository categoryRepository,
                          ApplicationRepository applicationRepository,
                          BigImageRepository bigImageRepository,
                          ArchiveRepository archiveRepository) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.applicationRepository = applicationRepository;
        this.bigImageRepository = bigImageRepository;
        this.archiveRepository = archiveRepository;
    }

    @Override
    public void run(String... strings) {
        List<Authority> authorities = prepareAuthorities();
        this.authorityRepository.saveAll(authorities);

        List<User> users = prepareUsers(authorities);
        this.userRepository.saveAll(users);

        List<Category> categories = prepareSoftwareCategories();
        this.categoryRepository.saveAll(categories);

        List<Application> applications = prepareApplications(categories, users.get(0));
        this.applicationRepository.saveAll(applications);

        List<BigImage> bigImages = prepareBigImages(applications);
        bigImageRepository.saveAll(bigImages);

        List<Archive> archives = prepareArchives(applications);
        archiveRepository.saveAll(archives);
    }

    private List<Authority> prepareAuthorities() {
        return Arrays.asList(new Authority[]{
                new Authority(AuthorityName.ROLE_USER),
                new Authority(AuthorityName.ROLE_ADMIN),
        });
    }

    private List<User> prepareUsers(List<Authority> authorities) {
        Authority userAuthority = authorities.stream()
                .filter(auth -> auth.getName().equals(AuthorityName.ROLE_USER))
                .findFirst()
                .get();
        User user = new User("user", "user", "John", "User", userAuthority);

        Authority adminAuthority = authorities.stream()
                .filter(auth -> auth.getName().equals(AuthorityName.ROLE_ADMIN))
                .findFirst()
                .get();
        User admin = new User("admin", "admin", "Jane", "Admin", adminAuthority, userAuthority);

        return Arrays.asList(new User[]{
                user,
                admin
        });
    }

    private List<Category> prepareSoftwareCategories() {
        return Arrays.asList(new Category[]{
                new Category("Games"),
                new Category("Multimedia"),
                new Category("Productivity"),
                new Category("Tools"),
                new Category("Health"),
                new Category("Lifestyle"),
                new Category("Cooking"),
                new Category("Auto")});
    }

    private List<BigImage> prepareBigImages(List<Application> applications) {
        byte[][] images = new byte[][]{getResourceAsByteArray("big.png"),
                getResourceAsByteArray("big2.png")};

        List<BigImage> bigImages = new ArrayList<>(applications.size());
        for (int i = 0; i < applications.size(); i++) {
            Application app = applications.get(i);
            bigImages.add(new BigImage(images[i % 2], app));
        }

        return bigImages;
    }

    private List<Archive> prepareArchives(List<Application> applications) {
        byte[] archive = getResourceAsByteArray("valid.zip");
        List<Archive> archives = new ArrayList<>(applications.size());
        for (Application app : applications) {
            archives.add(new Archive(archive, app));
        }

        return archives;
    }

    private List<Application> prepareApplications(List<Category> categories,
                                                  User user) {
        List<Application> applications = new ArrayList<>();
        byte[] smallImage = getResourceAsByteArray("small.png");
        byte[] smallImage2 = getResourceAsByteArray("small2.png");
        byte[][] images = new byte[][]{smallImage, smallImage2};

        int categoriesSize = categories.size();
        for (int i = 0; i < categoriesSize; i++) {
            Category category = categories.get(i);
            String categoryName = category.getName();

            int start = APPLICATIONS_PER_CATEGORY * i;
            int end = APPLICATIONS_PER_CATEGORY * (i + 1);
            for (int j = start; j < end; j++) {
                String index = "# " + (j + 1);
                Application application = new Application(
                        categoryName + " Application " + index,
                        categoryName + " Application Package " + index,
                        "Android is a mobile operating system developed by Google, based on a modified version of the Linux kernel and other open source software and designed primarily for touchscreen mobile devices such as smartphones and tablets.",
                        new SmallImage(images[(int) (j % 2)]),
                        category,
                        user);
                application.setNumberOfDownloads(j);

                applications.add(application);
            }
        }

        return applications;
    }

    private byte[] getResourceAsByteArray(String fileName) {
        byte[] bytes = new byte[0];

        try {
            //ClassPathResource classPathResource = new ClassPathResource("db/" + fileName);
            Path path = Paths.get(getClass().getClassLoader().getResource("db/" + fileName).toURI());
            bytes = Files.readAllBytes(path);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return bytes;
    }
}
