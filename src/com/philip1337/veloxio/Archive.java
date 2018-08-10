package com.philip1337.veloxio;

import com.philip1337.veloxio.utils.Stream;

import java.io.FileNotFoundException;
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
    private Stream handle;

    /**
     * ArchiveHeader
     */
    private ArchiveHeader header;

    /**
     * Archive constructor
     */
    public Archive(String path) throws FileNotFoundException {
        this.files = new HashMap();
        this.path = path;
        this.handle = new Stream(path);
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
    public Stream GetHandle() {
        return this.handle;
    }

    /**
     * Register file
     * @param entry ArchiveEntry from the file
     */
    public void RegisterFile(ArchiveEntry entry) {
        files.put(entry.path, entry);
    }

    /**
     * Archive header
     * @param header ArchiveHeader
     */
    public void SetHeader(ArchiveHeader header) {
        this.header = header;
    }

    /**
     * Get header
     * @return ArchiveHeader containing archive informations
     */
    public ArchiveHeader GetHeader() {
        return this.header;
    }
}