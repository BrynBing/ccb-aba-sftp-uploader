package com.ccb.aba.sftp.uploader.service;

import java.io.File;
import java.io.IOException;

public interface Uploader {
    boolean upload(File file) throws IOException;
}
