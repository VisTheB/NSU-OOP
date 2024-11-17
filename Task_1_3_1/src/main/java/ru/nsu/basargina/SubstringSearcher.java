package ru.nsu.basargina;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for finding substring in the file.
 */
public class SubstringSearcher {
    /**
     * Method reads the file by chars and searches for substring occurrences.
     *
     * @param inputStream - input stream for reading
     * @param substring - substring to find
     * @return list of indexes of the beginning of each substring occurrence
     * @throws IOException if error during reading file occurred
     */
    public static List<Long> findSubstringInFile(InputStream inputStream,
                                                 String substring) throws IOException {
        List<Long> resultIndices = new ArrayList<>();
        if (substring.isEmpty()) {
            return resultIndices;
        }

        try (InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            long index = 0;
            int currentChar = 0;

            ArrayList<Character> buff = new ArrayList<>();

            for (int i = 0; i < substring.length(); i++) {
                currentChar = isr.read();
                buff.add((char) currentChar);
            }

            boolean cmpFlag;
            while (currentChar != -1) {
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
                currentChar = isr.read();
                buff.add((char) currentChar);
            }
        } catch (IOException e) {
            throw new IOException("Error reading from file", e);
        }
        return resultIndices;
    }
}