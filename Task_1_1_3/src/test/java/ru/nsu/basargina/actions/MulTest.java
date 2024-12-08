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

    @Test
    void testEvalWithoutVariables() {
        Expression left = new Sub(new Number(20), new Number(4));
        Expression right = new Div(new Add(new Number(7), new Number(1)), new Number(1));
        Mul multiplication = new Mul(left, right);
        Expression simplified = multiplication.simplify();

        double result = simplified.eval();

        assertEquals(128.0, result);
    }

    @Test
    void testMulByZero() {
        Expression left = new Sub(new Number(5), new Number(5));
        Expression right = new Number(10);
        Mul multiplication = new Mul(left, right);

        Expression left2 = new Number(11);
        Expression right2 = new Div(new Number(0), new Number(3));
        Mul multiplication2 = new Mul(left2, right2);

        Expression simplified = multiplication.simplify();
        Expression simplified2 = multiplication2.simplify();

        double result = simplified.eval();
        double result2 = simplified2.eval();

        assertEquals(0.0, result);
        assertEquals(0.0, result2);
    }

    @Test
    void testMulByOne() {
        Expression left = new Number(1);
        Expression right = new Number(2);
        Mul multiplication = new Mul(left, right);

        Expression left2 = new Number(11);
        Expression right2 = new Number(1);
        Mul multiplication2 = new Mul(left2, right2);

        Expression simplified = multiplication.simplify();
        Expression simplified2 = multiplication2.simplify();

        double result = simplified.eval();
        double result2 = simplified2.eval();

        assertEquals(2.0, result);
        assertEquals(11.0, result2);
    }
}