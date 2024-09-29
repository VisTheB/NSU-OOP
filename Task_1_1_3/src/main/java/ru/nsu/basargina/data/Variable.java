package ru.nsu.basargina.data;

import java.util.Map;

/**
 * Class that represents a variable.
 */
public class Variable extends Expression {
    private final String name;

    /**
     * Create variable.
     *
     * @param name - variable name
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Returns string representation of the variable.
     *
     * @return string variable
     */
    public String print() {
        return name;
    }

    /**
     * Differentiation.
     *
     * @param variable - variable for differentiating
     * @return derivative from the variable
     */
    public Expression derivative(String variable) {
        return new Number(name.equals(variable) ? 1 : 0);
    }

    /**
     * Assigning a value to a variable.
     *
     * @param variables - variables to be assigned
     * @return - value of the variable
     */
    public double eval(Map<String, Double> variables) {
        return variables.getOrDefault(name, 0.0);
    }
}
