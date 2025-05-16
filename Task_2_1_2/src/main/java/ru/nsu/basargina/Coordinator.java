package ru.nsu.basargina;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/**
 * The main node of the cluster.
 * Work process:
 * - Listens to the specified UDP port and responds to "DISCOVER_PRIME" requests
 * ‑ Accepts TCP connections from workers on the specified port
 * - Divides the source array into equal fragments and sends it to the workers
 * - Collects responses and as soon as false is received,
 * sends SHUTDOWN and shuts down.
 */
public class Coordinator {
    private final int tcpPort;
    private final int udpPort;
    private final int expectedWorkers;
    private final Path inputFile;
    private final List<WorkerHandler> workers = Collections.synchronizedList(new ArrayList<>());
    private final ExecutorService clientPool = Executors.newCachedThreadPool();
    // Queue for the results from the workers
    private static final BlockingQueue<Boolean> resultQueue = new LinkedBlockingQueue<>();
    // Flag that at least one worker has already found a composite number.
    private static final AtomicBoolean compositeFound = new AtomicBoolean(false);
    private final int[] data;

    /**
     * Create coordinator with given cmd parameters.
     *
     * @param tcpPort tcp port for message exchanging
     * @param udpPort udp port to find workers using broadcast
     * @param expectedWorkers expected amount of connected workers
     * @param inputFile input file with numbers
     */
    private Coordinator(int tcpPort, int udpPort, int expectedWorkers, Path inputFile) {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.expectedWorkers = expectedWorkers;
        this.inputFile = inputFile;
        this.data = readInputFile(inputFile);
    }

