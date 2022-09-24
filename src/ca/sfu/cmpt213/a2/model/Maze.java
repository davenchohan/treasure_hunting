package ca.sfu.cmpt213.a2.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A class that uses a depth first search algorithm to create a 2D array of MazeCells to make a Maze with a single path.
 * It then adds loops for additional paths and gets rid of 2x2 squares of walls or empty space.
 *
 * @author Daven Chohan
 */

public class Maze {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 16;

    private final MazeCell[][] myMaze;


    public Maze() {
        myMaze = new MazeCell[HEIGHT][WIDTH];
        boolean isValid = false;
        while (!isValid) {
            initializeMaze(myMaze);
            fixCorner();
            createOuterWalls();
            backtrackRecursion(myMaze[1][1]);
            fixSomeLines();
            createOuterWalls();
            isValid = validateMaze();
        }
    }


    private void initializeMaze(MazeCell[][] maze) {
        for (int y = 0; y < Maze.HEIGHT; y++) {
            for (int x = 0; x < Maze.WIDTH; x++) {
                maze[y][x] = new MazeCell(true);
                maze[y][x].setXPosition(x);
                maze[y][x].setYPosition(y);
                maze[y][x].setCellType(CellType.WALL);
            }
        }
    }

    private void createOuterWalls() {
        for (int x = 0; x < WIDTH; x++) {
            myMaze[0][x].setVisited(true);
            myMaze[HEIGHT - 1][x].setVisited(true);
            myMaze[0][x].setWall(true);
            myMaze[HEIGHT - 1][x].setWall(true);
            myMaze[0][x].setCellType(CellType.WALL);
            myMaze[HEIGHT - 1][x].setCellType(CellType.WALL);

        }
        for (int y = 0; y < HEIGHT; y++) {
            myMaze[y][0].setVisited(true);
            myMaze[y][WIDTH - 1].setVisited(true);
            myMaze[y][0].setWall(true);
            myMaze[y][WIDTH - 1].setWall(true);
            myMaze[y][0].setCellType(CellType.WALL);
            myMaze[y][WIDTH - 1].setCellType(CellType.WALL);
        }
    }

    private void backtrackRecursion(MazeCell currentCell) {
        setCellAsVisited(currentCell);
        depthFirstSearch(currentCell);
    }

    private void setCellAsVisited(MazeCell currentCell) {
        int x = currentCell.getXPosition();
        int y = currentCell.getYPosition();
        myMaze[y][x].setVisited(true);
        myMaze[y][x].setWall(false);
        myMaze[y][x].setCellType(CellType.EMPTY);
    }

    private void depthFirstSearch(MazeCell currentCell) {
        ArrayList<String> randomDirection = new ArrayList<>();
        randomDirection.add("north");
        randomDirection.add("south");
        randomDirection.add("east");
        randomDirection.add("west");
        Collections.shuffle(randomDirection);
        int yPosition = currentCell.getYPosition();
        int xPosition = currentCell.getXPosition();
        for (String direction : randomDirection) {
            if (direction.equals("north")) {
                if (yPosition - 2 >= 0) {
                    if (!myMaze[yPosition - 2][xPosition].isVisited()) {
                        myMaze[yPosition - 1][xPosition].setCellType(CellType.EMPTY);
                        myMaze[yPosition - 1][xPosition].setVisited(true);
                        myMaze[yPosition - 1][xPosition].setWall(false);
                        currentCell.setYPosition(yPosition - 2);
                        backtrackRecursion(currentCell);
                    }
                }
            }
            if (direction.equals("south")) {
                if ((yPosition + 2) < HEIGHT) {
                    if ((!myMaze[yPosition + 2][xPosition].isVisited())) {
                        myMaze[yPosition + 1][xPosition].setCellType(CellType.EMPTY);
                        myMaze[yPosition + 1][xPosition].setVisited(true);
                        myMaze[yPosition + 1][xPosition].setWall(false);
                        currentCell.setYPosition(yPosition + 2);
                        backtrackRecursion(currentCell);
                    }
                }
            }
            if (direction.equals("east")) {
                if ((xPosition + 2) < WIDTH) {
                    if (!myMaze[yPosition][xPosition + 2].isVisited()) {
                        myMaze[yPosition][xPosition + 1].setCellType(CellType.EMPTY);
                        myMaze[yPosition][xPosition + 1].setVisited(true);
                        myMaze[yPosition][xPosition + 1].setWall(false);
                        currentCell.setXPosition(xPosition + 2);
                        backtrackRecursion(currentCell);
                    }
                }
            }
            if (direction.equals("west")) {
                if ((xPosition - 2) >= 0) {
                    if (!myMaze[yPosition][xPosition - 2].isVisited()) {
                        myMaze[yPosition][xPosition - 1].setCellType(CellType.EMPTY);
                        myMaze[yPosition][xPosition - 1].setVisited(true);
                        myMaze[yPosition][xPosition - 1].setWall(false);
                        currentCell.setXPosition(xPosition - 2);
                        backtrackRecursion(currentCell);
                    }
                }
            }
        }
    }

