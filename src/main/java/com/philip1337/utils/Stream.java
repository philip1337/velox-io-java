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
     *
     * @param pName String
     * @throws FileNotFoundException if file not found
     */
    public Stream(String pName) throws FileNotFoundException {
        super(pName);
        this.offset = 0;
        try {
            this.getChannel().position(0);
        } catch (IOException e) {
            // Usually not happening
        }
    }

    /**
     * Seek
     *
     * @param offset position to seek
     */
    public void seek(int offset) {
        this.offset = offset;
    }

    /**
     * Read integer
     *
     * @return int
     * @throws IOException
     */
    public final int readInt() throws IOException {
        byte[] bytes = new byte[4];
        if (this.read(bytes, 0, 4) != 4)
            throw new IOException("Failed to read int position: " + this.getChannel().position() + " eof: " +
                                  this.getChannel().size());

        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getInt();
    }

    /**
     * Read string
     *
     * @param size of the string
     * @return String containg the bytes
     */
    public final String readString(int size) throws IOException {
        byte[] buffer = new byte[size];
        if (this.read(buffer, 0, size) != size)
            throw new IOException("Failed to read string");
        return new String(buffer, StandardCharsets.UTF_8);
    }

    /**
     * Read long
     *
     * @return long
     * @throws IOException
     */
    public final long readLong() throws IOException {
        byte[] bytes = new byte[8];
        if (this.read(bytes, 0, 8) != 8)
            throw new IOException("Failed to read long");

        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}
