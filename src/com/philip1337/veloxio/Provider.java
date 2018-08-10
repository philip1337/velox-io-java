package com.philip1337.veloxio;

import com.philip1337.veloxio.utils.XXHash;
import net.jpountz.xxhash.StreamingXXHash64;
import net.jpountz.xxhash.XXHashFactory;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

class Provider {
    /**
     * Archives
     */
    private HashMap<String, Archive> archives;

    /**
     * Archive loader
     */
    private ArchiveLoader loader;

    /**
     * Constructor
     */
    public Provider() {
        archives = new HashMap();
        loader = new ArchiveLoader();
    }

    /**
     * Register archive
     * @param path to the archive
     * @return booolean true if it was successfull
     * @throws FileNotFoundException
     */
    public boolean RegisterArchive(String path) throws FileNotFoundException {
        // Archive already registered
        if (archives.containsKey(path))
            return false;

        Archive a = new Archive(path);
        try {
            if (loader.Load(path, a)) {
                archives.put(path, a);
                return true;
            }
        } catch (IOException e) {
            // e.printStackTrace();
        }

        return false;
    }

    /**
     * Get file
     * @param path to the file in the vfs
     * @return File
     * @throws IOException File not found
     */
    public ArchiveFile Get(String path) throws IOException {
        long hashedPath = loader.GetHasher().GetPath(path);
        //archives.forEach((k,v)-> {
        //    if (v.HasFile(hashedPath)) {
        //        ArchiveEntry e = v.GetEntry(hashedPath);
        //        return new File(v, e);
        //    }
        //});   // Lambda return (I don't know how this works)

        for(HashMap.Entry<String, Archive> entry : archives.entrySet()) {
            Archive archive = entry.getValue();
            if (archive.HasFile(hashedPath)) {
                return new ArchiveFile(archive, archive.GetEntry(hashedPath), path);
            }
        }

        // If our vfs provider do not own the file we just return an exception
        throw new IOException("File not found: " + path);
    }
}