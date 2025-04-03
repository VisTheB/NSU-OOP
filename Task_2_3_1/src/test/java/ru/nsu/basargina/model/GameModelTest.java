package ru.nsu.basargina.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ru.nsu.basargina.model.fieldsegments.FoodSegment;
import ru.nsu.basargina.model.fieldsegments.BodySegment;

/**
 * Class with tests for game model.
 */
public class GameModelTest {

    @Test
    public void testOutOfBounds() {
        Level level = new Level(1, 200, 0);
        GameModel model = new GameModel(3, 3, 10, level);
        Snake snake = model.getSnake();

        snake.setCurrentDirection(Direction.UP);
        // first step up
        assertTrue(model.update());
        // second step up
        assertFalse(model.update());
    }

    @Test
    public void testFoodConsumptionAndGrowth() {
        Level level = new Level(1, 200, 0);
        GameModel model = new GameModel(10, 10, 10, level);
        Snake snake = model.getSnake();

        // Delete all food
        model.getFoodItems().clear();
        snake.setCurrentDirection(Direction.RIGHT);

        BodySegment head = snake.getSegments().getFirst();
        model.getFoodItems().add(new FoodSegment(head.getX() + 1, head.getY()));

        int oldLength = snake.getSegments().size();
        boolean continueGame = model.update();

        // Snake should grow and game continue.
        assertTrue(continueGame);
        assertEquals(oldLength + 1, snake.getSegments().size());
    }
}