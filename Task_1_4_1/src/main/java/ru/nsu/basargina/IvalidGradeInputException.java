package ru.nsu.basargina;

/**
 * Class for representing possible exception for grade input data.
 */
public class IvalidGradeInputException extends Exception {
    /**
     * Creates exception with given message.
     *
     * @param message - exception message
     */
    public IvalidGradeInputException(String message) {
        super(message);
    }
}
