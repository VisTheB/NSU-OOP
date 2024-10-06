package ru.nsu.basargina;

import ru.nsu.basargina.actions.Add;
import ru.nsu.basargina.actions.Div;
import ru.nsu.basargina.actions.Mul;
import ru.nsu.basargina.actions.Sub;
import ru.nsu.basargina.data.Expression;
import ru.nsu.basargina.data.Number;
import ru.nsu.basargina.data.Variable;

/**
 * Class that parse given expression to required format.
 */
public class ExpressionParser {

    private int pos = -1; // current index
    private int ch; // current char
    private final String input;

    /**
     * Create parser for user input.
     *
     * @param input - user input
     */
    public ExpressionParser(String input) {
        this.input = input;
    }

    /**
     * Moves the pointer to the next character and updates the current ch character.
     */
    private void nextChar() {
        ch = (++pos < input.length()) ? input.charAt(pos) : -1;
    }

    /**
     * Skips spaces and checks if the current character is equal to charToEat.
     * If yes, it goes to the next character.
     *
     * @param charToEat - char for comparison
     * @return true if charToEat is equal to ch
     */
    private boolean eat(int charToEat) {
        while (ch == ' ') {
            nextChar();
        }

        if (ch == charToEat) {
            nextChar();
            return true;
        }
        return false;
    }

    /**
     * Starts the string parsing process.
     *
     * @return parsed expression (like: new Mul(new Number(2), new Variable("x")))
     */
    public Expression parse() {
        try {
            nextChar();
            Expression expr = parseExpression();

            if (pos < input.length()) {
                throw new Exception("Unexpected: " + (char) ch);
            }
            return expr;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Processing of additions and subtractions in an expression.
     *
     * @return an expression representing the sum or difference of terms
     */
    private Expression parseExpression() throws Exception {
        // check for "*" or "/"
        Expression expr = parseTerm();

        while (true) {
            if (eat('+')) {
                expr = new Add(expr, parseTerm());
            } else if (eat('-')) {
                expr = new Sub(expr, parseTerm());
            } else {
                return expr;
            }
        }
    }

    /**
     * Processing of divisions and multiplications in an expression.
     *
     * @return an expression representing the product or quotient of terms
     */
    private Expression parseTerm() throws Exception {
        // check for number, variable, unary "+"/"-", ")", "("
        Expression term = parseFactor();

        while (true) {
            if (eat('*')) {
                term = new Mul(term, parseFactor());
            } else if (eat('/')) {
                term = new Div(term, parseFactor());
            } else {
                return term;
            }
        }
    }

    /**
     * Processing of numbers, variables, unary "+"/"-", ")", "(".
     *
     * @return an expression representing a number, variable, or subexpression in parentheses
     */
    private Expression parseFactor() throws Exception {
        // unary plus
        if (eat('+')) {
            return parseFactor();
        }
        // unary minus
        if (eat('-')) {
            return new Sub(new Number(0), parseFactor());
        }

        Expression factor;
        int startPos = this.pos; // fix start position

        if (eat('(')) { // parentheses

            factor = parseExpression(); // parse the expression in parentheses
            eat(')');

        } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers

            while ((ch >= '0' && ch <= '9') || ch == '.') {
                nextChar();
            }
            // convert string number to integer
            int value = Integer.parseInt(input.substring(startPos, this.pos));
            factor = new Number(value);

        } else if (ch >= 'a' && ch <= 'z') { // variables

            while (ch >= 'a' && ch <= 'z') {
                nextChar();
            }
            String name = input.substring(startPos, this.pos);
            factor = new Variable(name);

        } else {
            throw new Exception("Unexpected: " + (char) ch);
        }

        return factor;
    }
}