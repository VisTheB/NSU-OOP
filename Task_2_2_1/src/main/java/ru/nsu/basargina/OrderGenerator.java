package ru.nsu.basargina;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class that generate orders anp put them to the queue.
 */
public class OrderGenerator implements Runnable {
    private final OrderQueue orderQueue;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private int orderCounter = 0;
    private final long generationInterval;

    /**
     * Create order generator with given order queue.
     *
     * @param orderQueue order queue
     */
    public OrderGenerator(OrderQueue orderQueue, long generationInterval) {
        this.orderQueue = orderQueue;
        this.generationInterval = generationInterval;
    }

    /**
     * Stop generating orders.
     */
    public void stopGenerating() {
        running.set(false);
    }

    /**
     * Getter for orders count.
     *
     * @return orders count
     */
    public int getOrderCounter() {
        return orderCounter;
    }

    /**
     * Method for running order generator thread.
     */
    @Override
    public void run() {
        while (running.get()) {
            try {
                Order order = new Order(++orderCounter);
                System.out.println(order);
                orderQueue.put(order);

                Thread.sleep(generationInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("OrderGenerator stopped.");
    }
}