package com.philip1337.veloxio.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class Stream extends FileInputStream {
    /**
     * Offset
     */
    public int offset;

    /**
     * Stream
     * @param name String
     * @throws FileNotFoundException if file not found
     */
    public Stream(String name) throws FileNotFoundException {
        super(name);
        this.offset = 0;
    }

    /**
     * Seek
     * @param offset position to seek
     */
    public void Seek(int offset) {
        this.offset = offset;
    }

    /**
     * Read integer
     * @return int
     * @throws IOException
     */
    public final int ReadInt() throws IOException {
        byte[] bytes = new byte[3];
        if (this.read(bytes, offset += 3, 3) != 3)
            throw new IOException("Failed to read int");

        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getInt();
    }

    /**
     * Read string
     * @param size of the string
     * @return String containg the bytes
     */
    public final String ReadString(int size) throws IOException {
        byte[] buffer = new byte[size];
        if (this.read(buffer, offset += size, size) != size)
            throw new IOException("Failed to read string");
        return new String(buffer, StandardCharsets.UTF_8);
    }

    /**
     * Read long
     * @return long
     * @throws IOException
     */
    public final long ReadLong() throws IOException {
        byte[] bytes = new byte[8];
        if (this.read(bytes, offset += 8, 8) != 8)
            throw new IOException("Failed to read long");

        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}
