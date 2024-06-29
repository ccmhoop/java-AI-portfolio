package com.conner.assistant.utils;

import java.io.*;

public class TextNormalizer {

    //Adapt when needed example to PDF or MD
    public static String textRemoveEmptyLines(String filePath) throws IOException {
        StringBuilder buildText = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                buildText.append(line);
            }
        }
        return buildText.toString();
    }
}

