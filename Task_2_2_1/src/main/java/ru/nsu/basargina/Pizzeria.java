package ru.nsu.basargina;

import java.util.ArrayList;
import java.util.List;

/**
 * Pizzeria class for managing orders baking/delivering and closing pizzeria.
 */
public class Pizzeria {
    final OrderQueue orderQueue;
    final Warehouse warehouse;

    private final List<Thread> bakerThreads = new ArrayList<>();
    private final List<Thread> courierThreads = new ArrayList<>();
    private final List<Baker> bakers = new ArrayList<>();
    private final List<Courier> couriers = new ArrayList<>();

    private Thread generatorThread;
    private OrderGenerator orderGenerator;
    private final long generationInterval;

    /**
     * Create warehouse, orders queue, bakers and couriers.
     *
     * @param config - pizzeria config
     */
    public Pizzeria(PizzeriaConfig config) {
        this.orderQueue = new OrderQueue();
        this.warehouse = new Warehouse(config.warehouseCapacity);

        // Create bakers
        for (PizzeriaConfig.Baker b : config.bakers) {
            Baker baker = new Baker(
                    b.bakerName,
                    b.cookingTimePerPizza,
                    orderQueue,
                    warehouse
            );
            bakers.add(baker);
        }

        // Create couriers
        for (PizzeriaConfig.Courier c : config.couriers) {
            Courier courier = new Courier(
                    c.courierName,
                    c.trunkCapacity,
                    warehouse
            );
            couriers.add(courier);
        }

        this.generationInterval = config.generationInterval;
        this.orderGenerator = new OrderGenerator(orderQueue, generationInterval);
    }

    /**
     * Start all threads.
     */
    public void start() {
        for (Baker baker : bakers) {
            Thread t = new Thread(baker, "BakerThread-" + baker.hashCode());
            bakerThreads.add(t);
            t.start();
        }

        for (Courier courier : couriers) {
            Thread t = new Thread(courier, "CourierThread-" + courier.hashCode());
            courierThreads.add(t);
            t.start();
        }

        generatorThread = new Thread(orderGenerator, "OrderGenThread");
        generatorThread.start();
    }

    /**
     * Stop accepting orders, wait until all threads finish.
     */
    public void stopPizzeria() {
        orderGenerator.stopGenerating();
        orderQueue.setStopOrderQueue(true);
        warehouse.setStopWarehouse(true);

        for (Thread t : bakerThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        for (Thread t : courierThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Pizzeria stopped.");
    }

    /**
     * Getter for orders count.
     *
     * @return orders count
     */
    public int getOrderCounter() {
        return orderGenerator.getOrderCounter();
    }
}
