package com.philip1337.veloxio;

public class ArchiveHeader {
    /**
     * Containing the file count
     */
    public int count;

    /**
     * Containing the offset where the archive index starts
     */
    public int offset;

    /**
     * Magic (verify header / archive)
     */
    public String magic;
}
