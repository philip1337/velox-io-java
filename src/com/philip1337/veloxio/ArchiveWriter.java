package com.philip1337.veloxio;

import com.philip1337.veloxio.utils.XXHash;
import com.philip1337.veloxio.utils.XXTEA;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class ArchiveWriter {
    /**
     * Path to the archive output
     */
    private String path;

    /**
     * ArchiveHeader
     */
    private ArchiveHeader header;

    /**
     * Files
     */
    private HashMap<Long, ArchiveEntry> files;

    /**
     * Hasher
     */
    private XXHash hasher;

    /**
     * Output file writer
     */
    private FileOutputStream output;

    /**
     * Current offset
     */
    private int currentOffset;

    /**
     * Constructor
     *
     * @param path String to the output
     */
    public ArchiveWriter(String path) throws FileNotFoundException {
        this.files = new HashMap();
        this.path = path;
        this.hasher = new XXHash();
        this.output = new FileOutputStream(path);

        // Initial position after skipping the archive header padding
        try {
            this.output.getChannel().position(GetArchiveHeaderLength());
        } catch (IOException e) {
            throw new FileNotFoundException("Invalid file stream: " + this.path);
        }
    }

    /**
     * This is the initi
     *
     * @return
     */
    public int GetArchiveHeaderLength() {
        return (2 * 4) + VeloxConfig.magic.length();
    }

    /**
     * Write file
     */
    public boolean WriteFile(String path, String diskPath, int flags) throws IOException {
        // Hashed path
        long hashedPath = hasher.GetPath(path);

        // File
        File file = new File(diskPath);
        FileInputStream input = new FileInputStream(file);

        // Read into byte array
        byte[] buffer = new byte[(int) file.length()];
        int offset = 0;
        int numRead = 0;
        while (numRead >= 0) {
            numRead = input.read(buffer, offset, buffer.length - offset);
            offset += numRead;
        }

        ArchiveEntry entry = new ArchiveEntry();
        entry.flags = flags;
        entry.diskSize = buffer.length;
        entry.size = entry.diskSize;

        //if ((flags & VeloxConfig.CRYPT) == VeloxConfig.RAW) {
        //    // RAW
        //}

        if ((entry.flags & VeloxConfig.COMPRESSED) == VeloxConfig.COMPRESSED) {
            // COMPRESSED
            LZ4Factory factory = LZ4Factory.fastestInstance();
            LZ4Compressor compressor = factory.fastCompressor();

            int decompressedLength = (int) file.length();

            int maxCompressedLength = compressor.maxCompressedLength(decompressedLength);
            byte[] compressed = new byte[maxCompressedLength];
            entry.size = compressor.compress(buffer, 0, decompressedLength, compressed, 0,
                    maxCompressedLength);
            buffer = compressed;
        }

        if ((entry.flags & VeloxConfig.CRYPT) == VeloxConfig.CRYPT) {
            // ENCRYPTED
            buffer = XXTEA.encrypt(buffer, path.getBytes());
        }

        // Write to file
        output.write(buffer);

        // Add file to archive index
        files.put(hashedPath, entry);
        return true;
    }

    /**
     * Write index
     *
     * @return boolean true if success
     */
    public boolean WriteIndex() {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

        try {
            header.offset = (int) output.getChannel().position();

            for (HashMap.Entry<Long, ArchiveEntry> set : files.entrySet()) {
                ArchiveEntry entry = set.getValue();

                // Path
                buffer.reset();
                buffer.putLong(entry.path);
                output.write(buffer.array());

                // Disk size
                buffer.reset();
                buffer.putInt(entry.diskSize);
                output.write(buffer.array());

                // Flags
                buffer.reset();
                buffer.putInt(entry.flags);
                output.write(buffer.array());

                // Offset
                buffer.reset();
                buffer.putInt(entry.offset);
                output.write(buffer.array());

                // Size
                buffer.reset();
                buffer.putInt(entry.size);
                output.write(buffer.array());
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Write header
     *
     * @return boolean true if success
     */
    public boolean WriteHeader() {
        header.count = files.size();
        header.magic = VeloxConfig.magic;

        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        try {
            this.output.getChannel().position(0);

            // Count
            buffer.reset();
            buffer.putInt(header.count);
            output.write(buffer.array());

            // Offset
            buffer.reset();
            buffer.putInt(header.offset);
            output.write(buffer.array());

            // Magic
            output.write(header.magic.getBytes());
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
