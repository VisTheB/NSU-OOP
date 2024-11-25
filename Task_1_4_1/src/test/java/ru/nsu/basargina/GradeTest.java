package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Class with tests for grades.
 */
class GradeTest {
    @Test
    public void testSetNameNull() {
        IvalidGradeInputException exception = assertThrows(IvalidGradeInputException.class, () -> {
            new Grade(null, 5, WorkType.EXAM, 1);
        });

        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testSetNameEmpty() {
        IvalidGradeInputException exception = assertThrows(IvalidGradeInputException.class, () -> {
            new Grade("", 5, WorkType.EXAM, 1);
        });

        assertEquals("Name cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testSetScoreTooLow() {
        IvalidGradeInputException exception = assertThrows(IvalidGradeInputException.class, () -> {
            new Grade("Math", -1, WorkType.DIFF_PASS, 2);
        });

        assertEquals("Score must be between 2 and 5.", exception.getMessage());
    }

    @Test
    public void testSetScoreTooHigh() {
        IvalidGradeInputException exception = assertThrows(IvalidGradeInputException.class, () -> {
            new Grade("Osi", 6, WorkType.ASSIGNMENT, 3);
        });

        assertEquals("Score must be between 2 and 5.", exception.getMessage());
    }

    @Test
    public void testSetWorkTypeNull() {
        IvalidGradeInputException exception = assertThrows(IvalidGradeInputException.class, () -> {
            Grade grade = new Grade("AI", 5, null, 4);
        });

        assertEquals("Work type cannot be null.", exception.getMessage());
    }

    @Test
    public void testSetSemesterNegative() {
        IvalidGradeInputException exception = assertThrows(IvalidGradeInputException.class, () -> {
            new Grade("English", 4, WorkType.TEST, 0);
        });

        assertEquals("Semester must be between 1 and 8.", exception.getMessage());
    }

    @Test
    public void testSetSemesterTooHigh() {
        IvalidGradeInputException exception = assertThrows(IvalidGradeInputException.class, () -> {
            new Grade("History", 2, WorkType.COLLOQUIUM, 9);
        });

        assertEquals("Semester must be between 1 and 8.", exception.getMessage());
    }
}