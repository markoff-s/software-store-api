package com.da.softwarestore.model.security;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Authority {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 50, nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorityName name;

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.LAZY)
    private List<User> users;

    private Authority() {}

    public Authority(AuthorityName name) {
        this.name = name;
    }

    public AuthorityName getName() {
        return name;
    }
}