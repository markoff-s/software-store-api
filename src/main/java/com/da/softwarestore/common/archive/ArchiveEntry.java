package com.da.softwarestore.common.archive;

public class ArchiveEntry {
    private String name;
    private byte[] bytes;

    public ArchiveEntry(String name, byte[] bytes) {
        this.name = name;
        this.bytes = bytes;
    }

    public String getName() {
        return name;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
