package ru.nsu.basargina;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Class for thread-safe order queue without using BlockingQueue.
 */
public class OrderQueue {
    private final Queue<Order> queue = new LinkedList<>();

    /**
     * Add an order to the order queue and notify waiting bakers.
     *
     * @param order - order to be added
     */
    public synchronized void put(Order order) {
        queue.add(order);
        notifyAll(); // notify bakers that order has appeared
    }

    /**
     * Take an order from the queue if it's not empty.
     *
     * @return order
     * @throws InterruptedException if thread was interrupted
     */
    public synchronized Order take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        return queue.poll();
    }

    /**
     * Synchronized version of checking if queue is empty.
     *
     * @return true if queue is empty
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Synchronized version of getting size of the queue.
     *
     * @return queue size
     */
    public synchronized int size() {
        return queue.size();
    }
}
