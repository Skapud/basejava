package com.urise.webapp;

import com.urise.webapp.storage.SqlStorage;
import com.urise.webapp.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Config {
    private static final File PROPS = new File(getHomeDir(),"config/resumes.properties");
    private static final Config INSTANCE = new Config();

    private final File storageDir;
    private final Storage storage;
    private Set<String> immutableUuids = new HashSet<>() {{  // for JDK 9+: Set.of("111", "222");
        add("11111111-1111-1111-1111-111111111111");
        add("22222222-2222-2222-2222-222222222222");
    }};

//    private final String dbUrl;
//    private final String dbUser;
//    private final String dbPassword;

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try (InputStream IS = new FileInputStream(PROPS)) {
            Properties props = new Properties();
            props.load(IS);
            storageDir = new File(props.getProperty("storage.dir"));
            storage = new SqlStorage(props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password"));

//            dbUrl = props.getProperty("db.url");
//            dbUser = props.getProperty("db.user");
//            dbPassword = props.getProperty("db.password");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid Config File " + PROPS.getAbsolutePath());
        }
    }

    public File getStorageDir() {
        return storageDir;
    }

    public Storage getStorage() {
        return storage;
    }

//    public String getDbUrl() {
//        return dbUrl;
//    }
//
//    public String getDbUser() {
//        return dbUser;
//    }
//
//    public String getDbPassword() {
//        return dbPassword;
//    }

    private static File getHomeDir() {
        String prop = System.getProperty("homeDir");
        File homeDir = new File(prop == null ? "." : prop);
        if (!homeDir.isDirectory()) {
            throw new IllegalStateException(homeDir + " is not directory");
        }
        return homeDir;
    }

    public boolean isImmutable(String uuids) {
        return immutableUuids.contains(uuids);
    }

    public void checkImmutable(String uuids) {
        if (immutableUuids.contains(uuids))
            throw new RuntimeException("Зарезервированные резюме нельзя менять");
    }
}

