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
    public String toString() {
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
     * @return - value of the variable or error if variable is missing
     */
    public double eval(Map<String, Double> variables) {
        if (variables.containsKey(name)) {
            return variables.get(name);
        } else {
            throw new RuntimeException("You haven't assigned this variable: " + name);
        }
    }
}
