package com.urise.webapp.storage;

import com.urise.webapp.exception.*;
import com.urise.webapp.model.Resume;
import org.junit.jupiter.api.*;
import static com.urise.webapp.storage.AbstractArrayStorage.STORAGE_LIMIT;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    protected AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test
    void saveOverflow() {
        storage.clear();
        for(int i = 0; i < STORAGE_LIMIT; i++) {
            try {
                storage.save(new Resume("uuid" + i, ""));
            } catch (Exception e) {
                Assertions.fail("Error during fill");
            }
        }
        assertThrows(StorageException.class, () -> storage.save(new Resume(String.valueOf(STORAGE_LIMIT), "")));
    }
}