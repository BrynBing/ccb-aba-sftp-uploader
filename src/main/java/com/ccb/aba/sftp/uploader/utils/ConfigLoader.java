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

    public String getMountContains() {
        return getRequired("mount.expect.contains");
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
}
