package com.philip1337.veloxio;

import com.philip1337.veloxio.utils.XXTEA;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

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
    private byte[] GetFromContainer() {
        return new byte[1];
    }

    public byte[] Get() {
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
                // Somehting went wrong here
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
