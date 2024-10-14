package ru.nsu.basargina.data;

import java.util.Map;

/**
 * An abstract class with methods for expression.
 */
public abstract class Expression {
    /**
     * Printing an expression to a file/console.
     *
     */
    @Override
    public abstract String toString();

    /**
     * Differentiating expression by a given variable.
     *
     * @param variable - variable for differentiating
     * @return derivative
     */
    public abstract Expression derivative(String variable);

    /**
     * Evaluation of the expression.
     *
     * @param variables - variables being signified
     * @return meaning of the expression
     */
    public abstract double eval(Map<String, Double> variables);
}
