package ru.nsu.basargina.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.basargina.data.Expression;
import ru.nsu.basargina.data.Number;
import ru.nsu.basargina.data.Variable;

/**
 * Class with tests for Add class.
 */
class AddTest {
    @Test
    void testPrint() {
        Expression left = new Number(666);
        Expression right = new Variable("a");
        Add sum = new Add(left, right);

        String output = sum.print();

        assertEquals("(666.0+a)", output);
    }

    @Test
    void testDerivative() {
        Expression left = new Variable("f");
        Expression right = new Number(154);
        Add sum = new Add(left, right);

        Expression derivative = sum.derivative("f");
        String output = derivative.print();

        assertEquals("(1.0+0.0)", output);
    }

    @Test
    void testEval() {
        Expression left = new Variable("x");
        Expression right = new Number(65);
        Add sum = new Add(left, right);

        Map<String, Double> vars = new HashMap<>();
        vars.put("x", 40.0);

        double result = sum.eval(vars);

        assertEquals(105.0, result);
    }
}