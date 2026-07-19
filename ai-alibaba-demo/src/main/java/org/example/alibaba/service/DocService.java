package org.example.alibaba.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author floyd
 */
@Service
public class DocService {

    public List<Document> loadDocument(String filePath) {
        File file = new File(filePath);
        List<File> docFiles = new ArrayList<>();
        if (file.isDirectory()) {
            for (File listFile : file.listFiles()) {
                if (listFile.getName().endsWith(".md")) {
                    docFiles.add(listFile);
                }
            }
        } else if (file.getName().endsWith(".md")) {
            docFiles.add(file);
        }

        List<Document> documentList = new ArrayList<>();
        for (File docFile : docFiles) {
            TextReader textReader = new TextReader("file:" + docFile.getAbsolutePath());
            documentList.addAll(textReader.read());
        }
        return documentList;
    }
}
