package ru.nsu.basargina;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import ru.nsu.basargina.data.Expression;

/**
 * Main class where user inputs string.
 */
public class Main {
    /**
     * Calls parser to parse given expression.
     *
     * @param args - isn't used
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Input expression to parse:");
        String userInput = scanner.nextLine();
        ExpressionParser parser = new ExpressionParser(userInput);

        Expression e = parser.parse();
        System.out.println(e.print());

        // differentiation
        System.out.println("Input the derivative of the variable:");
        String derivVar = scanner.nextLine();
        System.out.println(e.derivative(derivVar).print());

        // evaluation
        System.out.println("Input variables and their values in the format 'x=10; y=13' :");
        String variablesInput = scanner.nextLine();

        Map<String, Double> variables = parseVariables(variablesInput);
        System.out.println(e.eval(variables));
    }

    /**
     * Parses user's input of variables and put it to map.
     *
     * @param input - user input
     * @return map with variables as keys and their values as values
     */
    private static Map<String, Double> parseVariables(String input) {
        Map<String, Double> variables = new HashMap<>();
        String[] assignments = input.split(";");

        for (String assignment : assignments) {
            assignment = assignment.trim(); // trim the spaces

            if (!assignment.isEmpty()) {
                String[] parts = assignment.split("=");

                if (parts.length == 2) {
                    String variable = parts[0].trim();
                    double value = Double.parseDouble(parts[1].trim());
                    variables.put(variable, value);
                }
            }
        }
        return variables;
    }
}