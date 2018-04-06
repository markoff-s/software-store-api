package com.da.softwarestore.model.software;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class Archive extends Blob {

    private Archive() {}

    public Archive(byte[] bytes, Application application) {
        this.bytes = bytes;
        this.application = application;
    }
}
