package com.da.softwarestore.common.archive;

import com.da.softwarestore.common.exception.ArgumentValidationException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class ApplicationArchiveReader {

    private static final int BUFFER_SIZE = 8192;

    public List<ArchiveEntry> read(byte[] archive) {
        if (archive.length == 0)
            throw new ArgumentValidationException("archive", "Archive length can not be zero!");

        List<ArchiveEntry> archiveEntries = new ArrayList<>();
        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(archive))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                archiveEntries.add(getEntry(entry, zis));
            }

        } catch (IOException e) {
            throw new ArgumentValidationException("archive", e);
        }

        return archiveEntries;
    }

    private ArchiveEntry getEntry(ZipEntry entry, ZipInputStream zis) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream((int) entry.getSize())) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int readBytes;
            while ((readBytes = zis.read(buffer, 0, buffer.length)) >= 0) {
                out.write(buffer, 0, readBytes);
            }

            return new ArchiveEntry(entry.getName(), out.toByteArray());
        }
    }
}
