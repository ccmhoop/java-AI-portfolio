package com.conner.assistant.rag;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

public class TextNormalizer {


    //TODO make it work with PDF and MD etc.
    /**
     * Removes empty spaces from a text file.
     *
     * @param filePath The path of the text file to be processed.
     * @return An Optional containing the text with removed empty spaces, or an empty Optional if an error occurs.
     */
    public static Optional<String> removeEmptySpaces(String filePath)  {
        StringBuilder buildText = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    //replace double spaces and separates the next line with a space " "
                    buildText.append(line.replaceAll("\\s+", " ")).append(" ");
                }
            }
        }catch (IOException e){
            return Optional.empty();
        }
        return Optional.of(buildText.toString());
    }

}