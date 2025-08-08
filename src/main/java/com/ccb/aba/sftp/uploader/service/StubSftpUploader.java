package com.ccb.aba.sftp.uploader.service;

import java.io.File;

public class StubSftpUploader implements Uploader{
    @Override
    public boolean upload(File file) {
        System.out.println("[MOCK] Uploading file...");
        return true;
    }
}
