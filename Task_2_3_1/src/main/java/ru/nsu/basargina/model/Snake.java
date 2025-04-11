package ru.nsu.basargina.model;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.basargina.model.fieldsegments.BodySegment;
import ru.nsu.basargina.model.fieldsegments.Segment;

/**
 * Class with snake's moving logic.
 */
public class Snake {
    private List<BodySegment> segments;
    private Direction currentDirection;

    /**
     * Create snake with initial parameters.
     *
     * @param startX start x coord
     * @param startY start y coord
     * @param initialDirection inital direction
     */
    public Snake(int startX, int startY, Direction initialDirection) {
        segments = new ArrayList<>();
        segments.add(new BodySegment(startX, startY));
        this.currentDirection = initialDirection;
    }

    /**
     * Getter for the whole snake.
     *
     * @return list of segments
     */
    public List<BodySegment> getSegments() {
        return segments;
    }

    /**
     * Getter for current snake direction.
     *
     * @return current direction
     */
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /**
     * Setter for current snake direction.
     *
     * @param direction current direction
     */
    public void setCurrentDirection(Direction direction) {
        this.currentDirection = direction;
    }

    /**
     * Method for snake moving.
     *
     * @param grow if true then snake grows
     */
    public void move(boolean grow) {
        Segment head = segments.getFirst();
        int newX = head.getX() + currentDirection.getXoffset();
        int newY = head.getY() + currentDirection.getYoffset();

        segments.addFirst(new BodySegment(newX, newY));

        if (!grow) {
            segments.removeLast();
        }
    }

    /**
     * Check if snake's head collided with body.
     *
     * @return true if collided
     */
    public boolean checkSelfCollision() {
        Segment head = segments.getFirst();
        for (int i = 1; i < segments.size(); i++) {
            if (head.equals(segments.get(i))) {
                return true;
            }
        }
        return false;
    }
}
