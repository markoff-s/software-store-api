package com.da.softwarestore.model.software;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
@Data
public class SmallImage {

    @Column(columnDefinition = "BLOB")
    @Lob
    private byte[] image;

    private SmallImage() {}

    public SmallImage(byte[] image) {
        this.image = image;
    }
}
