package com.da.softwarestore.model.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@ToString(exclude = "password")
public class User {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    @Size(min = 4, max = 50)
    private String userName;

    @Column(length = 100, nullable = false)
    @Size(min = 4, max = 100)
    @JsonIgnore
    private String password;

    @Column(length = 50, nullable = false)
    @Size(min = 4, max = 50)
    private String firstName;

    @Column(length = 50, nullable = false)
    @Size(min = 4, max = 50)
    private String lastName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")})
    private List<Authority> authorities;

    private User() {
    }

    public User(String userName, String password, String firstName, String lastName, Authority... authorities) {
        this.userName = userName;
        this.setPassword(password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.authorities = Arrays.asList(authorities);
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return userName;
    }

    public String getFirstname() {
        return firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }
}
