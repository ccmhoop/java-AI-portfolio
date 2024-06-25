package com.conner.assistant.utils;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileReader {

    //TODO Build into Sentence TextSplitter
    public static String textFileReader(String filePath) {
        File file = new File(filePath);
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byte[] data = FileCopyUtils.copyToByteArray(inputStream);
            String content = new String(data, StandardCharsets.UTF_8);
            System.out.println(content);
            return content;
        } catch (IOException e) {
            System.err.println("Error reading file from resources: " + filePath);
            e.printStackTrace();
            return null;
        }
    }
}
