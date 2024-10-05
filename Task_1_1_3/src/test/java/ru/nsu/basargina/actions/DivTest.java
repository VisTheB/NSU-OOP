package ru.nsu.basargina.actions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.basargina.data.Expression;
import ru.nsu.basargina.data.Number;
import ru.nsu.basargina.data.Variable;

/**
 * Class with tests for Div class.
 */
class DivTest {
    @Test
    void testPrint() {
        Expression left = new Variable("o");
        Expression right = new Number(-765);
        Div division = new Div(left, right);

        String output = division.toString();

        assertEquals("(o/-765.0)", output);
    }

    @Test
    void testDerivative() {
        Expression left = new Variable("s");
        Expression right = new Number(52);
        Div division = new Div(left, right);

        Expression derivative = division.derivative("s");
        String output = derivative.toString();

        assertEquals("(((1.0*52.0)-(s*0.0))/(52.0*52.0))", output);
    }

    @Test
    void testEval() {
        Expression left = new Variable("k");
        Expression right = new Number(512);
        Div division = new Div(left, right);

        Map<String, Double> vars = new HashMap<>();
        vars.put("k", 1024.0);

        double result = division.eval(vars);

        assertEquals(2.0, result);
    }
}