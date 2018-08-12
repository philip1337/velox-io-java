package com.philip1337.veloxio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class Provider {
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
     *
     * @param path to the archive
     * @return booolean true if it was successfull
     * @throws FileNotFoundException
     */
    public boolean registerArchive(String path) throws FileNotFoundException {
        // Archive already registered
        if (archives.containsKey(path))
            return false;

        Archive a = new Archive(path);
        try {
            if (loader.load(path, a)) {
                archives.put(path, a);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Get file
     *
     * @param pPath to the file in the vfs
     * @return File
     * @throws IOException File not found
     */
    public ArchiveFile get(String pPath) throws IOException {
        long hashedPath = loader.getHasher().getPath(pPath);
        //archives.forEach((k,v)-> {
        //    if (v.HasFile(hashedPath)) {
        //        ArchiveEntry e = v.GetEntry(hashedPath);
        //        return new File(v, e);
        //    }
        //});   // Lambda return (I don't know how this works)

        for (HashMap.Entry<String, Archive> entry : archives.entrySet()) {
            Archive archive = entry.getValue();
            if (archive.hasFile(hashedPath)) {
                return new ArchiveFile(archive, archive.getEntry(hashedPath), pPath);
            }
        }

        // If our vfs provider do not own the file we just return an exception
        throw new IOException("File not found: " + pPath);
    }
}