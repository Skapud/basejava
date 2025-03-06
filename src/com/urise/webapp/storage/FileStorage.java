package com.urise.webapp.storage;

import com.urise.webapp.exception.StorageException;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.serializers.Serializer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private final Serializer streamSerializer;

    protected FileStorage(File directory, Serializer streamSerializer) {
        Objects.requireNonNull(directory, "directory must not be null");

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.streamSerializer = streamSerializer;
        this.directory = directory;
    }

    @Override
    public void clear() {
        File[] files = getCheckedListFiles(directory);
        for (File file : files) {
            doDelete(file);
        }
    }

    @Override
    public int size() {
        File[] files = getCheckedListFiles(directory);
        return files.length;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected void doUpdate(Resume resume, File file) {
        try {
            streamSerializer.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected void doSave(Resume resume, File file) {
        try {
            file.createNewFile();
            streamSerializer.doWrite(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return streamSerializer.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Read error", file.getName(), null);
        }
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("File hasn't been deleted", file.getName());
        }
    }

    @Override
    protected List<Resume> doGetAllSorted() {
        List<Resume> list = new ArrayList<>();
        File[] files = getCheckedListFiles(directory);
        for (File file : files) {
            if (file.isFile()) {
                list.add(doGet(file));
            }
        }
        return list;
    }

    private File[] getCheckedListFiles(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Read error", null);
        }
        return files;
    }
}
