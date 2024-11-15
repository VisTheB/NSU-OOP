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
    public static List<Long> findSubstringInFile(String filename,
                                                 String substring) throws IOException {
        List<Long> resultIndices = new ArrayList<>();
        if (substring.isEmpty()) {
            return resultIndices;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            long index = 0;
            char currentChar = 0;

            ArrayList<Character> buff = new ArrayList<>();

            for (int i = 0; i < substring.length(); i++) {
                currentChar = (char) br.read();
                buff.add(currentChar);
            }

            boolean cmpFlag;
            while (currentChar != 65535) {
                cmpFlag = true;
                for (int i = 0; i < substring.length(); i++) {
                    if (buff.get(i) != substring.charAt(i)) {
                        cmpFlag = false;
                        break;
                    }
                }
                if (cmpFlag) {
                    resultIndices.add(index);
                }
                index++;

                buff.removeFirst();
                currentChar = (char) br.read();
                buff.add(currentChar);
            }
        } catch (IOException e) {
            throw new IOException("Error reading from file", e);
        }
        return resultIndices;
    }
}