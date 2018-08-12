package com.philip1337.veloxio.archiver;

import com.philip1337.veloxio.ArchiveFile;
import com.philip1337.veloxio.Provider;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ActionValidate {
    /**
     * Path to the xml file
     */
    private String xmlPath;

    /**
     * Path to the archive (output)
     */
    private String packPath;

    /**
     * Path to the file (that's in the archive)
     */
    private String filePath;

    /**
     * Constructor
     * @param packPath to the pack archive
     */
    public ActionValidate(String pFile, String pPackPath) {
        this.filePath = pFile;
        this.packPath = pPackPath;
    }

    /**
     * Run (action entry)
     */
    public void run() {
        Provider p = new Provider();
        try {
            if (!p.registerArchive(packPath))
                System.out.printf("Failed to register archive: %s\n", packPath);
        } catch (FileNotFoundException e) {
            System.out.printf("[Error] Failed to register archive: %s with error: %s \n", packPath, e.getMessage());
            return;
        }

        try {
            ArchiveFile file = p.get(filePath);
            byte[] buffer = file.get();

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(buffer);
            }
        } catch (IOException e) {
            System.out.printf("[Error] Failed to get file from archive archive: %s with error: %s \n",
                              packPath, e.getMessage());
        }
    }
}
