package com.da.softwarestore.util;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class ResourceManagerUnitTests {

    ResourceManager resourceManager = new ResourceManager();

    @Test
    @Parameters({"small.png", "big.png"})
    public void shouldGetImageForExistingImageResource(String fileName) {
        byte[] bytes = resourceManager.getImage(Paths.get("archives", fileName).toString());

        assertThat(bytes).isNotEmpty();
    }

    @Test
    @Parameters({"no-such-image.png"})
    public void shouldReturnNullForNonExistingImageResource(String fileName) {
        byte[] bytes = resourceManager.getImage(Paths.get("archives", fileName).toString());

        assertThat(bytes).isNull();
    }
}