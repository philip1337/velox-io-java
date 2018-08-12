package com.philip1337.veloxio;

import com.philip1337.veloxio.utils.Stream;
import com.philip1337.veloxio.utils.XXTEA;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

import java.io.IOException;

public class ArchiveFile {
    /**
     * Archive
     */
    private Archive archive;

    /**
     * ArchiveEntry
     */
    private ArchiveEntry entry;

    /**
     * Path
     */
    private String path;

    /**
     * File constructor
     *
     * @param pArchive parent of the file
     * @param pEntry   entry data
     * @param pPath    effective path for the file
     */
    public ArchiveFile(Archive pArchive, ArchiveEntry pEntry, String pPath) {
        this.archive = pArchive;
        this.entry = pEntry;
        this.path = pPath;
    }

    /**
     * Get file buffer
     *
     * @return byte array (containing the buffer)
     */
    private byte[] getFromContainer() throws IOException {
        Stream stream = this.archive.getHandle();

        // Set offset
        stream.getChannel().position(this.entry.offset);

        // Read
        byte[] buffer = new byte[this.entry.size];
        int read = stream.read(buffer, 0, this.entry.size);
        if (read != this.entry.size)
            throw new IOException("Failed to read file from archive: " + archive.getPath());

        return buffer;
    }

    public byte[] get() throws IOException {
        byte[] buffer = getFromContainer();

        if ((entry.flags & VeloxConfig.CRYPT) == VeloxConfig.CRYPT) {
            // CRYPT
            buffer = XXTEA.decrypt(buffer, path.getBytes());
        }

        if ((entry.flags & VeloxConfig.COMPRESS) == VeloxConfig.COMPRESS) {
            // COMPRESSED
            LZ4Factory factory = LZ4Factory.fastestInstance();
            LZ4FastDecompressor decompressor = factory.fastDecompressor();
            byte[] newBuffer = new byte[entry.diskSize];
            int decompressedLength = decompressor.decompress(buffer, 0, newBuffer, 0, entry.diskSize);
            buffer = newBuffer; // Move the buffer
        }

        //if ((entry.flags & VeloxConfig.RAW) == VeloxConfig.RAW) {
        //  // RAW
        //  //  We do nothing here
        //}

        return buffer;
    }
}
