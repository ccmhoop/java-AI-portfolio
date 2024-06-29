package com.conner.assistant.utils;

import java.io.*;

public class TextNormalizer {

    //Adapt when needed example to PDF or MD
    public static String removeEmptySpaces(String filePath) throws IOException {
        StringBuilder buildText = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                //replace double spaces and separates the next line with a space " "
                buildText.append(line.replaceAll("\\s+"," ")).append(" ");
            }
        }
        return buildText.toString();
    }
}

