package ru.nsu.basargina;

import static ru.nsu.basargina.OrderStatus.*;

/**
 * Class for describing an order.
 */
public class Order {
    private final int OrderId;
    private OrderStatus orderStatus;

    /**
     * Create order with given id and status CREATED.
     *
     * @param orderId - order id
     */
    public Order(int orderId) {
        this.OrderId = orderId;
        this.orderStatus = CREATED;
    }

    /**
     * Getter for order id.
     *
     * @return - order id
     */
    public int getOrderId() {
        return OrderId;
    }

    /**
     * Getter for order status.
     *
     * @return - order status
     */
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    /**
     * Setter for order status.
     *
     * @param orderStatus - order status
     */
    public synchronized void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * Converts order to string.
     *
     * @return string containing info about order
     */
    @Override
    public String toString() {
        return "[Order " + getOrderId() + " " + getOrderStatus() + "]";
    }
}
