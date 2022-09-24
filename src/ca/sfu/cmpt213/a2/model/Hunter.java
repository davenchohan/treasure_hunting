package ca.sfu.cmpt213.a2.model;

/**
 * A class that creates a Hunter object that is utilized to determine the position of the Hunter
 *
 * @author Daven chohan
 */

public class Hunter {
    private int xPosition;
    private int yPosition;

    public Hunter(int yPosition, int xPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public int getXPosition() {
        return xPosition;
    }

    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }

}
