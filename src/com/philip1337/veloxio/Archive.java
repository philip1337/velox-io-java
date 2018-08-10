package com.philip1337.veloxio;

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
     * Archive constructor
     */
    public Archive() {
        files = new HashMap();
    }

    /**
     * Set path
     * @param path to the file / archive container on disk
     */
    public void SetPath(String path) {
        this.path = path;
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
}