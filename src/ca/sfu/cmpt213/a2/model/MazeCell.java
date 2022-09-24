package ca.sfu.cmpt213.a2.model;

/**
 * A class that creates a cell within the Maze. Each cell has a position, it's wall state, the type of Cell
 * it is and whether it has been visited or not.
 *
 * @author Daven Chohan
 */

public class MazeCell {
    private boolean isWall;
    private boolean isVisited;
    private CellType cellType;
    private int xPosition;
    private int yPosition;

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

    public MazeCell(boolean isWall) {
        this.isWall = isWall;
        isVisited = false;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean wall) {
        isWall = wall;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    public void setPosition(int yPositionTemp, int xPositionTemp) {
        xPosition = xPositionTemp;
        yPosition = yPositionTemp;
    }

    public void setCellType(CellType type) {
        switch (type) {
            case WALL:
                cellType = CellType.WALL;
                break;
            case EMPTY:
                cellType = CellType.EMPTY;
                break;
            case RELIC:
                cellType = CellType.RELIC;
                break;
            case GUARDIAN:
                cellType = CellType.GUARDIAN;
                break;
            case HUNTER:
                cellType = CellType.HUNTER;
                break;
            case INVISIBLE_EMPTY:
                cellType = CellType.INVISIBLE_EMPTY;
                break;
            case INVISIBLE_WALL:
                cellType = CellType.INVISIBLE_WALL;
                break;
            case DEAD_HUNTER:
                cellType = CellType.DEAD_HUNTER;
                break;
            default:
                assert false;
        }
    }


    public CellType getCellType() {
        return cellType;
    }
}
