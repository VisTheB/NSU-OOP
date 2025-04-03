package ru.nsu.basargina.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.nsu.basargina.model.fieldsegments.BodySegment;

/**
 * Class with tests for snake.
 */
public class SnakeTest {
    Snake snake;
    int initialLength;
    @BeforeEach
    public void setUp() {
        snake = new Snake(5, 5, Direction.RIGHT);
        initialLength = snake.getSegments().size();
    }

    @Test
    public void testMoveWithoutGrowth() {
        snake.move(false);

        assertEquals(initialLength, snake.getSegments().size());

        BodySegment head = snake.getSegments().getFirst();
        assertEquals(6, head.getX());
        assertEquals(5, head.getY());
    }

    @Test
    public void testMoveWithGrowth() {
        snake.move(true);
        assertEquals(initialLength + 1, snake.getSegments().size());
    }

    @Test
    public void testSelfCollision() {
        snake.move(true); // (6,5)
        snake.setCurrentDirection(Direction.DOWN);
        snake.move(true); // (6,6)
        snake.setCurrentDirection(Direction.LEFT);
        snake.move(true); // (5,6)
        snake.setCurrentDirection(Direction.UP);
        snake.move(true); // (5,5)
        assertTrue(snake.checkSelfCollision());
    }
}