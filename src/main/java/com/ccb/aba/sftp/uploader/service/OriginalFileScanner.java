package com.ccb.aba.sftp.uploader.service;

import com.ccb.aba.sftp.uploader.utils.ConfigLoader;
import com.ccb.aba.sftp.uploader.utils.DateUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class OriginalFileScanner {
    public List<File> findAbaFilesWithinRange(LocalDate startDate, LocalDate endDate) {
        ZonedDateTime start = DateUtil.startBoundary(startDate);
        ZonedDateTime end = DateUtil.endBoundary(endDate);
        if (end.toInstant().isBefore(start.toInstant())) {
            throw new IllegalArgumentException("ENDDATE cannot be before STARTDATE.");
        }

        String sageDir = ConfigLoader.getInstance().getSageDir();
        Path root = Paths.get(sageDir);
        if (!Files.isDirectory(root)) {
            throw new RuntimeException("SAGE directory not found or not a directory: " + sageDir);
        }

        List<File> result = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(root)) {
            for (Path p : stream) {
                if (!Files.isRegularFile(p, LinkOption.NOFOLLOW_LINKS)) {
                    continue;
                }
                String name = p.getFileName().toString();
                if (!name.toLowerCase().endsWith(".aba")) {
                    continue;
                }


                BasicFileAttributes attr = Files.readAttributes(p, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                Instant mtime = attr.lastModifiedTime().toInstant();
                if (DateUtil.isInDateRange(mtime, start, end)) {
                    result.add(p.toFile());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to scan SAGE directory: " + e.getMessage(), e);
        }

        return result;
    }
}
