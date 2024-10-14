package ru.nsu.basargina.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        String output = variable.toString();

        assertEquals("z", output);
    }

    @Test
    void testDerivative() {
        Expression variable = new Variable("y");
        Expression derivative = variable.derivative("y");

        String output = derivative.toString();

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

    @Test
    void testEvalWithoutVariable() {
        Expression variable = new Variable("f");
        Map<String, Double> vars = new HashMap<>();
        vars.put("k", 2.0);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            variable.eval(vars);
        });
        assertEquals("You haven't assigned this variable: f", exception.getMessage());
    }
}