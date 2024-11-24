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
    public Grade(String subjectName, int score, WorkType type, int semester) throws Exception {
        setName(subjectName);
        setScore(score);
        setWorkType(type);
        setSemester(semester);
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

    /**
     * Set subject name of the grade.
     *
     * @param name - non-empty string
     * @throws Exception if name is null or empty
     */
    public void setName(String name) throws Exception {
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("Name cannot be null or empty.");
        }
        this.subjectName = name;
    }

    /**
     * Set score of the grade.
     *
     * @param score - number between 2 and 5
     * @throws Exception if number is invalid
     */
    public void setScore(int score) throws Exception {
        if (score < 2 || score > 5) {
            throw new Exception("Score must be between 2 and 5.");
        }
        this.score = score;
    }

    /**
     * Set work type of the grade.
     * @param type - one of 8 work types
     * @throws Exception if work type is null
     */
    public void setWorkType(WorkType type) throws Exception {
        if (type == null) {
            throw new Exception("Work type cannot be null.");
        }
        this.type = type;
    }

    /**
     * Set semester in which grade was received.
     *
     * @param semester - number between 1 and 8
     * @throws Exception if number is invalid
     */
    public void setSemester(int semester) throws Exception {
        if (semester < 1 || semester > 8) {
            throw new Exception("Semester must be between 1 and 8.");
        }
        this.semester = semester;
    }
}
