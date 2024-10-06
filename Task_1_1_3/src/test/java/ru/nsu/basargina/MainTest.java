package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Class with tests for Main class.
 */
class MainTest {
    PrintStream originalOut;
    InputStream originalIn;

    @AfterEach
    void tearDown() {
        System.setOut(originalOut); // Restore original output
        System.setIn(originalIn); // Restore original input
    }

    @Test
    void testMainMethods() {
        String input = "((3+x)/((7*y)-11))\nx\nx=27; y = 2\n";
        originalIn = System.in; // original input stream
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Redirect the standard output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        Main.main(new String[0]);

        String output = outputStream.toString();
        String expectedOut = "Input expression to parse:\n"
                + "((3.0+x)/((7.0*y)-11.0))\n"
                + "Input the derivative of the variable:\n"
                + "((((0.0+1.0)*((7.0*y)-11.0))-((3.0+x)*(((0.0*y)+"
                + "(7.0*0.0))-0.0)))/(((7.0*y)-11.0)*((7.0*y)-11.0)))\n"
                + "Input variables and their values in the format 'x=10; y=13' :\n"
                + "10.0\n";

        assertEquals(expectedOut, output);
    }

    @Test
    void testIncorrectInput() {
        String input = "(3+?)\n";
        originalIn = System.in; // original input stream
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Redirect the standard output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        Main.main(new String[0]);

        String output = outputStream.toString();
        String expectedOut = "Input expression to parse:\n"
                + "Unexpected: ?\n"
                + "You entered incorrect expression!\n";

        assertEquals(expectedOut, output);
    }

    @Test
    public void testParseVariables() {
        String input = "x=10; y=20";
        Map<String, Double> result = Main.parseVariables(input);

        assertEquals(2, result.size());
        assertEquals(10.0, result.get("x"));
        assertEquals(20.0, result.get("y"));
    }
}