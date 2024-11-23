package ru.nsu.basargina;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for creating student's record book
 * and getting various information about it using Stream API.
 */
public class RecordBook {
    private List<Grade> grades;
    private boolean isOnPaidForm;

    /**
     * Creates record book with grades.
     *
     * @param isOnPaidForm - if on paid form
     */
    public RecordBook(boolean isOnPaidForm) {
        this.grades = new ArrayList<>();
        this.isOnPaidForm = isOnPaidForm;
    }

    /**
     * Add grade to list of all grades.
     *
     * @param grade - grade to be added
     */
    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    /**
     * Calculate average grade score from all grades.
     *
     * @return - average grade
     */
    public double calculateAverageGrade() {
        if (grades.isEmpty()) {
            return 0.0;
        }

        int totalScore = grades.stream().mapToInt(Grade::getScore).sum();
        return totalScore / (double) grades.size();
    }

    /**
     * Checks if student can switch to budget form.
     *
     * @return true if student can switch
     */
    public boolean canSwitchToBudget() {
        if (!isOnPaidForm) {
            return false;
        }
        List<Grade> lastTwoSessions = getLastTwoSemestersGrades();

        // conditions for budget switch: no 2 or 3 for exam and diff_pass
        return lastTwoSessions.stream().noneMatch(
                grade -> (grade.getScore() == 2 ||
                         grade.isSatisfactory()) &&
                        (grade.getType() == WorkType.EXAM || grade.getType() == WorkType.DIFF_PASS)
        );
    }

    /**
     * Get all grades for last two semesters.
     *
     * @return list of last grades
     */
    private List<Grade> getLastTwoSemestersGrades() {
        List<Grade> lastTwoSessions = new ArrayList<>();
        int lastSemester = grades.stream().mapToInt(Grade::getSemester).max().orElse(0);

        for (Grade grade : grades) {
            if (grade.getSemester() >= lastSemester - 1) {
                lastTwoSessions.add(grade);
            }
        }
        return lastTwoSessions;
    }

    /**
     * Checks if student can get red diploma.
     *
     * @return true if student can get
     */
    public boolean canGetRedDiploma() {
        long fivesCnt = grades.stream().filter(Grade::isFive).count();
        
        // checks if student doesn't have 3
        boolean noSatisfactoryMarks = grades.stream().noneMatch(grade ->
                grade.isSatisfactory() && (grade.getType() == WorkType.EXAM || 
                        grade.getType() == WorkType.DIFF_PASS));

        // how many % of fives student has
        double fivesPercentage = (grades.isEmpty()) ? 0 
                : (fivesCnt / (double) grades.size()) * 100;

        // checks if student has 5 for vkr
        boolean hasExcellentVKR = grades.stream()
                .filter(grade -> grade.getType() == WorkType.VKR_DEFENSE)
                .anyMatch(Grade::isFive);

        // for red diploma student must have >= 75 % fives, no 3 grades and 5 for vkr
        return fivesPercentage >= 75 && noSatisfactoryMarks && hasExcellentVKR;
    }

    /**
     * Checks if student can get increased scholarship.
     *
     * @return true if student can
     */
    public boolean canGetIncreasedScholarship() {
        List<Grade> lastSemesterGrades = getLastSemesterGrades();
        // student must have all fives for last semester
        return lastSemesterGrades.stream().allMatch(Grade::isFive);
    }

    /**
     * Get all grades for last semester.
     *
     * @return list of all last semester grades
     */
    private List<Grade> getLastSemesterGrades() {
        int lastSemester = grades.stream().mapToInt(Grade::getSemester).max().orElse(0);
        List<Grade> lastSemesterGrades = new ArrayList<>();
        
        for (Grade grade : grades) {
            if (grade.getSemester() == lastSemester) {
                lastSemesterGrades.add(grade);
            }
        }
        return lastSemesterGrades;
    }
}
