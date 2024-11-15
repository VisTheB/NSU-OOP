package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.nsu.basargina.SubstringSearcher.findSubstringInFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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

        List<Long> results = findSubstringInFile(tempFile.getPath(), pattern);
        List<Long> expected = new ArrayList<>();
        expected.add(10L);
        expected.add(23L);

        assertFalse(results.isEmpty());
        assertEquals(expected, results);
    }

    @Test
    public void testFindSubstringRussian() throws Exception {
        String content = "Это тест для поиска подстроки. Начать тест.";
        File tempFile = createTempFile(content);
        String pattern = "тест";

        List<Long> results = findSubstringInFile(tempFile.getPath(), pattern);
        List<Long> expected = new ArrayList<>();
        expected.add(4L);
        expected.add(38L);

        assertFalse(results.isEmpty());
        assertEquals(expected, results);
    }

    @Test
    public void testFindSubstringFail() throws Exception {
        String content = "This is some lol text.";
        File tempFile = createTempFile(content);
        String pattern = "lollol";

        List<Long> results = findSubstringInFile(tempFile.getPath(), pattern);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFindMultipleOccurrences() throws Exception {
        String content = "abcabcabcaaaaaaaaabbbbbcc";
        File tempFile = createTempFile(content);
        String pattern = "abc";

        List<Long> expected = new ArrayList<>();
        expected.add(0L);
        expected.add(3L);
        expected.add(6L);

        List<Long> results = findSubstringInFile(tempFile.getPath(), pattern);
        assertFalse(results.isEmpty());
        assertEquals(expected, results);
    }

    @Test
    public void testEmptyText() throws Exception {
        String content = "";
        File tempFile = createTempFile(content);
        String pattern = "hello";

        List<Long> results = findSubstringInFile(tempFile.getPath(), pattern);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testEmptyPattern() throws Exception {
        String content = "Some random random ??.";
        File tempFile = createTempFile(content);
        String pattern = "";

        List<Long> results = findSubstringInFile(tempFile.getPath(), pattern);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSeveralLinesInText() throws Exception {
        InputStream in = getClass().getResourceAsStream("/test.txt");
        File tempFile = File.createTempFile("tempFile", ".txt");
        tempFile.deleteOnExit();

        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }

            String pattern = "dear";

            List<Long> expected = new ArrayList<>();
            expected.add(274L);
            expected.add(424L);

            List<Long> results = findSubstringInFile(tempFile.getPath(), pattern);
            assertFalse(results.isEmpty());
            assertEquals(expected, results);

        } catch (IOException e) {
            throw new IOException("Error reading from file", e);
        }
    }

    @Test
    public void testLargeFile() throws Exception {
        File tempFile = File.createTempFile("largeTempFile", ".txt");
        long targetSize = 1L * 1024 * 1024 * 1024;
        String content = "The wondrous moment of our meeting.\n";
        String pattern = "lol";
        long currentSize = 0L;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            StringBuilder sb = new StringBuilder();

            writer.write("Hello lol name lol");
            while (currentSize < targetSize) {
                sb.append(content);
                if (sb.length() >= 10 * 1024 * 1024) {
                    writer.write(sb.toString());
                    currentSize += sb.length();
                    sb.setLength(0);
                }
            }
            writer.write(sb.toString());
            currentSize += sb.length();
        } catch (IOException e) {
            throw new IOException("Error reading from file", e);
        }

        tempFile.deleteOnExit();

        List<Long> results = findSubstringInFile(tempFile.getPath(), pattern);
        long expectedOccurrences = 2L;

        assertFalse(results.isEmpty());
        assertEquals(expectedOccurrences, results.size());
    }
}