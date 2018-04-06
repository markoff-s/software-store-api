package com.da.softwarestore.model.software;

public class ApplicationCreationRequest {
    private Category category;
    private String description;
    private byte[] archive;

    protected ApplicationCreationRequest(Category category, String description, byte[] archive) {
        this.category = category;
        this.description = description;
        this.archive = archive;
    }

    public static ApplicationCreationRequest of(Category category, String description, byte[] archive) {
        return new ApplicationCreationRequest(category, description, archive);
    }

    public Category getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getArchive() {
        return archive;
    }
}
