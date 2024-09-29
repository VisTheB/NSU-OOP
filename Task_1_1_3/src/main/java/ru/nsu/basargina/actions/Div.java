package ru.nsu.basargina.actions;

import java.util.Map;
import ru.nsu.basargina.data.Expression;

/**
 * Class that represents division operation.
 */
public class Div extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Creates left and right part of the expression with "/".
     *
     * @param left - left part
     * @param right - right part
     */
    public Div(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * String representation of the expression with '/'.
     *
     * @return string with '/'
     */
    public String print() {
        return "(" + left.print() + "/" + right.print() + ")";
    }

    /**
     * Differentiation of the left and right parts.
     *
     * @param variable - variable for differentiating
     * @return derivative by given variable
     */
    public Expression derivative(String variable) {
        // (u/v)' = (u'v - v'u) / vv
        return new Div(new Sub(new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable))),
                new Mul(right, right));
    }

    /**
     * Evaluates left and right part.
     *
     * @param variables - variables being signified
     * @return - result of the evaluation
     */
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) / right.eval(variables);
    }
}
