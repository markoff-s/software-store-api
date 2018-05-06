package com.da.softwarestore.util;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class ResourceManagerUnitTests {

    @Test
    @Parameters({"small.png", "big.png"})
    public void shouldGetImageFromResources(String fileName) throws URISyntaxException, IOException {
        ResourceManager resourceManager = new ResourceManager();
        byte[] bytes = resourceManager.getImage(Paths.get("archives", fileName).toString());

        assertThat(bytes).isNotEmpty();
    }
}