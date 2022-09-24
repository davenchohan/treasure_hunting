package ca.sfu.cmpt213.a2.model;

/**
 * A class that creates a Guardian object. Used to determine a Guardian's position. Also holds the Guardians previous move and previous position's CellType.
 *
 * @author Daven Chohan
 */

public class Guardian {
    private int xPosition;
    private int yPosition;
    private String previousMove;
    private CellType cellType;

    public Guardian(int yPosition, int xPosition, String previousMove, CellType cellType) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.previousMove = previousMove;
        this.cellType = cellType;
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

    public String getPreviousMove() {
        return previousMove;
    }

    public void setPreviousMove(String previousMove) {
        this.previousMove = previousMove;
    }

    public void setCellType(CellType type) {
        switch (type) {
            case EMPTY:
                cellType = CellType.EMPTY;
                break;

            case INVISIBLE_EMPTY:
                cellType = CellType.INVISIBLE_EMPTY;
                break;
            default:
                assert false;
        }
    }


    public CellType getCellType() {
        return cellType;
    }

}
