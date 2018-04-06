package com.da.softwarestore.model.software;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Category extends BaseEntity {

    @Column(length = 50, unique = true, nullable = false)
    private String name;

    private Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