    /**
     * Main method. Parses CLI arguments and launches the coordinator.
     *
     * @param args cli arguments.
     */
    public static void main(String[] args) {
        // default parameters if not set
        int tcp = 9000;
        int udp = 9999;
        int exp = 2;
        Path inputFile = Paths.get("input.txt");
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--port" -> tcp = Integer.parseInt(args[++i]);
                case "--udp" -> udp = Integer.parseInt(args[++i]);
                case "--expected" -> exp = Integer.parseInt(args[++i]);
                case "--input" -> inputFile = Paths.get(args[++i]);
            }
        }

        if (!Files.exists(inputFile)) {
            System.err.println("Input file not found: " + inputFile.toAbsolutePath());
            System.exit(1);
        }

        Coordinator coord = new Coordinator(tcp, udp, exp, inputFile);
        coord.run();
    }

    private static int[] readInputFile(Path path) {
        try (IntStream ints = Files.lines(path)
                .filter(line -> !line.isBlank())
                .mapToInt(Integer::parseInt)) {
            return ints.toArray();
        } catch (IOException e) {
            System.out.println("Something went wrong while reading from file.");
        }
        return new int[0];
    }

    /**
     * Method for running coordinator.
     * - Starts UDP discovery and TCP sockets,
     * - Waits for the required number of workers,
     * - Distributes tasks and collects responses.
     */
    private void run() {
        System.out.printf("Coordinator ports: TCP=%d, UDP=%d. Is waiting %d worker(s)...%n", tcpPort, udpPort, expectedWorkers);

        // Start udp discovering
        Thread udpThread = new Thread(this::udpDiscoveryLoop, "UDP-Discovery");
        udpThread.setDaemon(true);
        udpThread.start();

        // Start tcp server socket
        try (ServerSocket serverSocket = new ServerSocket(tcpPort)) {
            serverSocket.setReuseAddress(true);
            while (workers.size() < expectedWorkers) {
                Socket socket = serverSocket.accept();
                WorkerHandler wh = new WorkerHandler(socket);
                workers.add(wh);
                clientPool.submit(wh);
            }
            System.out.println("All workers have connected. Distributing tasks...");
            distributeTasks();
            waitTasksCompletion();
        } catch (IOException e) {
            System.out.println("Something went wrong while opening tcp socket.");
        }
        clientPool.shutdown();
    }

    /**
     * Method for detecting workers.
     * - Listens to the UDP port.
     * - If it receives "DISCOVER_PRIME", it responds to the sender with the message
     */
    private void udpDiscoveryLoop() {
        try (DatagramSocket udpSocket = new DatagramSocket(udpPort)) {
            udpSocket.setSoTimeout(0); // Enable blocking mode
            byte[] buf = new byte[256];
            while (true) {
                // Create packet for receiving messages
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                udpSocket.receive(packet);
                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8).trim();
                if ("DISCOVER_PRIME".equals(msg)) { // worker has been found
                    InetAddress requester = packet.getAddress();
                    int requesterPort = packet.getPort();

                    // create and send reply from coordinator
                    String localIp = InetAddress.getLocalHost().getHostAddress();
                    String reply = "COORDINATOR " + localIp + " " + tcpPort;
                    byte[] out = reply.getBytes(StandardCharsets.UTF_8);
                    udpSocket.send(new DatagramPacket(out, out.length, requester, requesterPort));
                    System.out.printf("[UDP] Replying to %s:%d – %s%n", requester.getHostAddress(), requesterPort, reply);
                }
            }
        } catch (IOException e) {
            System.out.println("Something went wrong while udp discovering.");
        }
    }

    /**
     * Divides the array into equal fragments and sends them to the workers.
     */
    private void distributeTasks() {
        // dividing array by the number of workers
        int workersCnt = workers.size();
        int chunk = (data.length + workersCnt - 1) / workersCnt;

        for (int i = 0; i < workersCnt; i++) {
            int start = i * chunk;
            int end = Math.min(data.length, start + chunk);
            if (start >= end) break;

            int[] fragment = Arrays.copyOfRange(data, start, end);
            UUID taskId = UUID.randomUUID();
            workers.get(i).sendTask(taskId, fragment);
        }
    }

    /**
     * Collects responses and as soon as false is received,
     * sends SHUTDOWN and shuts down.
     */
    private void waitTasksCompletion() {
        try {
            boolean allPrime = true;
            int received = 0;
            while (received < workers.size()) {
                boolean partPrime = resultQueue.take();
                received++;
                if (!partPrime) {
                    allPrime = false;
                    compositeFound.set(true);
                    break;
                }
            }
            System.out.printf("Result: array %s contains only prime numbers%n", allPrime ? "" : "doesn't");
        } catch (InterruptedException e) {
            System.out.println("Waiting for tasks completion has been interrupted.");
            Thread.currentThread().interrupt();
        }

        workers.forEach(WorkerHandler::sendShutdown);
    }

    /**
     * Class for serving one worker:
     * - receives HELLO
     * - sends tasks
     * - reads RESULT/ABORT_FOUND lines
     * - sets the result.
     */
    private static class WorkerHandler implements Runnable {
        private final Socket socket;
        private final BufferedReader in;
        private final PrintWriter out;
        //Flag that a composite number has already been found
        private final AtomicBoolean finished = new AtomicBoolean(false);

        /**
         * Create worker handler.
         *
         * @param socket socket accepted from ServerSocket.accept TCP socket
         * @throws IOException if something wrong with the socket
         */
        WorkerHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                    true);
        }

        /**
         * Sends the task to the worker as a string.
         * 
         * @param taskId task id
         * @param fragment array fragment
         */
        void sendTask(UUID taskId, int[] fragment) {
            // Format: TASK taskId n value1 value2 ... valueN\n
            StringBuilder sb = new StringBuilder();
            sb.append("TASK ").append(taskId).append(' ').append(fragment.length);
            for (int v : fragment) sb.append(' ').append(v);

            out.println(sb);
            System.out.println("[TCP] - Worker(" + socket.getRemoteSocketAddress() + "): " + sb);
        }

        /**
         * Sends shutdown to worker.
         */
        void sendShutdown() {
            out.println("SHUTDOWN");
        }

        /**
         * The main working process.
         * - Reads HELLO = responds with ACK
         * - Waits for RESULT/ABORT_FOUND and puts the result in the queue
         */
        @Override
        public void run() {
            try (socket; in; out) {
                // Hello from coordinator
                String hello = in.readLine();
                System.out.println("[TCP] HELLO from " + socket.getRemoteSocketAddress() + ": " + hello);
                out.println("ACK");

                String line;
                while ((line = in.readLine()) != null) {
                    if (finished.get()) {
                        break;
                    }
                    if (line.startsWith("RESULT")) {
                        // RESULT taskId true|false
                        boolean partPrime = Boolean.parseBoolean(line.split(" ")[2]);
                        resultQueue.offer(partPrime);
                        finished.set(true);
                        if (!partPrime) {
                            compositeFound.set(true);
                        }
                        break;
                    }
                    else if (line.startsWith("ABORT_FOUND")) {
                        // ABORT_FOUND taskId offendingNumber
                        resultQueue.offer(false);
                        finished.set(true);
                        compositeFound.set(true);
                        break;
                    }
                }
            } catch (IOException e) {
                System.out.println("Something went wrong while reading/printing socket.");
            }
        }
    }
}