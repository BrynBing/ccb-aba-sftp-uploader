package com.ccb.aba.sftp.uploader.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final ConfigLoader INSTANCE = new ConfigLoader();
    private final Properties properties = new Properties();

    private ConfigLoader() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (in == null) {
                throw new IllegalStateException("application.properties not found on classpath.");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }

    public static ConfigLoader getInstance() {
        return INSTANCE;
    }

    public String getSageDir() {
        return getRequired("path.sage");
    }
    public String getProcessedDir() {
        return getRequired("path.processed");
    }

    private String getRequired(String key) {
        String v = properties.getProperty(key);
        if (v == null || v.trim().isEmpty()) {
            throw new IllegalStateException("Missing required property: " + key);
        }
        return v;
    }

    // --- SFTP ---
    public String getSftpHost() { return getRequired("sftp.host"); }
    public int getSftpPort() { return Integer.parseInt(getRequired("sftp.port")); }
    public String getSftpUsername() { return getRequired("sftp.username"); }
    public String getSftpPrivateKeyPath() { return getRequired("sftp.private.key.path"); }

    public String getSftpPrivateKeyPassphrase() { return properties.getProperty("sftp.private.key.passphrase"); }
    public String getSftpKnownHostsPath() { return properties.getProperty("sftp.known.hosts.path"); }
    public String getSftpStrictHostKeyChecking() { return properties.getProperty("sftp.strict.hostkey"); } // "yes"/"no"
    public String getSftpRemoteDir() { return properties.getProperty("sftp.remote.dir"); }

}
