package com.philip1337.veloxio;

import com.philip1337.veloxio.utils.Stream;
import com.philip1337.veloxio.utils.XXHash;

import java.io.IOException;

public class ArchiveLoader {
    /**
     * Hasher
     */
    private XXHash hasher;

    /**
     * Loader constructor
     */
    public ArchiveLoader() {
        hasher = new XXHash();
    }

    /**
     * Hasher used to hash paths
     *
     * @return XXHash
     */
    public XXHash GetHasher() {
        return hasher;
    }

    /**
     * Load archive
     *
     * @param path String
     * @param a    Archive
     * @return boolean true if success
     * @throws IOException if archive stream is invalid
     */
    public boolean Load(String path, Archive a) throws IOException {
        a.SetPath(path);

        // Validate header header
        if (!ValidateHeader(a))
            return false;

        // Failed to read entries
        if (!ReadEntries((a)))
            return false;

        return true;
    }

    /**
     * Validate header
     *
     * @param a Archive container
     * @return boolean true if everything went well
     */
    private boolean ValidateHeader(Archive a) throws IOException {
        Stream stream = a.GetHandle();

        ArchiveHeader header = new ArchiveHeader();
        header.count = stream.ReadInt();
        header.offset = stream.ReadInt();
        header.magic = stream.ReadString(VeloxConfig.magic.length());

        // Invalid magic
        if (!header.magic.equals(VeloxConfig.magic))
            return false;

        a.SetHeader(header);
        return true;
    }

    /**
     * Read entries from index
     *
     * @param a Archive container
     * @return boolean if success true
     * @throws IOException if stream is invalid
     */
    private boolean ReadEntries(Archive a) throws IOException {
        Stream stream = a.GetHandle();
        ArchiveHeader header = a.GetHeader();

        // Seek to offset where the header starts
        stream.Seek(header.offset);

        // Read index
        for (int i = 0; i <= header.count; i++) {
            // Archive entry
            ArchiveEntry entry = new ArchiveEntry();

            // This has to be in the exact order to work (we read bytes by bytes)
            entry.path = stream.ReadLong();
            entry.diskSize = stream.ReadInt();
            entry.flags = stream.ReadInt();
            entry.offset = stream.ReadInt();
            entry.size = stream.ReadInt();

            // Register file
            a.RegisterFile(entry);
        }

        return true;
    }
}
