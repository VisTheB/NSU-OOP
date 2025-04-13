package ru.nsu.basargina;

/**
 * Class for representing possible exception for grade input data.
 */
public class InvalidGradeInputException extends Exception {
    /**
     * Creates exception with given message.
     *
     * @param message - exception message
     */
    public InvalidGradeInputException(String message) {
        super(message);
    }
}
