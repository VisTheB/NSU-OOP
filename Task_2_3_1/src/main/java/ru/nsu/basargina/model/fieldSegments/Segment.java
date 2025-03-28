package ru.nsu.basargina.model.fieldSegments;

/**
 * Class that represents block on the game field.
 */
public abstract class Segment {
    private int x;
    private int y;

    /**
     * Create segment with given coordinates.
     *
     * @param x x coord
     * @param y y coord
     */
    public Segment(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for x.
     *
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for y.
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Setter for x.
     *
     * @return x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Setter for y.
     *
     * @return y
     */
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segment other = (Segment) o;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
