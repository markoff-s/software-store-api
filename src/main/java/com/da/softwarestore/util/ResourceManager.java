package com.da.softwarestore.util;

import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ResourceManager {
    public byte[] getImage(String relativePath) {
        try {
            Path path = Paths.get(getClass().getClassLoader().getResource(relativePath).toURI());
            return Files.readAllBytes(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
