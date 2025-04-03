package ru.nsu.basargina.model.fieldsegments;

/**
 * Class that represents particular snake segment on the field.
 */
public class BodySegment extends Segment {

    /**
     * Create snake segment with given coordinates.
     *
     * @param x x coord
     * @param y y coord
     */
    public BodySegment(int x, int y) {
        super(x, y);
    }
}