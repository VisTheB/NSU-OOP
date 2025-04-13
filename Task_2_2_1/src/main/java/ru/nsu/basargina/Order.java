package ru.nsu.basargina;

/**
 * Class for describing an order.
 */
public class Order {
    private final int orderId;
    private OrderStatus orderStatus;

    /**
     * Create order with given id and status CREATED.
     *
     * @param orderId - order id
     */
    public Order(int orderId) {
        this.orderId = orderId;
        this.orderStatus = OrderStatus.CREATED;
    }

    /**
     * Getter for order id.
     *
     * @return - order id
     */
    public int getorderId() {
        return orderId;
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
        return "[Order " + getorderId() + " " + getOrderStatus() + "]";
    }
}
