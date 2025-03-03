package ru.nsu.basargina;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for storing pizzas with limited capacity and waiting.
 */
public class Warehouse {
    private final int capacity;
    private final List<Order> storage;

    /**
     * Create warehouse with given capacity.
     *
     * @param capacity - storage capacity
     */
    public Warehouse(int capacity) {
        this.capacity = capacity;
        this.storage = new LinkedList<>();
    }

    /**
     * Baker puts pizza to the warehouse if it isn't full and notify waiting couriers.
     *
     * @param order - order to be added
     * @throws InterruptedException if thread was interrupted
     */
    public synchronized void putPizza(Order order) throws InterruptedException {
        while (storage.size() >= capacity) {
            wait();
        }
        storage.add(order);
        notifyAll(); // notify couriers that pizza has appeared
    }

    /**
     * Courier takes n pizzas from the warehouse if it isn't empty and notify waiting bakers.
     *
     * @param n - pizzas quantity
     * @return list of n pizzas
     * @throws InterruptedException if thread was interrupted
     */
    public synchronized List<Order> takePizzas(int n) throws InterruptedException {
        while (storage.isEmpty() && this.getCurrentCount() != n) {
            wait();  // wait until n pizzas occur in the storage
        }

        int pizzasToTake = Math.min(n, storage.size());
        List<Order> result = new LinkedList<>();
        for (int i = 0; i < pizzasToTake; i++) {
            result.add(storage.removeFirst());
        }

        notifyAll(); // notify baker if he is waiting for warehouse to be free
        return result;
    }

    /**
     * Synchronized version of getting current amount of pizzas in the warehouse.
     *
     * @return current amount of pizzas
     */
    public synchronized int getCurrentCount() {
        return storage.size();
    }
}
