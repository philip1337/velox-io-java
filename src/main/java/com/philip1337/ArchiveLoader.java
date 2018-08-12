package com.philip1337.veloxio;

import com.philip1337.veloxio.utils.Stream;
import com.philip1337.veloxio.utils.XXHash;

import java.io.IOException;

class ArchiveLoader {
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
    public XXHash getHasher() {
        return hasher;
    }

    /**
     * Load archive
     *
     * @param path String
     * @param pArchive    Archive
     * @return boolean true if success
     * @throws IOException if archive stream is invalid
     */
    public boolean load(String path, Archive pArchive) throws IOException {
        pArchive.setPath(path);

        // Validate header header
        if (!validateHeader(pArchive))
            return false;

        // Failed to read entries
        return readEntries((pArchive));
    }

    /**
     * Validate header
     *
     * @param pArchive Archive container
     * @return boolean true if everything went well
     */
    private boolean validateHeader(Archive pArchive) throws IOException {
        Stream stream = pArchive.getHandle();

        ArchiveHeader header = new ArchiveHeader();
        header.count = stream.readInt();
        header.offset = stream.readInt();
        header.magic = stream.readString(VeloxConfig.magic.getBytes().length);

        // Invalid magic
        if (!header.magic.equals(VeloxConfig.magic)) {
            System.out.printf("Invalid magic %s != %s. \n", header.magic, VeloxConfig.magic);
            return false;
        }

        pArchive.setHeader(header);
        return true;
    }

    /**
     * Read entries from index
     *
     * @param pArchive Archive container
     * @return boolean if success true
     * @throws IOException if stream is invalid
     */
    private boolean readEntries(Archive pArchive) throws IOException {
        Stream stream = pArchive.getHandle();
        ArchiveHeader header = pArchive.getHeader();

        // Seek to offset where the header starts
        stream.getChannel().position(header.offset);

        // Read index
        for (int i = 0; i < header.count; i++) {
            // Archive entry
            ArchiveEntry entry = new ArchiveEntry();

            // This has to be in the exact order to work (we read bytes by bytes)
            entry.path = stream.readLong();     // 8
            entry.diskSize = stream.readInt();  // 4
            entry.flags = stream.readInt();     // 4
            entry.offset = stream.readInt();    // 4
            entry.size = stream.readInt();      // 4
            entry.decryptedSize = stream.readInt(); // 4

            // Register file
            pArchive.registerFile(entry);
        }
        return true;
    }
}
