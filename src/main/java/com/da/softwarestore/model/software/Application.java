package com.da.softwarestore.model.software;

import com.da.softwarestore.model.security.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Application extends BaseEntity {

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private String packageName;

    @Column(columnDefinition = "CLOB")
    @Lob
    private String description;

    @Embedded
    private SmallImage smallImage;

    @Column
    private LocalDateTime dateCreated;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID")
    @JsonIgnore
    private User createdBy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CATEGORY_ID")
    @JsonIgnore
    private Category category;

    @Column(nullable = false)
    private int numberOfDownloads;

    private Application() {
    }

    public Application(String name,
                       String packageName,
                       String description,
                       SmallImage smallImage,
                       Category category,
                       User createdBy) {
        Assert.hasLength(name, "Name must not be empty");
        Assert.hasLength(packageName, "Package name must not be empty");
        Assert.notNull(smallImage, "Small image must not be null");
        Assert.notNull(category, "Category must not be null");
        Assert.notNull(createdBy, "User must not be null");

        this.name = name;
        this.packageName = packageName;
        this.description = description;
        this.smallImage = smallImage;
        this.category = category;
        this.numberOfDownloads = 0;
        this.dateCreated = LocalDateTime.now();
        this.createdBy = createdBy;
    }

    public Long getId() {
        return super.getId();
    }

    public void setNumberOfDownloads(int numberOfDownloads) {
        this.numberOfDownloads = numberOfDownloads;
    }
}
