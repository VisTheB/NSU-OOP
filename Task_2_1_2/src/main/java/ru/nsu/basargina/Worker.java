package ru.nsu.basargina;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Worker node of the cluster.
 * Work process:
 * - Sends UDP broadcast messages to find coordinator
 * - Opens TCP connection with coordinator for msgs exchanging
 * - Receives a fragment of the array from the coordinator
 * - Checks each number for simplicity and immediately reports
 * if there is at least one composite one.
 * Usage:
 * javac src/main/java/ru/nsu/basargina/Worker.java
 * java -cp src/main/java ru.nsu.basargina.Worker
 */
public class Worker {
    private static final int DISCOVERY_PORT = 9999;
    private static final String DISCOVER_MSG = "DISCOVER_PRIME";
    private static final int DISCOVERY_ATTEMPTS = 5;
    private static final int UDP_TIMEOUT_MS = 2000;
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final String workerId = UUID.randomUUID().toString();
    private final byte[] udpBuffer = DISCOVER_MSG.getBytes(StandardCharsets.UTF_8);

    /**
     * Main method. Launches worker with cli args.
     *
     * @param args cli arguments
     */
    public static void main(String[] args) {
        try {
            new Worker().start();
        } catch (Exception e) {
            System.err.println("Worker terminated: " + e.getMessage());
        }
    }

    /**
     * Method for running worker.
     * - Finds coordinator using udp broadcast
     * - Opens tcp connection with coordinator
     * - Runs until SHUTDOWN.
     *
     * @throws IOException if any network error
     */
    private void start() throws IOException {
        InetSocketAddress coord = discoverCoord();
        openTcpConnection(coord);
        eventLoop();
    }

    /**
     * UDP coordinator discovering:
     * - Sends several broadcast packets
     * - Receives response from coordinator.
     *
     * @return coordinator's ip and port
     * @throws IllegalStateException if there is no response after N attempts
     */
    private InetSocketAddress discoverCoord() {
        System.out.println("UDP discovery started...");
        try (DatagramSocket udpSocket = new DatagramSocket()) {
            udpSocket.setBroadcast(true);
            udpSocket.setSoTimeout(UDP_TIMEOUT_MS);

            DatagramPacket packet = new DatagramPacket(
                    udpBuffer, udpBuffer.length,
                    InetAddress.getByName("255.255.255.255"), DISCOVERY_PORT);

            byte[] respBuf = new byte[128];
            DatagramPacket response = new DatagramPacket(respBuf, respBuf.length);

            for (int attempt = 1; attempt <= DISCOVERY_ATTEMPTS; attempt++) {
                udpSocket.send(packet);
                try {
                    udpSocket.receive(response);
                    String msg = new String(
                            response.getData(), 0, response.getLength(), StandardCharsets.UTF_8);
                    if (msg.startsWith("COORDINATOR")) {
                        StringTokenizer st = new StringTokenizer(msg);
                        st.nextToken(); // skip keyword
                        String ip = st.nextToken();
                        int port = Integer.parseInt(st.nextToken());

                        System.out.printf("[UDP] Coordinator found at %s:%d%n", ip, port);

                        return new InetSocketAddress(ip, port);
                    }
                } catch (SocketTimeoutException e) {
                    System.out.printf("[UDP] No response (%d/%d)%n", attempt, DISCOVERY_ATTEMPTS);
                }
            }
        } catch (IOException e) {
            System.out.println("UDP discovery failed." + e.getMessage());
        }
        return null;
    }

    /**
     * - Open TCP connection with coordinator
     * - Send HELLO
     * - Wait for ACK.
     *
     * @param coord coordinator's ip and port
     * @throws IOException if handshake has failed
     */
    private void openTcpConnection(InetSocketAddress coord) throws IOException {
        socket = new Socket();
        socket.connect(coord);

        out = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream(), StandardCharsets.UTF_8), true);
        in  = new BufferedReader(new InputStreamReader(
                socket.getInputStream(), StandardCharsets.UTF_8));

        out.printf("HELLO %s%n", workerId);
        String ack = in.readLine();
        if (ack == null || !ack.startsWith("ACK")) {
            throw new IOException("Handshake failed: expected ACK, got " + ack);
        }
        System.out.println("Handshake complete.");
    }

    /**
     * The main cycle of receiving and processing commands from the coordinator.
     *
     * @throws IOException if socket errors
     */
    private void eventLoop() throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            if (line.startsWith("TASK")) {
                handleTask(line);
            } else if (line.startsWith("SHUTDOWN")) {
                System.out.println("SHUTDOWN received. Bye!");
                break;
            } else {
                System.err.println("Unknown message: " + line);
            }
        }
        socket.close();
    }

    /**
     * Process string "TASK taskId n1 n2 … nk".
     * If composite number has been found, break and send RESULT.
     *
     * @param msg string with task
     */
    private void handleTask(String msg) {
        StringTokenizer st = new StringTokenizer(msg);
        st.nextToken(); // "TASK"
        String taskId = st.nextToken();

        boolean allPrime = true;

        while (st.hasMoreTokens()) {
            int n = Integer.parseInt(st.nextToken());
            if (!isPrime(n)) {
                allPrime = false;
                break;
            }
        }

        if (allPrime) {
            out.printf("RESULT %s true%n", taskId);
        } else {
            out.printf("RESULT %s false%n", taskId);
        }
    }

    /**
     * Util method for checking prime numbers.
     *
     * @param n number to be checked
     * @return true if number is prime
     */
    private static boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        if (n == 2) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }
        for (int d = 3; d * d <= n; d += 2) {
            if (n % d == 0) return false;
        }
        return true;
    }
}