package com.philip1337.veloxio;

import com.philip1337.veloxio.utils.XXTEA;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.IOException;

class File {
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
     * @param archive parent of the file
     * @param entry entry data
     * @param path effective path for the file
     */
    public File(Archive archive, ArchiveEntry entry, String path) {
        this.archive = archive;
        this.entry = entry;
        this.path = path;
    }

    /**
     * Get file buffer
     * @return byte array (containing the buffer)
     */
    private byte[] GetFromContainer() throws IOException {
        DataInputStream stream = new DataInputStream(this.archive.GetHandle());

        byte[] buffer = new byte[this.entry.size];
        int read = stream.read(buffer, this.entry.offset, this.entry.size);
        if (read != this.entry.size)
            throw new IOException("Failed to read file from archive: " + archive.GetPath());

        return buffer;
    }

    public byte[] Get() throws IOException {
        byte[] buffer = GetFromContainer();

        //if ((entry.flags & VeloxConfig.RAW) == VeloxConfig.RAW) {
        //  // RAW
        //  //  We do nothing here
        //}

        if ((entry.flags & VeloxConfig.COMPRESSED) == VeloxConfig.COMPRESSED) {
            // COMPRESSED
            LZ4Factory factory = LZ4Factory.fastestInstance();
            LZ4FastDecompressor decompressor = factory.fastDecompressor();
            byte[] newBuffer = new byte[entry.diskSize];
            int compressedLength2 = decompressor.decompress(buffer, 0, newBuffer, 0, entry.diskSize);
            if (compressedLength2 != entry.size) {
                // Something went wrong here
                throw new IOException("Failed to decompress: " + path);
            }
            buffer = newBuffer; // Move the buffer
        }

        if ((entry.flags & VeloxConfig.CRYPT) == VeloxConfig.CRYPT) {
            // ENCRYPTED
            buffer = XXTEA.decrypt(buffer, path.getBytes());
        }

        return buffer;
    }
}
