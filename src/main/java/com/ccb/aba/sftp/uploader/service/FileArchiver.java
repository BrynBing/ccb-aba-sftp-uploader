package com.ccb.aba.sftp.uploader.service;

import com.ccb.aba.sftp.uploader.utils.ConfigLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.*;

public class FileArchiver {
    public void archive(File file) {
        if (file == null || !file.isFile()) {
            throw new IllegalArgumentException("Invalid file to archive: " + file);
        }

        Path source = file.toPath();
        Path processedDir = Paths.get(ConfigLoader.getInstance().getProcessedDir());
        Path target = processedDir.resolve(file.getName());
        Path tempTarget = processedDir.resolve(file.getName() + ".part");

        try {
            Files.copy(source, tempTarget, REPLACE_EXISTING, COPY_ATTRIBUTES);

            try {
                Files.move(tempTarget, target, ATOMIC_MOVE, REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(tempTarget, target, REPLACE_EXISTING);
            }

        } catch (IOException e) {
            try { Files.deleteIfExists(tempTarget); } catch (IOException ignore) {}
            throw new RuntimeException("Archive (copy) failed for " + file.getName() + ": " + e.getMessage(), e);
        }
    }
}
