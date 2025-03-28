package ru.nsu.basargina.model;

/**
 * Class that represents game level.
 */
public class Level {
    private int levelNumber;
    private int speed;          // Например, скорость обновления (мс)
    private int obstacleCount;

    /**
     * Create level.
     *
     * @param levelNumber current level number
     * @param speed
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
}