package ru.nsu.basargina;

/**
 * Class for storing information about grade.
 */
public class Grade {
    private String subjectName;
    private int score;
    private WorkType type;
    private int semester;

    /**
     * Creates student's grade.
     *
     * @param subjectName - name of the subject
     * @param score - what grade
     * @param type - type of the job
     * @param semester - number of the current semester
     */
    public Grade(String subjectName, int score, WorkType type, int semester) {
        this.subjectName = subjectName;
        this.score = score;
        this.type = type;
        this.semester = semester;
    }

    /**
     * Get type of the work.
     *
     * @return work type
     */
    public WorkType getType() {
        return type;
    }

    /**
     * Get score of the grade.
     *
     * @return grade score
     */
    public int getScore() {
        return score;
    }

    /**
     * Get number of the current semester.
     *
     * @return semester number
     */
    public int getSemester() {
        return semester;
    }

    /**
     * Checks if grade is excellent.
     *
     * @return true if grade is 5
     */
    public boolean isFive() {
        return score == 5;
    }

    /**
     * Checks if grade is satisfactory.
     *
     * @return true if grade is 3
     */
    public boolean isSatisfactory() {
        return score == 3;
    }
}
