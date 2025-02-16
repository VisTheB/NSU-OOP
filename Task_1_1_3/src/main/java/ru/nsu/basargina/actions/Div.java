package ru.nsu.basargina.actions;

import java.util.Map;
import ru.nsu.basargina.data.Expression;
import ru.nsu.basargina.data.Number;

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
    public String toString() {
        return "(" + left.toString() + "/" + right.toString() + ")";
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
        double rightPart = right.eval(variables);
        if (rightPart == 0.0) {
            throw new RuntimeException("Division by zero!");
        } else {
            return left.eval(variables) / rightPart;
        }
    }

    /**
     * Evaluates left and right part without variables.
     *
     * @return - result of the evaluation
     */
    public double eval() {
        double rightPart = right.eval();
        if (rightPart == 0.0) {
            throw new RuntimeException("Division by zero!");
        } else {
            return left.eval() / rightPart;
        }
    }

    /**
     * Simplification of a given division expression.
     *
     * @return simplified expression
     */
    public Expression simplify() {
        Expression simpleLeft = left.simplify();
        Expression simpleRight = right.simplify();

        // If left operand is 0, return 0
        if (simpleLeft instanceof Number && simpleLeft.eval() == 0) {
            return new Number(0);
        }

        // If right operand is 1, return left operand
        if (simpleRight instanceof Number && simpleRight.eval() == 1) {
            return simpleLeft;
        }

        return new Div(simpleLeft, simpleRight);
    }
}
