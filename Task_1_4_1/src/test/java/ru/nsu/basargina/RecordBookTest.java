package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class with tests for student's record book.
 */
class RecordBookTest {
    private RecordBook student;

    @BeforeEach
    public void setup() {
        student = new RecordBook(true);
    }

    @Test
    public void testCanGetRedDiplomaAllFivesWithVkr() throws Exception {
        student.addGrade(new Grade("Math", 5, WorkType.EXAM, 1));
        student.addGrade(new Grade("Physics", 5, WorkType.EXAM, 8));
        student.addGrade(new Grade("Chemistry", 4, WorkType.DIFF_PASS, 8));
        student.addGrade(new Grade("AI", 5, WorkType.EXAM, 8));
        student.addGrade(new Grade("VKR", 5, WorkType.VKR_DEFENSE, 8));

        assertTrue(student.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiplomaWith3and2GradeInExam() throws Exception {
        student.addGrade(new Grade("Math", 5, WorkType.EXAM, 1));
        student.addGrade(new Grade("Math2", 5, WorkType.EXAM, 8));
        student.addGrade(new Grade("Osi", 2, WorkType.EXAM, 8));
        student.addGrade(new Grade("History", 2, WorkType.DIFF_PASS, 8));
        student.addGrade(new Grade("VKR", 5, WorkType.VKR_DEFENSE, 8));

        assertFalse(student.canGetRedDiploma());
    }

    @Test
    public void testCanGetRedDiplomaInsufficient5Grades() throws Exception {
        student.addGrade(new Grade("Math", 4, WorkType.EXAM, 1));
        student.addGrade(new Grade("Biology", 4, WorkType.EXAM, 8));
        student.addGrade(new Grade("OOP", 4, WorkType.EXAM, 8));
        student.addGrade(new Grade("AI", 5, WorkType.DIFF_PASS, 8));
        student.addGrade(new Grade("VKR", 5, WorkType.VKR_DEFENSE, 8));

        assertFalse(student.canGetRedDiploma());
    }

    @Test
    public void testCalculateAverageGrade() throws Exception {
        student.addGrade(new Grade("Math", 5, WorkType.EXAM, 1));
        student.addGrade(new Grade("Models", 4, WorkType.TEST, 5));
        student.addGrade(new Grade("Chemistry", 3, WorkType.COLLOQUIUM, 6));
        student.addGrade(new Grade("Osi", 2, WorkType.PASS, 4));
        student.addGrade(new Grade("English", 5, WorkType.ASSIGNMENT, 2));

        assertEquals(3.8, student.calculateAverageGrade());
    }

    @Test
    public void testCalculateAverageGradeEmpty() {
        assertEquals(0.0, student.calculateAverageGrade());
    }

    @Test
    public void testCanSwitchToBudgetAlready() {
        RecordBook student = new RecordBook(false);

        assertFalse(student.canSwitchToBudget());
    }

    @Test
    public void testCanSwitchToBudget() throws Exception {
        student.addGrade(new Grade("Osi", 3, WorkType.EXAM, 2));
        student.addGrade(new Grade("Math", 5, WorkType.EXAM, 3));
        student.addGrade(new Grade("Physics", 4, WorkType.EXAM, 4));
        student.addGrade(new Grade("OOP", 5, WorkType.DIFF_PASS, 4));

        assertTrue(student.canSwitchToBudget());
    }

    @Test
    public void testCanSwitchToBudgetWith3Grades() throws Exception {
        student.addGrade(new Grade("Tvims", 4, WorkType.EXAM, 1));
        student.addGrade(new Grade("Math", 3, WorkType.EXAM, 2));
        student.addGrade(new Grade("Physics", 4, WorkType.EXAM, 2));
        student.addGrade(new Grade("OOP", 5, WorkType.DIFF_PASS, 3));

        assertFalse(student.canSwitchToBudget());
    }

    @Test
    public void testCanGetIncreasedScholarship() throws Exception {
        student.addGrade(new Grade("Math", 5, WorkType.EXAM, 1));
        student.addGrade(new Grade("English", 5, WorkType.TEST, 1));
        student.addGrade(new Grade("History", 5, WorkType.PASS, 1));

        assertTrue(student.canGetIncreasedScholarship());
    }

    @Test
    public void testCanGetIncreasedScholarshipWithNoAll5Grades() throws Exception {
        student.addGrade(new Grade("Osi", 4, WorkType.EXAM, 3));
        student.addGrade(new Grade("Tvims", 5, WorkType.ASSIGNMENT, 3));
        student.addGrade(new Grade("AI", 5, WorkType.COLLOQUIUM, 3));

        assertFalse(student.canGetIncreasedScholarship());
    }
}