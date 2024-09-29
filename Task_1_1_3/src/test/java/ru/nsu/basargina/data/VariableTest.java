package ru.nsu.basargina.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Class with tests for Variable class.
 */
class VariableTest {
    @Test
    void testPrint() {
        Expression variable = new Variable("z");

        String output = variable.print();

        assertEquals("z", output);
    }

    @Test
    void testDerivative() {
        Expression variable = new Variable("y");
        Expression derivative = variable.derivative("y");

        String output = derivative.print();

        assertEquals("1.0", output);
    }

    @Test
    void testEval() {
        Expression variable = new Variable("y");
        Map<String, Double> vars = new HashMap<>();
        vars.put("y", 10.0);

        double result = variable.eval(vars);

        assertEquals(10.0, result);
    }
}