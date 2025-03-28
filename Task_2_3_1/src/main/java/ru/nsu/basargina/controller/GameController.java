package ru.nsu.basargina.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import ru.nsu.basargina.model.*;
import ru.nsu.basargina.model.fieldSegments.BodySegment;
import ru.nsu.basargina.model.fieldSegments.FoodSegment;
import ru.nsu.basargina.model.fieldSegments.ObstacleSegment;

/**
 * Class that represents controller of the game.
 */
public class GameController {
    @FXML
    private Canvas gameCanvas;

    @FXML
    private Label statusLabel;

    @FXML
    private Label levelLabel;

    private final int rows = 20;
    private final int cols = 30;
    private final int foodCount = 4;
    private final int targetLength = 10;
    private final int blockSize = 25;
    private final Level level = new Level(1, 200, 5);

    private GameModel gameModel;
    private Timeline gameLoop;

    /**
     * Start the game:
     */
    @FXML
    public void initialize() {

        gameModel = new GameModel(rows, cols, foodCount, targetLength, level);

        Level currentLevel = gameModel.getCurrentLevel();

        levelLabel.setText("Level: " + currentLevel.getLevelNumber());

        // key pressed event handler
        gameCanvas.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
            }
        });

        gameLoop = new Timeline(new KeyFrame(Duration.millis(currentLevel.getSpeed()), event -> {
            if (!gameModel.update()) {
                statusLabel.setText("Game Over!");
                gameLoop.stop();
            } else if (gameModel.isWin()) {
                statusLabel.setText("You Win!");
                gameLoop.stop();
            }
            levelLabel.setText("Level: " + currentLevel.getLevelNumber());
            draw();
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();

        draw();
    }

    /**
     * A key handler for changing the direction of the snake.
     *
     * @param event what key was pressed
     */
    private void handleKeyPress(KeyEvent event) {
        Snake snake = gameModel.getSnake();
        switch (event.getCode()) {
            case UP -> snake.setCurrentDirection(Direction.UP);
            case DOWN -> snake.setCurrentDirection(Direction.DOWN);
            case LEFT -> snake.setCurrentDirection(Direction.LEFT);
            case RIGHT -> snake.setCurrentDirection(Direction.RIGHT);
            default -> {
            }
        }
    }

    /**
     *Draw game field, snake, food and obstacles.
     */
    private void draw() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        //grid
        gc.setStroke(Color.LIGHTBLUE);
        for (int i = 0; i <= cols; i++) {
            gc.strokeLine(i * blockSize, 0, i * blockSize, rows * blockSize);
        }
        for (int j = 0; j <= rows; j++) {
            gc.strokeLine(0, j * blockSize, cols * blockSize, j * blockSize);
        }

        // Food
        gc.setFill(Color.RED);
        for (FoodSegment food : gameModel.getFoodItems()) {
            int x = food.getX();
            int y = food.getY();
            gc.fillRect(x * blockSize, y * blockSize, blockSize - 1, blockSize - 1);
        }

        // Obstacles
        gc.setFill(Color.DARKGRAY);
        for (ObstacleSegment obstacle : gameModel.getObstacles()) {
            int x = obstacle.getX();
            int y = obstacle.getY();
            gc.fillRect(x * blockSize, y * blockSize, blockSize - 1, blockSize - 1);
        }

        // Snake's body
        gc.setFill(Color.GREEN);
        for (BodySegment segment : gameModel.getSnake().getSegments()) {
            gc.fillRect(segment.getX() * blockSize, segment.getY() * blockSize, blockSize - 1, blockSize - 1);
        }
        // Snake's head
        gc.setFill(Color.DARKGREEN);
        BodySegment head = gameModel.getSnake().getSegments().getFirst();
        gc.fillRect(head.getX() * blockSize, head.getY() * blockSize, blockSize - 1, blockSize - 1);
    }
}