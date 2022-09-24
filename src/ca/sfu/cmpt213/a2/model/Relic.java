package ca.sfu.cmpt213.a2.model;

/**
 * A class that creates a Relic object. Used to determine the position of the created Relic.
 *
 * @author Daven Chohan
 */

public class Relic {
    private int xPosition;
    private int yPosition;

    public Relic(int yPosition, int xPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

}

