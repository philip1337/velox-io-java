package com.philip1337.veloxio;

import com.philip1337.veloxio.utils.Stream;

import java.io.FileNotFoundException;
import java.util.HashMap;

class Archive {
    /**
     * Files
     */
    private HashMap<Long, ArchiveEntry> files;

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
    public Archive(String pPath) throws FileNotFoundException {
        this.files = new HashMap();
        this.path = pPath;
        this.handle = new Stream(path);
    }

    /**
     * Set path
     *
     * @param pPath to the file / archive container on disk
     */
    public void setPath(String pPath) {
        this.path = pPath;
    }

    /**
     * Get path
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * Has file
     *
     * @return true if archive owns the file
     */
    public boolean hasFile(long pPath) {
        return files.containsKey(pPath);
    }

    /**
     * Get archive entry
     *
     * @param pPath to the file
     * @return ArchiveEntry for the file
     */
    public ArchiveEntry getEntry(long pPath) {
        return files.get(pPath);
    }

    /**
     * Get handle
     *
     * @return FileReader
     */
    public Stream getHandle() {
        return this.handle;
    }

    /**
     * Register file
     *
     * @param pEntry ArchiveEntry from the file
     */
    public void registerFile(ArchiveEntry pEntry) {
        files.put(pEntry.path, pEntry);
    }

    /**
     * Archive header
     *
     * @param pHeader ArchiveHeader
     */
    public void setHeader(ArchiveHeader pHeader) {
        this.header = pHeader;
    }

    /**
     * Get header
     *
     * @return ArchiveHeader containing archive informations
     */
    public ArchiveHeader getHeader() {
        return this.header;
    }
}