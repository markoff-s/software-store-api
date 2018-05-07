package com.da.softwarestore.model.software;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
@Data
public class Blob extends BaseEntity {

    @Column(columnDefinition = "BLOB", nullable = false)
    @Lob
    protected byte[] bytes;

    @OneToOne(optional = false)
    protected Application application;

    public byte[] getBytes() {
        return bytes;
    }
}
