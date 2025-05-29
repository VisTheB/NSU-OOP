package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class with tests.
 */
class CoordinatorTest {
    private PrintStream original;
    private ByteArrayOutputStream buf;

    @BeforeEach
    void setup() throws IOException {
        original = System.out;
        buf = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buf));
    }

    @AfterEach
    void teardown() {
        System.setOut(original);
        System.out.println(buf.toString());
    }

    @Test
    void testAllPrimes() throws Exception {
        int tcpPort = 9000;
        int udpPort = 9999;
        runTest(
                "src/test/resources/input.txt",
                "contains only prime numbers",
                tcpPort,
                udpPort
        );
    }

    @Test
    void testHasComposite() throws Exception {
        int tcpPort = 9001;
        int udpPort = 9998;
        runTest(
                "src/test/resources/input2.txt",
                "doesn't contain only",
                tcpPort,
                udpPort
        );
    }

    /**
     * Util method for running tests with different parameters.
     * 
     * @param inputPath path to input file
     * @param expected expected result
     * @param tcpPort coordinator tcp port
     * @param udpPort udp port
     * @throws InterruptedException if thread was interrupted
     */
    private void runTest(String inputPath, String expected, int tcpPort, int udpPort)
            throws InterruptedException {
        Path file = Paths.get(inputPath);

        Thread coord = new Thread(() -> {
            try {
                Coordinator.main(new String[]{
                        "--port", String.valueOf(tcpPort),
                        "--udp", String.valueOf(udpPort),
                        "--expected", "2",
                        "--input", String.valueOf(file)
                });
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });
        coord.start();

        Thread.sleep(500);

        Runnable workerTask = () -> {
            try {
                Worker.main(new String[]{"--udp",
                        String.valueOf(udpPort)});
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        };
        Thread w1 = new Thread(workerTask, "Worker-1");
        Thread w2 = new Thread(workerTask, "Worker-2");
        w1.start();
        w2.start();

        coord.join();

        String log = buf.toString();
        assertTrue(log.contains(expected));
    }
}