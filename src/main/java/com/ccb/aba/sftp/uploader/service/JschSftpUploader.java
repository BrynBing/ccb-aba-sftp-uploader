package com.ccb.aba.sftp.uploader.service;

import com.ccb.aba.sftp.uploader.utils.ConfigLoader;
import com.jcraft.jsch.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class JschSftpUploader implements Uploader {

    private final ConfigLoader config = ConfigLoader.getInstance();
    private Session session;
    private ChannelSftp sftp;

    private void ensureConnected() throws IOException {
        if (sftp != null && sftp.isConnected()) return;

        String host = config.getSftpHost();
        int port = config.getSftpPort();
        String username = config.getSftpUsername();
        String privateKeyPath = config.getSftpPrivateKeyPath();
        String passphrase = nullSafe(config.getSftpPrivateKeyPassphrase());
        String knownHostsPath = nullSafe(config.getSftpKnownHostsPath());
        String strict = nullSafe(config.getSftpStrictHostKeyChecking());

        try {
            JSch jsch = new JSch();
            if (!knownHostsPath.isEmpty()) {
                jsch.setKnownHosts(knownHostsPath);
            }
            if (!passphrase.isEmpty()) {
                jsch.addIdentity(privateKeyPath, passphrase);
            } else {
                jsch.addIdentity(privateKeyPath);
            }

            session = jsch.getSession(username, host, port);
            java.util.Properties cfg = new java.util.Properties();
            cfg.put("StrictHostKeyChecking", strict.isEmpty() ? "no" : strict);
            session.setConfig(cfg);

            session.connect(20_000);

            Channel channel = session.openChannel("sftp");
            channel.connect(20_000);
            sftp = (ChannelSftp) channel;

            String remoteDir = nullSafe(config.getSftpRemoteDir());
            if (!remoteDir.isEmpty()) {
                try {
                    sftp.cd(remoteDir);
                } catch (SftpException e) {
                    throw new IOException("Remote directory not found: " + remoteDir, e);
                }
            }
        } catch (JSchException e) {
            throw new IOException("Failed to connect to SFTP server: " + host + ":" + port, e);
        }
    }


    @Override
    public boolean upload(File file) throws IOException {
        if (file == null || !file.isFile()) {
            throw new IOException("Invalid file: " + file);
        }

        ensureConnected();

        String remoteFinalName = file.getName();
        String remoteTempName = remoteFinalName + ".part";

        try (InputStream in = Files.newInputStream(file.toPath())) {
            sftp.put(in, remoteTempName);
            sftp.rename(remoteTempName, remoteFinalName);
        } catch (SftpException e) {
            throw new IOException("SFTP error while uploading " + file.getName(), e);
        }

        return true;
    }

    public void close() {
        if (sftp != null && sftp.isConnected()) {
            sftp.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    private static String nullSafe(String s) {
        return s == null ? "" : s.trim();
    }
}