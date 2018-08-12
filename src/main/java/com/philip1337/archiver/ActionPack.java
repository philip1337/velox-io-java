package com.philip1337.veloxio.archiver;

import com.philip1337.veloxio.ArchiveWriter;
import com.philip1337.veloxio.VeloxConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

class Entry {
    public String path;
    public String diskPath;
    public int flags;
}

public class ActionPack {
    /**
     * Path to the xml file
     */
    private String xmlPath;

    /**
     * Path to the archive (output)
     */
    private String packPath;

    /**
     * Files
     */
    private HashMap<String, Entry> files;

    /**
     * Constructor
     * @param xmlPath to the xml containing a file list
     * @param packPath to the output file
     */
    public ActionPack(String pXmlPath, String pPackPath) {
        this.xmlPath = pXmlPath;
        this.files = new HashMap();
        this.packPath = pPackPath;
    }

    /**
     * Run (action entry)
     */
    public void run() {
        if (!loadXml()) {
            System.out.printf("[Error] Failed to load xml: %s \n", xmlPath);
            return;
        }

        // Write
        if (!writeArchive())
            System.out.printf("[Error] Failed to write archive: %s \n", packPath);
        else
            System.out.printf("Successfully written: %s ", packPath);
    }


    /**
     * Read value from node
     * @param e entry
     * @param attr Node
     * @return boolean true if node valid
     */
    private boolean ReadValue(Entry e, Node attr) {
        if (attr.getNodeName() == "diskPath") {
            e.diskPath = attr.getNodeValue();
            return true;
        } else if(attr.getNodeName() == "path") {
            e.path = attr.getNodeValue();
            return true;
        } else if(attr.getNodeName() == "flags") {
            String data = attr.getNodeValue().trim().replace(" ", "");
            String[] flags = data.split("\\|");

            // Flags
            for(int i = 0; i < flags.length; i++) {
                if (flags[i].equals("COMPRESS")) {
                    e.flags = e.flags | VeloxConfig.COMPRESS;
                } else if(flags[i].equals("CRYPT")) {
                    e.flags = e.flags | VeloxConfig.CRYPT;
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Load xml
     * @return boolean true if success
     */
    private boolean loadXml() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        factory.setValidating(false);
        DocumentBuilder builder = null;
        Document doc = null;
        // Get parser
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            //e.printStackTrace();
            System.out.printf("Failed to initialize parser with error: %s", xmlPath, e.getMessage());
            return false;
        }

        // Own error handler
        builder.setErrorHandler(new XmlErrorHandler());

        // Parse xml
        File file = new File(xmlPath);
        try {
            doc = builder.parse(file);
        } catch (SAXException e) {
            System.out.printf("Failed to parse xml %s with error: %s", xmlPath, e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.printf("Failed to parse xml %s with error: %s", xmlPath, e.getMessage());
            return false;
        }

        // Get root
        Node root = doc.getChildNodes().item(0);
        if (root == null || !root.getNodeName().equals("Archive")) {
            System.out.printf("Missing root 'Archive' in: %s \n", xmlPath);
        }

        // Loop trough files
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                Entry e = new Entry();

                // Attrs
                Node attr1 = currentNode.getAttributes().item(0);
                Node attr2 = currentNode.getAttributes().item(1);
                Node attr3 = currentNode.getAttributes().item(2);

                // Parse 2 attributes
                if (!ReadValue(e, attr1))
                    continue;
                if (!ReadValue(e, attr2))
                    continue;
                if (attr3 != null) {
                    if (!ReadValue(e, attr3)) {
                        continue;
                    }
                }

                // Add file to map
                files.put(e.path, e);
            }
        }

        return true;
    }

    private boolean writeArchive() {
        ArchiveWriter writer = null;
        try {
            writer = new ArchiveWriter(packPath);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            return false;
        }

        // Write archive
        for (HashMap.Entry<String, Entry> entry : files.entrySet()) {
            Entry value = entry.getValue();
            try {
                writer.writeFile(value.path, value.diskPath, value.flags);
            } catch (IOException e) {
                System.out.printf("[Error] Failed to write file: %s error: %s",value.path, e.getMessage());
                return false;
            }
        }

        // Write index and header
        writer.writeIndex();
        writer.writeHeader();
        return true;
    }
}
