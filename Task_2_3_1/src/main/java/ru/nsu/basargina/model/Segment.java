package ru.nsu.basargina.model;

/**
 * Class that represents particular snake piece.
 */
public class Segment {
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Segment)) return false;
        Segment other = (Segment) obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
