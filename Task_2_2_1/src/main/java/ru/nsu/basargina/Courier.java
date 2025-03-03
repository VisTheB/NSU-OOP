package ru.nsu.basargina;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static ru.nsu.basargina.OrderStatus.*;

/**
 * Class for courier with id and name who will take pizzas from warehouse
 * and deliver them for given period of time.
 */
public class Courier implements Runnable {
    private final int courierId;
    private final String courierName;
    private final int trunkCapacity;
    private final Warehouse warehouse;
    private AtomicBoolean running = new AtomicBoolean(true);

    /**
     * Create courier.
     *
     * @param courierId - courier id
     * @param courierName - courier name
     * @param trunkCapacity - courier's trunk capacity
     * @param warehouse - warehouse with pizzas
     */
    public Courier(int courierId, String courierName, int trunkCapacity, Warehouse warehouse) {
        this.courierId = courierId;
        this.courierName = courierName;
        this.trunkCapacity = trunkCapacity;
        this.warehouse = warehouse;
    }

    /**
     * Stop courier's thread
     */
    public void stopCourier() {
        running.set(false);
    }

    /**
     * Method for running courier thread.
     */
    @Override
    public void run() {
        while (running.get()) {
            try {
                // Take n pizzas form the warehouse
                List<Order> orders = warehouse.takePizzas(trunkCapacity);

                // Deliver orders
                for (Order order : orders) {
                    order.setOrderStatus(DELIVERING);
                    System.out.println(order.getOrderId() + "is DELIVERING by Courier " + courierName);
                }
                // Let delivery take 2 seconds
                Thread.sleep(2000);

                // Order has been delivered
                for (Order order : orders) {
                    order.setOrderStatus(DELIVERED);
                    System.out.println(order.getOrderId() + " DELIVERED");
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                //running.set(false);
            }
        }
        System.out.println("Courier " + courierName + " stopped.");
    }
}