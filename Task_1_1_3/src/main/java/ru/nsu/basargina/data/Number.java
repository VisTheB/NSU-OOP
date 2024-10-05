package ru.nsu.basargina.data;

import java.util.Map;

/**
 * Class that represents a number.
 */
public class Number extends Expression {
    private final double value;

    /**
     * Create a number.
     *
     * @param value - value of the number
     */
    public Number(double value) {
        this.value = value;
    }

    /**
     * Returns string representation of the number.
     *
     * @return string number
     */
    public String toString() {
        return String.valueOf(value);
    }

    /**
     * Differentiation.
     *
     * @param variable - variable for differentiating
     * @return derivative from the number
     */
    public Expression derivative(String variable) {
        return new Number(0);
    }

    /**
     * Returns value for the evaluation.
     *
     * @param variables - variables being signified
     * @return - number itself
     */
    public double eval(Map<String, Double> variables) {
        return value;
    }
}
