package com.philip1337.veloxio;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.util.HashMap;

class Archive {
    /**
     * Files
     */
    private HashMap<Long , ArchiveEntry> files;

    /**
     * Archive path on disk
     */
    private String path;

    /**
     * File handle (reader)
     */
    private FileInputStream handle;

    /**
     * Archive constructor
     */
    public Archive(String path) throws FileNotFoundException {
        this.files = new HashMap();
        this.path = path;
        this.handle = new FileInputStream(path);
    }

    /**
     * Set path
     * @param path to the file / archive container on disk
     */
    public void SetPath(String path) {
        this.path = path;
    }

    /**
     * Get path
     * @return
     */
    public String GetPath() {
        return path;
    }

    /**
     * Has file
     * @return true if archive owns the file
     */
    public boolean HasFile(long path) {
        return files.containsKey(path);
    }

    /**
     * Get archive entry
     * @param path to the file
     * @return ArchiveEntry for the file
     */
    public ArchiveEntry GetEntry(long path) {
        return files.get(path);
    }

    /**
     * Get handle
     * @return FileReader
     */
    public FileInputStream GetHandle() {
        return this.handle;
    }
}