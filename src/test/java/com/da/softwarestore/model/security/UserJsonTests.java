package com.da.softwarestore.model.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@JsonTest
public class UserJsonTests {

    @Autowired
    private JacksonTester<User> jacksonTester;

    @Test
    public void testSerialize() throws IOException {
        User user = getUser();

        JsonContent<User> json = jacksonTester.write(user);

        assertThat(json).isEqualTo("user.json");
        assertThat(json).hasJsonPathStringValue("@.userName");
        assertThat(json).extractingJsonPathStringValue("@.userName")
                .isEqualTo("johns");
        assertThat(json).doesNotHaveJsonPathValue("@.password");
    }

    @Test
    public void testDeserialize() throws IOException, URISyntaxException {
        Path path = Paths.get(getClass().getResource("user.json").toURI());
        byte[] bytes = Files.readAllBytes(path);
        String json = new String(bytes);
        User user = getUser();

        User parsedUser = jacksonTester.parseObject(json);

        assertThat(parsedUser.getUserName()).isEqualTo(user.getUserName());
        assertThat(parsedUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(parsedUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(parsedUser.getAuthorities().size()).isEqualTo(user.getAuthorities().size());
    }

    private User getUser() {
        return new User("johns", "12345", "John", "Smith", new Authority(AuthorityName.ROLE_USER));
    }
}