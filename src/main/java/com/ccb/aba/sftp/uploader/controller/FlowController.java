package com.ccb.aba.sftp.uploader.controller;

import com.ccb.aba.sftp.uploader.service.*;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class FlowController {
    private final OriginalFileScanner originalFileScanner;
    private final FileFilter fileFilter;
    private final SftpUploader sftpUploader;
    private final FileArchiver fileArchiver;

    public FlowController() {
        this.originalFileScanner = new OriginalFileScanner();
        this.fileFilter = new FileFilter();
        this.sftpUploader = new SftpUploader();
        this.fileArchiver = new FileArchiver();
    }

    public void execute(LocalDate startDate, LocalDate endDate) {
        System.out.println("[INFO] Transfer process started.");

        // 1. Scan .aba files in date range
        List<File> allCandidates = originalFileScanner.findAbaFilesWithinRange(startDate, endDate);
        if (allCandidates.isEmpty()) {
            System.out.println("[INFO] No ABA files found in time range.");
            return;
        }

        System.out.println("[INFO] The following ABA files were found in the specified time range:");
        for (File file : allCandidates) {
            System.out.println("    - " + file.getName());
        }

        // 2. Filter the files already exist in OUT\Processed\
        List<File> filesToUpload = fileFilter.filterUnprocessedFiles(allCandidates);
        if (filesToUpload.isEmpty()) {
            System.out.println("[INFO] No ABA files need to be processed.");
            return;
        }

        System.out.println("[INFO] ABA files to upload:");
        for (File file : filesToUpload) {
            System.out.println("    - " + file.getName());
        }

//        int success = 0;
//        int failed = 0;
//
//        // 3. Upload + Archive
//        for (File file : filesToUpload) {
//            System.out.println("[INFO] Uploading: " + file.getName());
//
//            boolean uploaded = sftpUploader.upload(file);
//            if (uploaded) {
//                fileArchiver.archive(file);
//                System.out.println("[INFO] Uploaded and archived: " + file.getName());
//                success++;
//            } else {
//                System.err.println("[ERROR] Upload failed: " + file.getName());
//                failed++;
//            }
//        }
        for (File file : filesToUpload) {
            System.out.println("[INFO] Uploading: " + file.getName());
            fileArchiver.archive(file);
            System.out.println("[INFO] Uploaded and archived: " + file.getName());
        }
//
//        // 4. Print final message
//        System.out.println("[INFO] Transfer complete. Success: " + success + ", Failed: " + failed);

    }
}
