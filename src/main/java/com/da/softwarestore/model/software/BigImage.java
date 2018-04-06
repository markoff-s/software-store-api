package com.da.softwarestore.model.software;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class BigImage extends Blob {

    private BigImage() {}

    public BigImage(byte[] image, Application application) {
        this.bytes = image;
        this.application = application;
    }
}
