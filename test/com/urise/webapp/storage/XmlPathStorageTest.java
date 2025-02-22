package com.urise.webapp.storage;

import com.urise.webapp.storage.serializers.XmlStreamSerializer;

import static com.urise.webapp.storage.AbstractStorageTest.STORAGE_DIR;

public class XmlPathStorageTest extends AbstractStorageTest {
    public XmlPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new XmlStreamSerializer()));
    }
}
