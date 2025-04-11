package ru.nsu.basargina.model;

/**
 * Enum with possible snake directions.
 */
public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    /**
     * Returns the X-axis offset depending on the direction.
     *
     * @return x offset
     */
    public int getXoffset() {
        return switch (this) {
            case LEFT -> -1;
            case RIGHT -> 1;
            default -> 0;
        };
    }

    /**
     * Returns the Y-axis offset depending on the direction.
     *
     * @return y offset
     */
    public int getYoffset() {
        return switch (this) {
            case UP -> -1;
            case DOWN -> 1;
            default -> 0;
        };
    }
}
