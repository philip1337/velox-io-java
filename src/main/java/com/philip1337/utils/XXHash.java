package com.philip1337.veloxio.utils;

import com.philip1337.veloxio.VeloxConfig;
import net.jpountz.xxhash.StreamingXXHash64;
import net.jpountz.xxhash.XXHashFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class XXHash {
    /**
     * Hasher
     */
    private XXHashFactory hasher;

    /**
     * Hashing buffer
     */
    private byte[] buffer;

    /**
     * XXHash coonstructor
     */
    public XXHash() {
        buffer = new byte[1024];
        hasher = XXHashFactory.fastestInstance();
    }

    /**
     * XXHash algorithm
     * - Hash string into a hash64
     *
     * @param pPath file path
     * @return long
     * @throws IOException Buffer error
     */
    public long getPath(String pPath) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(pPath.getBytes("UTF-8"));

        // Build hash
        StreamingXXHash64 hash64 = hasher.newStreamingHash64(VeloxConfig.seed);

        // Buffer
        for (; ; ) {
            int read = in.read(buffer);
            if (read == -1) {
                break;
            }
            hash64.update(buffer, 0, read);
        }

        return hash64.getValue();
    }

}
