package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.nsu.basargina.SubstringSearcher.findSubstringInFile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Class with tests for substring search.
 */
public class SubstringSearcherTest {
    /**
     * Create temporary file.
     *
     * @param content - content to be written in the file
     * @return temporary file
     * @throws IOException if something went wrong during file creation
     */
    private File createTempFile(String content) throws IOException {
        File tempFile = File.createTempFile("tempFile", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        tempFile.deleteOnExit();
        return tempFile;
    }

    @Test
    public void testFindSubstringSuccess() throws Exception {
        String content = "This is a test string. test KMP search.";
        File tempFile = createTempFile(content);
        String pattern = "test";

        List<Integer> results = findSubstringInFile(tempFile.getPath(), pattern);
        List<Integer> expected = new ArrayList<>();
        expected.add(10);
        expected.add(23);

        assertFalse(results.isEmpty());
        assertEquals(expected, results);
    }

    @Test
    public void testFindSubstringRussian() throws Exception {
        String content = "Это тест для поиска подстроки. Начать тест.";
        File tempFile = createTempFile(content);
        String pattern = "тест";

        List<Integer> results = findSubstringInFile(tempFile.getPath(), pattern);
        List<Integer> expected = new ArrayList<>();
        expected.add(4);
        expected.add(38);

        assertFalse(results.isEmpty());
        assertEquals(expected, results);
    }

    @Test
    public void testFindSubstringFail() throws Exception {
        String content = "This is some lol text.";
        File tempFile = createTempFile(content);
        String pattern = "lollol";

        List<Integer> results = findSubstringInFile(tempFile.getPath(), pattern);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFindMultipleOccurrences() throws Exception {
        String content = "abcabcabcaaaaaaaaabbbbbcc";
        File tempFile = createTempFile(content);
        String pattern = "abc";

        List<Integer> expected = new ArrayList<>();
        expected.add(0);
        expected.add(3);
        expected.add(6);

        List<Integer> results = findSubstringInFile(tempFile.getPath(), pattern);
        assertFalse(results.isEmpty());
        assertEquals(expected, results);
    }

    @Test
    public void testEmptyText() throws Exception {
        String content = "";
        File tempFile = createTempFile(content);
        String pattern = "hello";

        List<Integer> results = findSubstringInFile(tempFile.getPath(), pattern);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testEmptyPattern() throws Exception {
        String content = "Some random random ??.";
        File tempFile = createTempFile(content);
        String pattern = "";

        List<Integer> results = findSubstringInFile(tempFile.getPath(), pattern);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSeveralLinesInText() throws Exception {
        String content = "Line 1\nLine 2\nLine 1";
        File tempFile = createTempFile(content);
        String pattern = "Line 1";

        List<Integer> expected = new ArrayList<>();
        expected.add(0);
        expected.add(14);

        List<Integer> results = findSubstringInFile(tempFile.getPath(), pattern);
        assertFalse(results.isEmpty());
        assertEquals(expected, results);
    }

    @Test
    public void testLargeFile() throws Exception {
        File tempFile = File.createTempFile("largeTempFile", ".txt");
        long size = 10 * 1024 * 1024; // 10 MB
        String content = "This is a test line. ";
        String pattern = "line";

        try (FileWriter writer = new FileWriter(tempFile)) {
            while (tempFile.length() < size) {
                writer.write(content);
            }
        }

        tempFile.deleteOnExit();

        List<Integer> results = findSubstringInFile(tempFile.getPath(), pattern);

        long expectedOccurrences = size / content.length() + 1;
        assertFalse(results.isEmpty());
        assertEquals(expectedOccurrences, results.size());
    }
}