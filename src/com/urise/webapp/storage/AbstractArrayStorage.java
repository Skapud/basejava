package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;
import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public abstract class AbstractArrayStorage implements Storage {
    protected static final int STORAGE_LIMIT = 10000;

    protected final Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public final void update(Resume r) {
        int index = getIndex(r.toString());
        if (index < 0) {
            System.out.println("Резюме " + r + " не найдено");
            return;
        }
        storage[index] = r;
    }

    public final void save(Resume r) {
        if (size >= storage.length) {
            System.out.println("Нет места, хранилище переполнено");
        } else if (getIndex(r.toString()) >= 0) {
            System.out.println("Резюме " + r + " уже было добавлено");
        } else {
            insertResume(r);
            size++;
        }
    }

    protected abstract void insertResume(Resume r);

    public final Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("Резюме " + uuid + " не найдено");
            return null;
        }
        return storage[index];
    }

    public final void delete(String uuid) {
        int index = getIndex(uuid);
        if (index < 0) {
            System.out.println("Резюме " + uuid + " не найдено");
            return;
        }
        fillDeletedElement(index);
        storage[size - 1] = null;
        size--;
    }

    protected abstract void fillDeletedElement(int index);

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    protected abstract int getIndex(String uuid);

    public int size() {
        return size;
    }
}
