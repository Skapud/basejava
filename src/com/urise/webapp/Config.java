package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final File PROPS = new File("config/resumes.properties");
    private static final Config INSTANCE = new Config();

    private Properties props = new Properties();
    private File storageDir;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try (InputStream IS = new FileInputStream(PROPS)) {
            props.load(IS);
            storageDir = new File(props.getProperty("storage.dir"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalid Config File " + PROPS.getAbsolutePath());
        }
    }

    public File getStorageDir() {
        return storageDir;
    }
}

