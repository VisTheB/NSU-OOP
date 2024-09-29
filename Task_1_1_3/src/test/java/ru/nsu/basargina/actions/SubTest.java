package ru.nsu.basargina.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.basargina.data.Expression;
import ru.nsu.basargina.data.Number;
import ru.nsu.basargina.data.Variable;

/**
 * Class with tests for Sub class.
 */
class SubTest {
    @Test
    void testPrint() {
        Expression left = new Number(9999);
        Expression right = new Variable("i");
        Sub diff = new Sub(left, right);

        String output = diff.print();

        assertEquals("(9999.0-i)", output);
    }

    @Test
    void testDerivative() {
        Expression left = new Variable("b");
        Expression right = new Number(11111);
        Sub diff = new Sub(left, right);

        Expression derivative = diff.derivative("b");
        String output = derivative.print();

        assertEquals("(1.0-0.0)", output);
    }

    @Test
    void testEval() {
        Expression left = new Variable("m");
        Expression right = new Number(-108);
        Sub diff = new Sub(left, right);

        Map<String, Double> vars = new HashMap<>();
        vars.put("m", 10.0);

        double result = diff.eval(vars);

        assertEquals(118.0, result);
    }
}