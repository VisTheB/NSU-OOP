package ru.nsu.basargina;

import java.util.concurrent.atomic.AtomicBoolean;

import static ru.nsu.basargina.OrderStatus.*;

/**
 * Class for baker with id and name who will bake pizza for given period of time.
 */
public class Baker implements Runnable {
    private final int bakerId;
    private final String bakerName;
    private final double cookingTimePerPizza;
    private final OrderQueue orderQueue;
    private final Warehouse warehouse;
    private AtomicBoolean running = new AtomicBoolean(true);

    /**
     * Create baker.
     *
     * @param bakerId - baker id
     * @param bakerName - baker name
     * @param cookingTimePerPizza - amount of seconds needed to cook one pizza
     * @param orderQueue - order queue
     * @param warehouse - warehouse with pizzas
     */
    public Baker(int bakerId, String bakerName, double cookingTimePerPizza,
                 OrderQueue orderQueue, Warehouse warehouse) {
        this.bakerId = bakerId;
        this.bakerName = bakerName;
        this.cookingTimePerPizza = cookingTimePerPizza;
        this.orderQueue = orderQueue;
        this.warehouse = warehouse;
    }

    /**
     * Stop baker thread.
     */
    public void stopBaker() {
        running.set(false);
    }

    /**
     * Method for running baker thread.
     */
    @Override
    public void run() {
        while (running.get()) {
            try {
                // Take order from order queue
                Order order = orderQueue.take();

                // Refresh status
                order.setOrderStatus(BAKING);
                System.out.println(order.getOrderId() + "is BAKING by Baker " + bakerName);

                // Baker bakes pizza
                long sleepTime = (long) (cookingTimePerPizza * 1000);
                Thread.sleep(sleepTime);

                // Put pizza to the warehouse when it's ready
                order.setOrderStatus(READY_FOR_DELIVERY);
                System.out.println(order.getOrderId() + " READY_FOR_DELIVERY");
                warehouse.putPizza(order);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // running.set(false);
            }
        }
        System.out.println("Baker " + bakerName + " stopped.");
    }
}