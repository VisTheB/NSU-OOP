package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.nsu.basargina.SubstringSearcher.findSubstringInFile;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Class with tests for substring search.
 */
public class SubstringSearcherTest {

    @Test
    public void testFindSubstringSuccess() throws Exception {
        String content = "This is a test string. test KMP search.";
        InputStream inputStream =
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        String pattern = "test";

        List<Long> results = findSubstringInFile(inputStream, pattern);
        List<Long> expected = new ArrayList<>();
        expected.add(10L);
        expected.add(23L);

        assertFalse(results.isEmpty());
        assertEquals(expected, results);
    }

    @Test
    public void testFindSubstringRussian() throws Exception {
        String content = "Это тест для поиска подстроки. Начать тест.";
        InputStream inputStream =
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        String pattern = "тест";

        List<Long> results = findSubstringInFile(inputStream, pattern);
        List<Long> expected = new ArrayList<>();
        expected.add(4L);
        expected.add(38L);

        assertFalse(results.isEmpty());
        assertEquals(expected, results);
    }

    @Test
    public void testFindSubstringFail() throws Exception {
        String content = "This is some lol text.";
        InputStream inputStream =
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        String pattern = "lollol";

        List<Long> results = findSubstringInFile(inputStream, pattern);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testFindMultipleOccurrences() throws Exception {
        String content = "abcabcabcaaaaaaaaabbbbbcc";
        InputStream inputStream =
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        String pattern = "abc";

        List<Long> expected = new ArrayList<>();
        expected.add(0L);
        expected.add(3L);
        expected.add(6L);

        List<Long> results = findSubstringInFile(inputStream, pattern);
        assertFalse(results.isEmpty());
        assertEquals(expected, results);
    }

    @Test
    public void testEmptyText() throws Exception {
        String content = "";
        InputStream inputStream =
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        String pattern = "hello";

        List<Long> results = findSubstringInFile(inputStream, pattern);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testEmptyPattern() throws Exception {
        String content = "Some random random ??.";
        InputStream inputStream =
                new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        String pattern = "";

        List<Long> results = findSubstringInFile(inputStream, pattern);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSeveralLinesInText() throws Exception {
        InputStream in = getClass().getResourceAsStream("/test.txt");

        String pattern = "dear";

        List<Long> expected = new ArrayList<>();
        expected.add(267L);
        expected.add(413L);

        List<Long> results = findSubstringInFile(in, pattern);
        assertFalse(results.isEmpty());
        assertEquals(expected, results);
    }

    @Test
    public void testLargeFile() throws Exception {
        File tempFile = File.createTempFile("largeTempFile", ".txt");
        long targetSize = 9L * 1024 * 1024 * 1024;
        String content = "The wondrous moment of our meeting.\n";
        String pattern = "lol";
        long currentSize = 0L;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            StringBuilder sb = new StringBuilder();

            writer.write("Hello lol name lol");
            while (currentSize < targetSize) {
                sb.append(content);
                currentSize += content.length();
                if (sb.length() >= 10 * 1024 * 1024) {
                    writer.write(sb.toString());
                    currentSize += content.length();
                    sb.setLength(0);
                }
            }
            writer.write(sb.toString());
            currentSize += sb.length();
        } catch (IOException e) {
            throw new IOException("Error reading from file", e);
        }

        tempFile.deleteOnExit();

        try (FileInputStream fis = new FileInputStream(tempFile)) {
            List<Long> results = SubstringSearcher.findSubstringInFile(fis, pattern);
            long expectedOccurrences = 2L;

            assertFalse(results.isEmpty());
            assertEquals(expectedOccurrences, results.size());
        }
    }
}