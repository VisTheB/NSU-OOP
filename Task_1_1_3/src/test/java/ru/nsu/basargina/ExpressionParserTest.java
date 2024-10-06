package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.basargina.actions.Add;
import ru.nsu.basargina.actions.Div;
import ru.nsu.basargina.actions.Mul;
import ru.nsu.basargina.actions.Sub;
import ru.nsu.basargina.data.Expression;
import ru.nsu.basargina.data.Number;
import ru.nsu.basargina.data.Variable;

/**
 * Class with tests for ExpressionParser class.
 */
class ExpressionParserTest {
    PrintStream originalOut;

    @Test
    public void testParseNumber() {
        ExpressionParser parser = new ExpressionParser("42");
        Expression expression = parser.parse();

        assertInstanceOf(Number.class, expression);
        assertEquals(42.0, expression.eval(new HashMap<>()));
        assertEquals("42.0", expression.toString());
    }

    @Test
    public void testParseVariable() {
        ExpressionParser parser = new ExpressionParser("x");
        Expression expression = parser.parse();

        assertInstanceOf(Variable.class, expression);

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 7.0);
        assertEquals(7.0, expression.eval(variables));
        assertEquals("x", expression.toString());
    }

    @Test
    public void testParseAdd() {
        ExpressionParser parser = new ExpressionParser("4 + 10");
        Expression expression = parser.parse();

        assertInstanceOf(Add.class, expression);

        assertEquals(14.0, expression.eval(new HashMap<>()));
        assertEquals("(4.0+10.0)", expression.toString());
    }

    @Test
    public void testParseSub() {
        ExpressionParser parser = new ExpressionParser("195 - 3");
        Expression expression = parser.parse();

        assertInstanceOf(Sub.class, expression);

        assertEquals(192.0, expression.eval(new HashMap<>()));
        assertEquals("(195.0-3.0)", expression.toString());
    }

    @Test
    public void testParseMul() {
        ExpressionParser parser = new ExpressionParser("25 * 10");
        Expression expression = parser.parse();

        assertInstanceOf(Mul.class, expression);

        assertEquals(250.0, expression.eval(new HashMap<>()));
        assertEquals("(25.0*10.0)", expression.toString());
    }

    @Test
    public void testParseDiv() {
        ExpressionParser parser = new ExpressionParser("16 / 2");
        Expression expression = parser.parse();

        assertInstanceOf(Div.class, expression);

        assertEquals(8.0, expression.eval(new HashMap<>()));
        assertEquals("(16.0/2.0)", expression.toString());
    }

    @Test
    public void testParseComplexExpression() {
        ExpressionParser parser = new ExpressionParser("(3 + x) * (7 - y)");
        Expression expression = parser.parse();

        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 2.0);
        variables.put("y", 5.0);

        String derivVar = "x";
        String resDerivative = expression.derivative(derivVar).toString();

        assertEquals("(((0.0+1.0)*(7.0-y))+((3.0+x)*(0.0-0.0)))", resDerivative);
        assertEquals(10.0, expression.eval(variables));
        assertEquals("((3.0+x)*(7.0-y))", expression.toString());
    }

    @Test
    public void testParseUnexpectedCharacter() {
        ExpressionParser parser = new ExpressionParser("3 + ?");

        // Redirect the standard output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        Expression expression = parser.parse();

        String output = outputStream.toString();
        String expectedOut = "Unexpected: ?\n";

        assertEquals(expectedOut, output);
        assertNull(expression);

        System.setOut(originalOut); // Restore original output
    }
}