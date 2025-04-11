package ru.nsu.basargina.model;

/**
 * Class that represents game level.
 */
public class Level {
    private int levelNumber;
    private int speed;
    private int obstacleCount;

    /**
     * Create level.
     *
     * @param levelNumber current level number
     * @param speed game speed
     * @param obstacleCount number of obstacles in current level
     */
    public Level(int levelNumber, int speed, int obstacleCount) {
        this.levelNumber = levelNumber;
        this.speed = speed;
        this.obstacleCount = obstacleCount;
    }

    /**
     * Getter for level number.
     *
     * @return level number
     */
    public int getLevelNumber() {
        return levelNumber;
    }

    /**
     * Getter for level speed.
     *
     * @return level speed
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Getter for number of obstacles.
     *
     * @return obstacles number
     */
    public int getObstacleCount() {
        return obstacleCount;
    }

    /**
     * Setter for level number.
     *
     * @param levelNumber number of the level to be set
     */
    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    /**
     * Setter for obstacle count.
     *
     * @param obstacleCount number of obstacles to be set
     */
    public void setObstacleCount(int obstacleCount) {
        this.obstacleCount = obstacleCount;
    }
}