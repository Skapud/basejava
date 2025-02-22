package com.urise.webapp.storage;

import com.urise.webapp.storage.serializers.DataStreamSerializer;
import com.urise.webapp.storage.serializers.ObjectStreamSerializer;

public class DataPathStorageTest extends AbstractStorageTest {
    public DataPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new DataStreamSerializer()));
    }
}
