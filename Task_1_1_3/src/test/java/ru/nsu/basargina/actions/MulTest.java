package ru.nsu.basargina.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.basargina.data.Expression;
import ru.nsu.basargina.data.Number;
import ru.nsu.basargina.data.Variable;

/**
 * Class with tests for Mul class.
 */
class MulTest {
    @Test
    void testPrint() {
        Expression left = new Variable("v");
        Expression right = new Number(678);
        Mul multiplication = new Mul(left, right);

        String output = multiplication.toString();

        assertEquals("(v*678.0)", output);
    }

    @Test
    void testDerivative() {
        Expression left = new Variable("l");
        Expression right = new Number(202);
        Mul multiplication = new Mul(left, right);

        Expression derivative = multiplication.derivative("l");
        String output = derivative.toString();

        assertEquals("((1.0*202.0)+(l*0.0))", output);
    }

    @Test
    void testEval() {
        Expression left = new Variable("q");
        Expression right = new Number(222);
        Mul multiplication = new Mul(left, right);

        Map<String, Double> vars = new HashMap<>();
        vars.put("q", 4.0);

        double result = multiplication.eval(vars);

        assertEquals(888.0, result);
    }
}