    private void fixSomeLines() {
        int additionalSpaceBottomRow = 0;
        while (additionalSpaceBottomRow < 2) {
            for (int x = 1; x < WIDTH - 2; x++) {
                if (myMaze[HEIGHT - 3][x].isWall()) {
                    myMaze[HEIGHT - 2][x].setWall(false);
                    myMaze[HEIGHT - 2][x].setCellType(CellType.EMPTY);
                    myMaze[HEIGHT - 2][x + 1].setWall(false);
                    myMaze[HEIGHT - 2][x + 1].setCellType(CellType.EMPTY);
                    myMaze[HEIGHT - 2][x - 1].setWall(false);
                    myMaze[HEIGHT - 2][x - 1].setCellType(CellType.EMPTY);
                    additionalSpaceBottomRow++;
                }
                additionalSpaceBottomRow++;
            }
        }

        int additionalSpaceRightColumn = 0;
        while (additionalSpaceRightColumn < 2) {
            for (int y = 1; y < HEIGHT - 2; y++) {
                if (myMaze[y][WIDTH - 3].isWall()) {
                    myMaze[y][WIDTH - 2].setWall(false);
                    myMaze[y][WIDTH - 2].setCellType(CellType.EMPTY);
                    myMaze[y + 1][WIDTH - 2].setWall(false);
                    myMaze[y + 1][WIDTH - 2].setCellType(CellType.EMPTY);
                    myMaze[y - 1][WIDTH - 2].setWall(false);
                    myMaze[y - 1][WIDTH - 2].setCellType(CellType.EMPTY);
                    additionalSpaceRightColumn++;
                }
                additionalSpaceRightColumn++;
            }
        }

        for (int x = 1; x < WIDTH - 1; x += 2) {
            myMaze[HEIGHT - 2][x].setWall(false);
            myMaze[HEIGHT - 2][x].setCellType(CellType.EMPTY);
        }

        for (int y = 0; y < HEIGHT - 1; y += 2) {
            myMaze[y][WIDTH - 2].setWall(false);
            myMaze[y][WIDTH - 2].setCellType(CellType.EMPTY);
        }

        int createLoops = 7;
        while (createLoops > 0) {
            int randomYLoop = (int) Math.round(Math.random() * (HEIGHT - 3) + 1);
            int randomXLoop = (int) Math.ceil(Math.random() * (WIDTH - 3) + 1);
            if (myMaze[randomYLoop][randomXLoop].isWall()) {
                myMaze[randomYLoop][randomXLoop].setWall(false);
                myMaze[randomYLoop][randomXLoop].setCellType(CellType.EMPTY);
                createLoops--;
            }
        }
    }

    private boolean validateMaze() {
        boolean isValid = true;
        for (int y = 1; y < HEIGHT; y++) {
            for (int x = 1; x < WIDTH; x++) {
                if (!myMaze[y][x].isWall()) {
                    boolean pathExists = !myMaze[y - 1][x].isWall();
                    if (!myMaze[y + 1][x].isWall()) {
                        pathExists = true;
                    }
                    if (!myMaze[y][x - 1].isWall()) {
                        pathExists = true;
                    }
                    if (!myMaze[y][x + 1].isWall()) {
                        pathExists = true;
                    }
                    if (!pathExists) {
                        isValid = false;
                    }
                }
                boolean fourEmptySquares = !myMaze[y][x].isWall() && !myMaze[y + 1][x].isWall() &&
                        !myMaze[y + 1][x + 1].isWall() && !myMaze[y][x + 1].isWall();
                if (fourEmptySquares) {
                    isValid = false;
                }
            }
        }
        for (int y = 0; y < HEIGHT - 2; y++) {
            for (int x = 0; x < WIDTH - 2; x++) {
                boolean fourWalls = myMaze[y][x].isWall() && myMaze[y + 1][x].isWall() &&
                        myMaze[y + 1][x + 1].isWall() && myMaze[y][x + 1].isWall();
                if (fourWalls) {
                    isValid = false;
                }
            }
        }
        return isValid;
    }

    private void fixCorner() {
        myMaze[1][1].setCellType(CellType.EMPTY);
        myMaze[1][1].setWall(false);
        myMaze[1][1].setVisited(true);
        myMaze[HEIGHT - 2][WIDTH - 2].setCellType(CellType.EMPTY);
        myMaze[HEIGHT - 2][WIDTH - 2].setWall(false);
        myMaze[HEIGHT - 2][WIDTH - 2].setVisited(true);
        myMaze[HEIGHT - 2][1].setCellType(CellType.EMPTY);
        myMaze[HEIGHT - 2][1].setWall(false);
        myMaze[HEIGHT - 2][1].setVisited(true);
        myMaze[1][WIDTH - 2].setCellType(CellType.EMPTY);
        myMaze[1][WIDTH - 2].setWall(false);
        myMaze[1][WIDTH - 2].setVisited(true);
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }


    public MazeCell[][] getMyMaze() {
        return myMaze;
    }
}
