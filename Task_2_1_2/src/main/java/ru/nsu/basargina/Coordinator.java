package ru.nsu.basargina;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * The main node of the cluster.
 * Work process:
 * - Listens to the specified UDP port and responds to "DISCOVER_PRIME" requests
 * ‑ Accepts TCP connections from workers on the specified port
 * - Divides the source array into equal fragments and sends it to the workers
 * - Collects responses and as soon as false is received,
 * sends SHUTDOWN and shuts down.
 * Usage:
 * javac src/main/java/ru/nsu/basargina/Coordinator.java
 * java -cp src/main/java ru.nsu.basargina.Coordinator --port 9000 --udp 9999 --expected 3
 * --input ./input.txt
 */
public class Coordinator {
    private final int tcpPort;
    private final int udpPort;
    private final int expectedWorkers;
    private final Path inputFile;
    private final List<WorkerHandler> workers = Collections.synchronizedList(new ArrayList<>());
    private final ExecutorService clientPool = Executors.newCachedThreadPool();
    // Queue for tasks for workers
    private static final BlockingQueue<TaskDescriptor> pendingTasks = new LinkedBlockingQueue<>();
    // Flag that composite number has been found
    private static final AtomicBoolean compositeFound = new AtomicBoolean(false);
    private static final AtomicInteger remainingTasks = new AtomicInteger(0);
    private static final Object finishLock = new Object();
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
                default -> throw new IllegalArgumentException("Unknown arg: " + args[i]);
            }
        }

        if (!Files.exists(inputFile)) {
            System.err.println("Input file not found: " + inputFile.toAbsolutePath());
            System.exit(1);
        }

        Coordinator coord = new Coordinator(tcp, udp, exp, inputFile);
        coord.run();
    }

    /**
     * Parses input file into array of numbers.
     *
     * @param path path to input file
     * @return array with numbers from file
     */
    private static int[] readInputFile(Path path) {
        try (IntStream ints = Files.lines(path)
                .filter(line -> !line.isBlank())
                .mapToInt(Integer::parseInt)) {
            return ints.toArray();
        } catch (IOException e) {
            System.err.println("Something went wrong while reading from file." + e.getMessage());
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
        System.out.printf("Coordinator ports: TCP=%d, UDP=%d. Is waiting %d worker(s)...%n",
                tcpPort, udpPort,
                expectedWorkers);

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
        } catch (IOException e) {
            System.err.println("Something went wrong while opening tcp socket." + e.getMessage());
        }
        distributeTasks();

        waitTasksCompletion();

        workers.forEach(WorkerHandler::sendShutdown);
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
                String msg = new String(packet.getData(), packet.getOffset(), packet.getLength(), 
                        StandardCharsets.UTF_8).trim();
                if ("DISCOVER_PRIME".equals(msg)) { // worker has been found
                    InetAddress requester = packet.getAddress();
                    int requesterPort = packet.getPort();

                    // create and send response from coordinator
                    String localIp = InetAddress.getLocalHost().getHostAddress();
                    String response = "COORDINATOR " + localIp + " " + tcpPort;
                    byte[] out = response.getBytes(StandardCharsets.UTF_8);
                    udpSocket.send(new DatagramPacket(out, out.length, requester, requesterPort));
                    System.out.printf("[UDP] Replying to %s:%d - %s%n", requester.getHostAddress(),
                            requesterPort,
                            response);
                }
            }
        } catch (IOException e) {
            System.err.println("Something went wrong while udp discovering." + e.getMessage());
        }
    }

    /**
     * Divides the array into equal fragments and adds them to tasks queue.
     */
    private void distributeTasks() {
        // dividing array by the number of workers
        int workersCnt = workers.size();
        remainingTasks.set(workersCnt);
        int chunk = (data.length + workersCnt - 1) / workersCnt;

        for (int i = 0; i < workersCnt; i++) {
            int start = i * chunk;
            int end = Math.min(data.length, start + chunk);
            if (start >= end) {
                break;
            }

            int[] fragment = Arrays.copyOfRange(data, start, end);
            UUID taskId = UUID.randomUUID();
            TaskDescriptor newTask = new TaskDescriptor(taskId, fragment);
            pendingTasks.add(newTask);
        }
    }

    /**
     * Waits until composite is found or there aren't any tasks left.
     */
    private void waitTasksCompletion() {
        synchronized (finishLock) {
            while (!compositeFound.get() && remainingTasks.get() > 0) {
                try {
                    finishLock.wait();
                } catch (InterruptedException e) {
                    System.err.println("Waiting for tasks completion has been interrupted.");
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (compositeFound.get()) {
            System.out.println("Result: array doesn't contain only prime numbers");
        } else {
            System.out.println("Result: array contains only prime numbers");
        }
    }

    /**
     * Record class for creating tasks.
     *
     * @param id task id
     * @param numbers array of numbers to check
     */
    private record TaskDescriptor(UUID id, int[] numbers) {
    }

    /**
     * Class for serving one worker:
     * - receives HELLO
     * - sends tasks
     * - reads RESULT lines
     * - sets the result.
     */
    private static class WorkerHandler implements Runnable {
        private final Socket socket;
        private final BufferedReader in;
        private final PrintWriter out;
        private TaskDescriptor currentTask;

        /**
         * Create worker handler.
         *
         * @param socket socket accepted from ServerSocket.accept TCP socket
         * @throws IOException if something wrong with the socket
         */
        WorkerHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                    StandardCharsets.UTF_8));
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),
                    StandardCharsets.UTF_8),
                    true);
        }

        /**
         * Sends the task to the worker as a string.
         *
         * @param taskId task id
         * @param fragment array fragment
         */
        void sendTask(UUID taskId, int[] fragment) {
            // Format: TASK taskId value1 value2 ... valueN\n
            StringBuilder sb = new StringBuilder();
            sb.append("TASK ").append(taskId);
            for (int v : fragment) {
                sb.append(' ').append(v);
            }

            out.println(sb);
            System.out.println("[TCP] Sending task to Worker(" + socket.getRemoteSocketAddress()
                    + "): " + sb);
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
         * - Waits for the task and then compute it
         * - If RESULT the notifies coordinator and sets flag
         * - If worker crashed his task will be put back to the tasks queue.
         */
        @Override
        public void run() {
            try (socket; in; out) {
                // Hello from worker
                String hello = in.readLine();
                System.out.println("[TCP] HELLO from " + socket.getRemoteSocketAddress()
                        + ": " + hello);
                out.println("ACK");

                while (!compositeFound.get()) {
                    currentTask = pendingTasks.take();

                    sendTask(currentTask.id, currentTask.numbers);

                    String line = in.readLine();
                    System.out.println(line);
                    if (line == null) {
                        throw new IOException("Connection closed");
                    }

                    if (line.startsWith("RESULT")) {
                        boolean partPrime = Boolean.parseBoolean(line.split(" ")[2]);
                        if (!partPrime) {
                            compositeFound.set(true);
                        }
                        remainingTasks.decrementAndGet();
                        // Wake up coordinator
                        synchronized (finishLock) {
                            finishLock.notifyAll();
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
                System.err.println(
                        "Worker " + socket.getRemoteSocketAddress()
                                + " failed on task "
                                + (currentTask != null ? currentTask.id : "none")
                                + "; requeueing...");
                if (currentTask != null && !compositeFound.get()) {
                    pendingTasks.add(currentTask);
                    synchronized (finishLock) {
                        finishLock.notifyAll();
                    }
                }
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Something went wrong while closing worker socket.");
                }
            }
        }
    }
}