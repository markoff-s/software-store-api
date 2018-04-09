package com.da.softwarestore.common.archive;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationArchiveReaderTest {

    private byte[] validArchive;

    @Before
    public void setUp() throws URISyntaxException, IOException {

        Path path = Paths.get(getClass().getClassLoader().getResource("archives/valid.zip").toURI());
        validArchive = Files.readAllBytes(path);
    }

    @Test
    public void shouldReadAllEntries() {
        ApplicationArchiveReader reader = new ApplicationArchiveReader();

        List<ArchiveEntry> archiveEntries = reader.read(validArchive);

        assertNotNull(archiveEntries);
        assertTrue(archiveEntries.size() == 3);
        for (ArchiveEntry entry : archiveEntries) {
            assertFalse(StringUtils.isEmpty(entry.getName()));
            assertTrue(entry.getBytes().length > 0);
        }

    }
}