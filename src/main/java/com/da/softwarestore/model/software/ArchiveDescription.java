package com.da.softwarestore.model.software;

public class ArchiveDescription {
    private String applicationName;
    private String packageName;
    private String smallImageName;
    private String bigImageName;

    protected ArchiveDescription(String applicationName, String packageName, String smallImageName, String bigImageName) {

        this.applicationName = applicationName;
        this.packageName = packageName;
        this.smallImageName = smallImageName;
        this.bigImageName = bigImageName;
    }

    public static ArchiveDescription of(String applicationName, String packageName, String smallImageName, String bigImageName) {
        return new ArchiveDescription(applicationName, packageName, smallImageName, bigImageName);
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSmallImageName() {
        return smallImageName;
    }

    public String getBigImageName() {
        return bigImageName;
    }
}
