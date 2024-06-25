package com.conner.assistant.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextSplitter {

    //TODO Build into Sentence TextSplitter add chunk overlap
    public static List<String> splitText(String filePath) {
        List<String> textLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()){
                    textLines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            e.printStackTrace();
        }
        return textLines;
    }
}

