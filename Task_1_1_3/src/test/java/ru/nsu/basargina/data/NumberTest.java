package ru.nsu.basargina.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import org.junit.jupiter.api.Test;

/**
 * Class with tests for Number class.
 */
class NumberTest {
    @Test
    void testPrint() {
        Expression number = new Number(14675);

        String output = number.toString();

        assertEquals("14675.0", output);
    }
    
    @Test
    void testDerivative() {
        Expression number = new Number(777);
        Expression derivative = number.derivative("x");

        String output = derivative.toString();

        assertEquals("0.0", output);
    }

    @Test
    void testEval() {
        Expression number = new Number(228);
        double result = number.eval(new HashMap<>());

        assertEquals(228, result);
    }
}