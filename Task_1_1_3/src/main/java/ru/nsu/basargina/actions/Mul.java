package ru.nsu.basargina.actions;

import java.util.Map;
import ru.nsu.basargina.data.Expression;
import ru.nsu.basargina.data.Number;

/**
 * Class that represents multiplication operation.
 */
public class Mul extends Expression {
    private final Expression left;
    private final Expression right;

    /**
     * Creates left and right part of the expression with "*".
     *
     * @param left - left part
     * @param right - right part
     */
    public Mul(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    /**
     * String representation of the expression with '*'.
     *
     * @return string with '*'
     */
    public String toString() {
        return "(" + left.toString() + "*" + right.toString() + ")";
    }

    /**
     * Differentiation of the left and right parts.
     *
     * @param variable - variable for differentiating
     * @return derivative by given variable
     */
    public Expression derivative(String variable) {
        // (u*v)' = u'*v + u*v'
        return new Add(new Mul(left.derivative(variable), right),
                new Mul(left, right.derivative(variable)));
    }

    /**
     * Evaluates left and right part.
     *
     * @param variables - variables being signified
     * @return - result of the evaluation
     */
    public double eval(Map<String, Double> variables) {
        return left.eval(variables) * right.eval(variables);
    }

    /**
     * Evaluates left and right part without variables.
     *
     * @return - result of the evaluation
    */
    public double eval() {
        return left.eval() * right.eval();
    }

    /**
     * Simplification of a given multiplication expression.
     *
     * @return - simplified expression
     */
    public Expression simplify() {
        Expression simpleLeft = left.simplify();
        Expression simpleRight = right.simplify();

        // If left operand is 0, return 0
        if (simpleLeft instanceof Number && simpleLeft.eval() == 0) {
            return new Number(0);
        }

        // If right operand is 0, return 0
        if (simpleRight instanceof Number && simpleRight.eval() == 0) {
            return new Number(0);
        }

        // If left operand is 1, return right operand
        if (simpleLeft instanceof Number && simpleLeft.eval() == 1) {
            return simpleRight;
        }

        // If right operand is 1, return left operand
        if (simpleRight instanceof Number && simpleRight.eval() == 1) {
            return simpleLeft;
        }

        return new Mul(simpleLeft, simpleRight);
    }
}
