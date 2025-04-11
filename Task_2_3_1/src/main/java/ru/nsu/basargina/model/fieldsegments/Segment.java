package ru.nsu.basargina.model.fieldsegments;

/**
 * Class that represents block on the game field.
 */
public abstract class Segment {
    private int xCoord;
    private int yCoord;

    /**
     * Create segment with given coordinates.
     *
     * @param xCoord x coord
     * @param yCoord y coord
     */
    public Segment(int xCoord, int yCoord) {
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    /**
     * Getter for xCoord.
     *
     * @return x coord
     */
    public int getX() {
        return xCoord;
    }

    /**
     * Getter for yCoord.
     *
     * @return y coord
     */
    public int getY() {
        return yCoord;
    }

    /**
     * Setter for xCoord.
     */
    public void setX(int xCoord) {
        this.xCoord = xCoord;
    }

    /**
     * Setter for yCoord.
     */
    public void setY(int yCoord) {
        this.yCoord = yCoord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Segment other = (Segment) o;
        return xCoord == other.xCoord && yCoord == other.yCoord;
    }

    @Override
    public int hashCode() {
        return 31 * xCoord + yCoord;
    }
}
