package ru.nsu.basargina.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class with tests for Number class.
 */
class NumberTest {
//    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    PrintStream originalOut;
//
//    @BeforeEach
//    void setUp() {
//        // Redirect the standard output
//        originalOut = System.out;
//        System.setOut(new PrintStream(outputStream));
//    }
//
//    @AfterEach
//    public void tearDown() {
//        System.setOut(originalOut); // Restore original output
//    }

    @Test
    void testPrint() {
        Expression number = new Number(14675);

        String output = number.print();

        assertEquals("14675.0", output);
    }
    
    @Test
    void testDerivative() {
        Expression number = new Number(777);
        Expression derivative = number.derivative("x");

        String output = derivative.print();

        assertEquals("0.0", output);
    }

    @Test
    void testEval() {
        Expression number = new Number(228);
        double result = number.eval(new HashMap<>());

        assertEquals(228, result);
    }
}