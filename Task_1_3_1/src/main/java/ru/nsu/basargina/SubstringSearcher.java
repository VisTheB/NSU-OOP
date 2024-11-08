package ru.nsu.basargina;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for finding substring in the file.
 */
public class SubstringSearcher {
    /**
     * Method reads the file by chars and searches for substring occurrences.
     *
     * @param filename - name of the file to read
     * @param substring - substring to find
     * @return list of indexes of the beginning of each substring occurrence
     * @throws IOException if error during reading file occurred
     */
    public static List<Integer> findSubstringInFile(String filename,
                                                    String substring) throws IOException {
        List<Integer> resultIndices = new ArrayList<>();
        if (substring.isEmpty()) {
            return resultIndices;
        }

        int index = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            StringBuilder str = new StringBuilder();

            int charRead;
            while ((charRead = br.read()) != -1) {
                char currentChar = (char) charRead;
                str.append(currentChar);

                if (str.length() > substring.length()) {
                    str.deleteCharAt(0);
                    index++;
                }

                if (str.length() == substring.length() && str.toString().equals(substring)) {
                    resultIndices.add(index);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading from file", e);
        }
        return resultIndices;
    }
}