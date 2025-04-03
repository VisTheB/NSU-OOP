package ru.nsu.basargina.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ru.nsu.basargina.model.fieldsegments.BodySegment;
import ru.nsu.basargina.model.fieldsegments.FoodSegment;
import ru.nsu.basargina.model.fieldsegments.ObstacleSegment;

/**
 * Class that represents general state of the game: playing field, snake, food, obstacles, 
 * current level and target length of the snake.
 */
public class GameModel {
    private final int foodCount = 3;
    private int rows;
    private int cols;
    private Snake snake;
    private List<FoodSegment> foodItems;
    private List<ObstacleSegment> obstacles;
    private Level currentLevel;
    private int targetLength;
    private Random random;

    /**
     * Create GameModel.
     *
     * @param rows game field number of rows
     * @param cols game field number of columns
     * @param targetLength target snake length to win the level
     * @param level current game level
     */
    public GameModel(int rows, int cols, int targetLength, Level level) {
        this.rows = rows;
        this.cols = cols;
        this.currentLevel = level;
        random = new Random();

        initGame(level, targetLength);
    }

    public void initGame(Level currLevel, int targetLength) {
        this.targetLength = targetLength;

        // Initialize snake in the middle of the field
        snake = new Snake(cols / 2, rows / 2, Direction.RIGHT);

        foodItems = new ArrayList<>();
        obstacles = new ArrayList<>();

        for (int i = 0; i < foodCount; i++) {
            spawnFood();
        }

        for (int i = 0; i < currLevel.getObstacleCount(); i++) {
            spawnObstacle();
        }
    }

    /**
     * Getter for the snake.
     *
     * @return snake
     */
    public Snake getSnake() {
        return snake;
    }

    /**
     * Getter for food items.
     *
     * @return list of food items
     */
    public List<FoodSegment> getFoodItems() {
        return foodItems;
    }

    /**
     * Getter for obstacles.
     *
     * @return list of obstacles
     */
    public List<ObstacleSegment> getObstacles() {
        return obstacles;
    }

    /**
     * Getter for current level.
     *
     * @return current level
     */
    public Level getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Method for spawning food in random segments.
     */
    public void spawnFood() {
        int x;
        int y;

        do {
            x = random.nextInt(cols);
            y = random.nextInt(rows);
        } while (isSegmentOccupied(x, y));

        foodItems.add(new FoodSegment(x, y));
    }

    /**
     * Method for spawning obstacle in random segments.
     */
    public void spawnObstacle() {
        int x;
        int y;

        do {
            x = random.nextInt(cols);
            y = random.nextInt(rows);
        } while (isSegmentOccupied(x, y));

        obstacles.add(new ObstacleSegment(x, y));
    }

    /**
     * Check whether segment is occupied.
     *
     * @param x x coord
     * @param y y coord
     * @return true if segment is occupied
     */
    private boolean isSegmentOccupied(int x, int y) {
        for (BodySegment s : snake.getSegments()) {
            if (s.getX() == x && s.getY() == y) {
                return true;
            }
        }

        for (FoodSegment f : foodItems) {
            if (f.getX() == x && f.getY() == y) {
                return true;
            }
        }

        for (ObstacleSegment o : obstacles) {
            if (o.getX() == x && o.getY() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Update game state:
     *  - Check for collisions
     *  - Process food eating
     *  - Move snake.
     *
     * @return true if game continues, false if collision happened
     */
    public boolean update() {
        BodySegment nextHead = getNextHeadPosition();

        // Check if head is out of bounds
        if (nextHead.getX() < 0 || nextHead.getX() >= cols
                || nextHead.getY() < 0 || nextHead.getY() >= rows) {
            return false;
        }

        // Check head collision with body
        if (snake.checkSelfCollision()) {
            return false;
        }

        // Check head collision with obstacles
        for (ObstacleSegment o : obstacles) {
            if (nextHead.getX() == o.getX() && nextHead.getY() == o.getY()) {
                return false;
            }
        }

        // grow is true if snake's head is on the fruit
        boolean grow = false;
        for (FoodSegment f : foodItems) {
            if (nextHead.getX() == f.getX() && nextHead.getY() == f.getY()) {
                grow = true;
                foodItems.remove(f);
                spawnFood();
                break;
            }
        }

        snake.move(grow);
        return true;
    }

    /**
     * Create new head position based on current position.
     *
     * @return new body segment
     */
    private BodySegment getNextHeadPosition() {
        BodySegment head = snake.getSegments().getFirst();

        int newX = head.getX() + snake.getCurrentDirection().getXOffset();
        int newY = head.getY() + snake.getCurrentDirection().getYOffset();

        return new BodySegment(newX, newY);
    }

    /**
     * Check condition for winning (exceed target length).
     */
    public boolean isWin() {
        return snake.getSegments().size() >= targetLength;
    }
}