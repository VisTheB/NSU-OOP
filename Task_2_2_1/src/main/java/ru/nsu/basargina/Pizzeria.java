package ru.nsu.basargina;

import java.util.ArrayList;
import java.util.List;

/**
 * Pizzeria class for managing orders baking/delivering and closing pizzeria.
 */
public class Pizzeria {
    private final int orderGenerationInterval = 500;
    private final int ordersCnt;

    final OrderQueue orderQueue;
    final Warehouse warehouse;

    private final List<Thread> bakerThreads = new ArrayList<>();
    private final List<Thread> courierThreads = new ArrayList<>();
    private final List<Baker> bakers = new ArrayList<>();
    private final List<Courier> couriers = new ArrayList<>();

    /**
     * Create warehouse, orders queue, bakers and couriers.
     *
     * @param config - pizzeria config
     */
    public Pizzeria(PizzeriaConfig config) {
        this.ordersCnt = config.getOrdersCnt();
        this.orderQueue = new OrderQueue();
        this.warehouse = new Warehouse(config.getWarehouseCapacity());

        // Create bakers
        for (PizzeriaConfig.Baker b : config.getBakers()) {
            Baker baker = new Baker(
                    b.getBakerName(),
                    b.getCookingTimePerPizza(),
                    orderQueue,
                    warehouse
            );
            bakers.add(baker);
        }

        // Create couriers
        for (PizzeriaConfig.Courier c : config.getCouriers()) {
            Courier courier = new Courier(
                    c.getCourierName(),
                    c.getTrunkCapacity(),
                    warehouse
            );
            couriers.add(courier);
        }
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

        // Generate orders
        for (int i = 0; i < ordersCnt; i++) {
            Order order = new Order(i + 1);
            System.out.println(order);
            orderQueue.put(order);

            try {
                Thread.sleep(orderGenerationInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Stop accepting orders, wait until all threads finish.
     */
    public void stopPizzeria() {
        // Wait until all orders are done and delivered
        while (!orderQueue.isEmpty() || warehouse.getCurrentCount() > 0) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Stop bakers and couriers threads
        for (Thread t : bakerThreads) {
            t.interrupt();
        }
        for (Thread t : courierThreads) {
            t.interrupt();
        }

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
}
