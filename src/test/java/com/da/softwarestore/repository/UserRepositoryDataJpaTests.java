package com.da.softwarestore.repository;

import com.da.softwarestore.model.security.Authority;
import com.da.softwarestore.model.security.AuthorityName;
import com.da.softwarestore.model.security.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryDataJpaTests {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Test
    public void saveShouldSaveUser() {
        User user = getUser();

        User savedUser = userRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);
        assertThat(savedUser.getUserName()).isEqualTo("johns");
        assertThat(savedUser.getFirstName()).isEqualTo("John");
        assertThat(savedUser.getLastName()).isEqualTo("Smith");
        assertThat(savedUser.getPassword()).isNotNull();
        assertThat(savedUser.getAuthorities().size()).isEqualTo(1);
        assertThat(savedUser.getAuthorities().get(0).getName()).isEqualTo(AuthorityName.ROLE_USER);
    }

    @Test
    public void findByUserNameShouldReturnUser() {
        User user = getUser();
        entityManager.persist(user.getAuthorities().get(0));
        entityManager.persist(user);

        User savedUser = userRepository.findByUserName(user.getUserName());

        assertThat(savedUser).isNotNull();
    }

    private User getUser() {
        return new User("johns", "12345", "John", "Smith", new Authority(AuthorityName.ROLE_USER));
    }
}