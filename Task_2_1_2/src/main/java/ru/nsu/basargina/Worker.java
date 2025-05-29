package ru.nsu.basargina;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
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
 * java -cp src/main/java ru.nsu.basargina.Worker --upd 9999
 */
public class Worker {
    private static int DISCOVERY_PORT;
    private static final String DISCOVER_MSG = "DISCOVER_PRIME";
    private static final int DISCOVERY_ATTEMPTS = 5;
    private static final int UDP_TIMEOUT_MS = 2000;
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final String workerId = UUID.randomUUID().toString();
    private final byte[] udpBuffer = DISCOVER_MSG.getBytes(StandardCharsets.UTF_8);

    /**
     * Main method. Launches worker with udp port from cli args.
     *
     * @param args cli arguments
     */
    public static void main(String[] args) {
        try {
            DISCOVERY_PORT = 9999;
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--udp")) {
                    DISCOVERY_PORT = Integer.parseInt(args[++i]);
                } else {
                    throw new IllegalArgumentException("Unknown arg: " + args[i]);
                }
            }
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
                System.out.println("SHUTDOWN received. Bye! " + workerId);
                break;
            } else {
                System.err.println("Unknown message: " + line);
            }
        }
        socket.close();
    }

    /**
     * Process string "TASK taskId n1 n2 … nk".
     * Send RESULT after processing.
     *
     * @param msg string with task
     */
    private void handleTask(String msg) {
        PrimeDetector pd = new PrimeDetector(msg);
        String pdResult = pd.findComposite();
        out.println(pdResult);
    }
}