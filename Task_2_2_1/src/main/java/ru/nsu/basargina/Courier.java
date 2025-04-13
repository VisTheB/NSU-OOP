package ru.nsu.basargina;

import java.util.List;

/**
 * Class for courier with id and name who will take pizzas from warehouse
 * and deliver them for given period of time.
 */
public class Courier implements Runnable {
    private final String courierName;
    private final int trunkCapacity;
    private final Warehouse warehouse;

    /**
     * Create courier.
     *
     * @param courierName - courier name
     * @param trunkCapacity - courier's trunk capacity
     * @param warehouse - warehouse with pizzas
     */
    public Courier(String courierName, int trunkCapacity, Warehouse warehouse) {
        this.courierName = courierName;
        this.trunkCapacity = trunkCapacity;
        this.warehouse = warehouse;
    }

    /**
     * Method for running courier thread.
     */
    @Override
    public void run() {
        Thread current = Thread.currentThread();
        while (!current.isInterrupted()) {
            try {
                // Take n pizzas form the warehouse
                List<Order> orders = warehouse.takePizzas(trunkCapacity);
                if (orders.isEmpty()) {
                    break;
                }

                // Deliver orders
                for (Order order : orders) {
                    order.setOrderStatus(OrderStatus.DELIVERING);
                    System.out.println(order);
                }
                // Let delivery take 2 seconds
                Thread.sleep(2000);

                // Order has been delivered
                for (Order order : orders) {
                    order.setOrderStatus(OrderStatus.DELIVERED);
                    System.out.println(order);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Courier " + courierName + " stopped.");
    }
}