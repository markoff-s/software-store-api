package com.da.softwarestore.model.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

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
        assertThat(json).hasJsonPathStringValue("@.username");
        assertThat(json).doesNotHaveJsonPathValue("@.password");
        assertThat(json).extractingJsonPathStringValue("@.username")
                .isEqualTo("johns");
    }

    private User getUser() {
        return new User("johns", "12345", "John", "Smith", new Authority(AuthorityName.ROLE_USER));
    }
}