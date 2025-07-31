package com.ccb.aba.sftp.uploader.service;

import com.ccb.aba.sftp.uploader.utils.ConfigLoader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileFilter {
    public List<File> filterUnprocessedFiles(List<File> candidates) {
        String processedDir = ConfigLoader.getInstance().getProcessedDir();
        Path processed = Paths.get(processedDir);

        List<File> out = new ArrayList<>();
        for (File f : candidates) {
            Path processedPath = processed.resolve(f.getName());
            if (!Files.exists(processedPath)) {
                out.add(f);
            }
        }
        return out;
    }
}
