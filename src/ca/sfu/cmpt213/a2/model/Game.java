package ca.sfu.cmpt213.a2.model;


import java.util.ArrayList;
import java.util.Collections;

/**
 * This class uses the aspects of the Maze to create the logic. It calculates the next position of every sprite and also
 * determines whether the user has won or lost the game.
 *
 * @author Daven Chohan
 */

public class Game {
    private static Maze maze;
    private Hunter hunter;
    private ArrayList<Guardian> guardians;
    private Relic relic;
    private boolean canMove = true;
    private int relicCount = 0;
    private String extraCommand = "nothing";
    private boolean hasWon;
    private boolean onRelic;
    private MazeCell guardianOnRelic;
    private boolean relicCovered;
    private int relicsNeeded = 3;
    private boolean gameOver;
    private final int HEIGHT = Maze.getHEIGHT();
    private final int WIDTH = Maze.getWIDTH();

    public Game() {
        createMaze();
        createHunter();
        createRelic();
        createGuardians();
        hasWon = false;
        gameOver = false;
    }

    private void createMaze() {
        maze = new Maze();
        MazeCell[][] myMaze = maze.getMyMaze();
        for (int y = 1; y < HEIGHT - 1; y++) {
            for (int x = 1; x < WIDTH - 1; x++) {
                makeInvisibleCell(myMaze[y][x]);
            }
        }
    }

    private void createHunter() {
        MazeCell[][] myMaze = maze.getMyMaze();
        hunter = new Hunter(1, 1);
        myMaze[1][1].setCellType(CellType.HUNTER);
        revealAroundHunter();
    }

    private void createRelic() {
        int xPos;
        int yPos;
        MazeCell[][] myMaze = maze.getMyMaze();
        int height = Maze.getHEIGHT();
        int width = Maze.getWIDTH();
        boolean relicPlaced = false;
        while (!relicPlaced) {
            yPos = (int) Math.round(Math.random() * (height - 3) + 1);
            xPos = (int) Math.ceil(Math.random() * (width - 3) + 1);
            if (myMaze[yPos][xPos].getCellType() == CellType.EMPTY || myMaze[yPos][xPos].getCellType() == CellType.INVISIBLE_EMPTY) {
                relic = new Relic(yPos, xPos);
                myMaze[yPos][xPos].setCellType(CellType.RELIC);
                relicPlaced = true;
            }
        }
    }

    private void makeInvisibleCell(MazeCell currentCell) {
        MazeCell[][] myMaze = maze.getMyMaze();
        int yPos = currentCell.getYPosition();
        int xPos = currentCell.getXPosition();
        CellType tempType = currentCell.getCellType();
        if (tempType == CellType.WALL) {
            myMaze[yPos][xPos].setCellType(CellType.INVISIBLE_WALL);
        } else if (tempType == CellType.EMPTY) {
            myMaze[yPos][xPos].setCellType(CellType.INVISIBLE_EMPTY);
        }
    }

    private void revealCell(MazeCell currentCell) {
        MazeCell[][] myMaze = maze.getMyMaze();
        int yPos = currentCell.getYPosition();
        int xPos = currentCell.getXPosition();
        CellType tempType = currentCell.getCellType();
        if (tempType == CellType.INVISIBLE_WALL) {
            myMaze[yPos][xPos].setCellType(CellType.WALL);
        } else if (tempType == CellType.INVISIBLE_EMPTY) {
            myMaze[yPos][xPos].setCellType(CellType.EMPTY);
        }
    }

    private void revealEntireMaze() {
        MazeCell[][] myMaze = maze.getMyMaze();
        for (int y = 1; y < HEIGHT - 1; y++) {
            for (int x = 1; x < WIDTH - 1; x++) {
                revealCell(myMaze[y][x]);
            }
        }
        Guardian guardian1 = guardians.get(0);
        Guardian guardian2 = guardians.get(1);
        Guardian guardian3 = guardians.get(2);
        guardian1.setCellType(CellType.EMPTY);
        guardian2.setCellType(CellType.EMPTY);
        guardian3.setCellType(CellType.EMPTY);
    }

    private void revealAroundHunter() {
        MazeCell[][] myMaze = maze.getMyMaze();
        int yPos = hunter.getYPosition();
        int xPos = hunter.getXPosition();
        revealCell(myMaze[yPos + 1][xPos]);
        revealCell(myMaze[yPos - 1][xPos]);
        revealCell(myMaze[yPos][xPos + 1]);
        revealCell(myMaze[yPos][xPos - 1]);
        revealCell(myMaze[yPos + 1][xPos + 1]);
        revealCell(myMaze[yPos + 1][xPos - 1]);
        revealCell(myMaze[yPos - 1][xPos + 1]);
        revealCell(myMaze[yPos - 1][xPos - 1]);
    }

    private void createGuardians() {
        MazeCell[][] myMaze = maze.getMyMaze();
        guardians = new ArrayList<>();
        guardians.add(new Guardian(HEIGHT - 2, WIDTH - 2, "null", CellType.INVISIBLE_EMPTY));
        myMaze[HEIGHT - 2][WIDTH - 2].setCellType(CellType.GUARDIAN);
        guardians.add(new Guardian(HEIGHT - 2, 1, "null", CellType.INVISIBLE_EMPTY));
        myMaze[HEIGHT - 2][1].setCellType(CellType.GUARDIAN);
        guardians.add(new Guardian(1, WIDTH - 2, "null", CellType.INVISIBLE_EMPTY));
        myMaze[1][WIDTH - 2].setCellType(CellType.GUARDIAN);
    }

    public void makeMove(String command) {
        setExtraCommand("nothing");
        switch (command) {
            case "W", "S", "A", "D" -> moveHunter(command, hunter);
            case "?" -> setExtraCommand("?");
            case "C" -> setExtraCommand("C");
            case "M" -> setExtraCommand("M");
        }
    }

    public void setExtraCommand(String command) {
        extraCommand = command;
        if (command.equals("C")) {
            relicsNeeded = 1;
        }
        if (command.equals("M")) {
            revealEntireMaze();
        }
    }

    public String getExtraCommand() {
        return extraCommand;
    }

    private void moveHunter(String command, Hunter hunter) {
        canMove = true;
        int hunterXPos = hunter.getXPosition();
        int hunterYPos = hunter.getYPosition();
        MazeCell[][] myMaze = maze.getMyMaze();
        MazeCell newCell = new MazeCell(false);
        if (command.equals("W")) {
            newCell = myMaze[hunterYPos - 1][hunterXPos];
            newCell.setPosition(hunterYPos - 1, hunterXPos);
        }
        if (command.equals("S")) {
            newCell = myMaze[hunterYPos + 1][hunterXPos];
            newCell.setPosition(hunterYPos + 1, hunterXPos);
        }
        if (command.equals("A")) {
            newCell = myMaze[hunterYPos][hunterXPos - 1];
            newCell.setPosition(hunterYPos, hunterXPos - 1);
        }
        if (command.equals("D")) {
            newCell = myMaze[hunterYPos][hunterXPos + 1];
            newCell.setPosition(hunterYPos, hunterXPos + 1);
        }
        int newCellXPos = newCell.getXPosition();
        int newCellYPos = newCell.getYPosition();
        if (!validMove(myMaze[newCellYPos][newCellXPos])) {
            canMove = false;
        } else {
            hunter.setYPosition(newCellYPos);
            hunter.setXPosition(newCellXPos);
            if (onRelic) {
                myMaze[hunterYPos][hunterXPos].setCellType(CellType.RELIC);
            } else {
                myMaze[hunterYPos][hunterXPos].setCellType(CellType.EMPTY);
            }
            onRelic = myMaze[newCellYPos][newCellXPos].getCellType() == CellType.RELIC;
            myMaze[newCellYPos][newCellXPos].setCellType(CellType.HUNTER);
            checkRelic(myMaze[hunterYPos][hunterXPos]);
            revealAroundHunter();
            moveGuardians(guardians);
            if (myMaze[newCellYPos][newCellXPos].getCellType() == CellType.GUARDIAN) {
                myMaze[newCellYPos][newCellXPos].setCellType(CellType.DEAD_HUNTER);
                gameOver = true;
            }
        }
    }

    private void moveGuardians(ArrayList<Guardian> guardians) {
        MazeCell[][] myMaze = maze.getMyMaze();
        Guardian guardian1 = guardians.get(0);
        Guardian guardian2 = guardians.get(1);
        Guardian guardian3 = guardians.get(2);
        for (Guardian guardian : guardians) {
            int yPos = guardian.getYPosition();
            int xPos = guardian.getXPosition();
            boolean guardianMoved = false;
            while (!guardianMoved) {
                ArrayList<String> randomDirection = new ArrayList<>();
                randomDirection.add("north");
                randomDirection.add("south");
                randomDirection.add("east");
                randomDirection.add("west");
                Collections.shuffle(randomDirection);
                checkGuardianPath(guardian);
                label:
                for (String direction : randomDirection) {
                    switch (direction) {
                        case "north":
                            if (!myMaze[yPos - 1][xPos].isWall() && !guardian.getPreviousMove().equals("south")) {
                                if (relicCovered && guardianOnRelic == myMaze[yPos][xPos]) {
                                    myMaze[yPos][xPos].setCellType(CellType.RELIC);
                                    myMaze[yPos - 1][xPos].setCellType(CellType.GUARDIAN);
                                    relicCovered = false;
                                    if (myMaze[yPos][xPos].getCellType() == CellType.HUNTER) {
                                        myMaze[yPos][xPos].setCellType(CellType.DEAD_HUNTER);
                                        myMaze[yPos][xPos - 1].setCellType(CellType.EMPTY);
                                        gameOver = true;
                                    }
                                } else {
                                    if (checkIfGuardianOnRelic(myMaze[yPos - 1][xPos])) {
                                        relicCovered = true;
                                    }
                                    if (myMaze[yPos][xPos].getCellType() == CellType.HUNTER) {
                                        myMaze[yPos][xPos].setCellType(CellType.DEAD_HUNTER);
                                        myMaze[yPos - 1][xPos].setCellType(CellType.EMPTY);
                                        gameOver = true;
                                    } else {
                                        if (guardian.getCellType() != CellType.GUARDIAN) {
                                            myMaze[yPos][xPos].setCellType(guardian.getCellType());
                                        }
                                        CellType tempType = myMaze[yPos - 1][xPos].getCellType();
                                        guardian.setCellType(tempType);
                                        myMaze[yPos - 1][xPos].setCellType(CellType.GUARDIAN);
                                    }
                                }
                                guardian.setPreviousMove("north");
                                guardian.setYPosition(yPos - 1);
                                guardianMoved = true;
                                break label;
                            }
                            break;
                        case "south":
                            if (!myMaze[yPos + 1][xPos].isWall() && !guardian.getPreviousMove().equals("north")) {
                                if (relicCovered && guardianOnRelic == myMaze[yPos][xPos]) {
                                    myMaze[yPos][xPos].setCellType(CellType.RELIC);
                                    myMaze[yPos + 1][xPos].setCellType(CellType.GUARDIAN);
                                    relicCovered = false;
                                    if (myMaze[yPos][xPos].getCellType() == CellType.HUNTER) {
                                        myMaze[yPos][xPos].setCellType(CellType.DEAD_HUNTER);
                                        myMaze[yPos][xPos - 1].setCellType(CellType.EMPTY);
                                        gameOver = true;
                                    }
                                } else {
                                    if (checkIfGuardianOnRelic(myMaze[yPos + 1][xPos])) {
                                        relicCovered = true;
                                    }
                                    if (myMaze[yPos][xPos].getCellType() == CellType.HUNTER) {
                                        myMaze[yPos][xPos].setCellType(CellType.DEAD_HUNTER);
                                        myMaze[yPos + 1][xPos].setCellType(CellType.EMPTY);
                                        gameOver = true;
                                    } else {
                                        if (guardian.getCellType() != CellType.GUARDIAN) {
                                            myMaze[yPos][xPos].setCellType(guardian.getCellType());
                                        }
                                        CellType tempType = myMaze[yPos + 1][xPos].getCellType();
                                        guardian.setCellType(tempType);
                                        myMaze[yPos + 1][xPos].setCellType(CellType.GUARDIAN);
                                    }
                                }
                                guardian.setPreviousMove("south");
                                guardian.setYPosition(yPos + 1);
                                guardianMoved = true;
                                break label;
                            }
                            break;
                        case "east":
                            if (!myMaze[yPos][xPos + 1].isWall() && !guardian.getPreviousMove().equals("west")) {
                                if (relicCovered && guardianOnRelic == myMaze[yPos][xPos]) {
                                    myMaze[yPos][xPos].setCellType(CellType.RELIC);
                                    myMaze[yPos][xPos + 1].setCellType(CellType.GUARDIAN);
                                    relicCovered = false;
                                    if (myMaze[yPos][xPos].getCellType() == CellType.HUNTER) {
                                        myMaze[yPos][xPos].setCellType(CellType.DEAD_HUNTER);
                                        myMaze[yPos][xPos - 1].setCellType(CellType.EMPTY);
                                        gameOver = true;
                                    }
                                } else {
                                    if (checkIfGuardianOnRelic(myMaze[yPos][xPos + 1])) {
                                        relicCovered = true;
                                    }
                                    if (myMaze[yPos][xPos].getCellType() == CellType.HUNTER) {
                                        myMaze[yPos][xPos].setCellType(CellType.DEAD_HUNTER);
                                        myMaze[yPos][xPos + 1].setCellType(CellType.EMPTY);
                                        gameOver = true;
                                    } else {
                                        if (guardian.getCellType() != CellType.GUARDIAN) {
                                            myMaze[yPos][xPos].setCellType(guardian.getCellType());
                                        }
                                        CellType tempType = myMaze[yPos][xPos + 1].getCellType();
                                        guardian.setCellType(tempType);
                                        myMaze[yPos][xPos + 1].setCellType(CellType.GUARDIAN);
                                    }
                                }
                                guardian.setPreviousMove("east");
                                guardian.setXPosition(xPos + 1);
                                guardianMoved = true;
                                break label;
                            }
                            break;
                        case "west":
                            if (!myMaze[yPos][xPos - 1].isWall() && !guardian.getPreviousMove().equals("east")) {
                                if (relicCovered && guardianOnRelic == myMaze[yPos][xPos]) {
                                    myMaze[yPos][xPos].setCellType(CellType.RELIC);
                                    myMaze[yPos][xPos - 1].setCellType(CellType.GUARDIAN);
                                    relicCovered = false;
                                    if (myMaze[yPos][xPos].getCellType() == CellType.HUNTER) {
                                        myMaze[yPos][xPos].setCellType(CellType.DEAD_HUNTER);
                                        myMaze[yPos][xPos - 1].setCellType(CellType.EMPTY);
                                        gameOver = true;
                                    }
                                } else {
                                    if (checkIfGuardianOnRelic(myMaze[yPos][xPos - 1])) {
                                        relicCovered = true;
                                    }
                                    if (myMaze[yPos][xPos].getCellType() == CellType.HUNTER) {
                                        myMaze[yPos][xPos].setCellType(CellType.DEAD_HUNTER);
                                        myMaze[yPos][xPos - 1].setCellType(CellType.EMPTY);
                                        gameOver = true;
                                    } else {
                                        if (guardian.getCellType() != CellType.GUARDIAN) {
                                            myMaze[yPos][xPos].setCellType(guardian.getCellType());
                                        }
                                        CellType tempType = myMaze[yPos][xPos - 1].getCellType();
                                        guardian.setCellType(tempType);
                                        myMaze[yPos][xPos - 1].setCellType(CellType.GUARDIAN);
                                    }
                                }
                                guardian.setPreviousMove("west");
                                guardian.setXPosition(xPos - 1);
                                guardianMoved = true;
                                break label;
                            }
                            break;
                        default:
                            assert false;
                    }
                }
            }
        }
        int tempY1 = guardian1.getYPosition();
        int tempX1 = guardian1.getXPosition();
        int tempY2 = guardian2.getYPosition();
        int tempX2 = guardian2.getXPosition();
        int tempY3 = guardian3.getYPosition();
        int tempX3 = guardian3.getXPosition();
        if (myMaze[tempY1][tempX1].getCellType() != CellType.GUARDIAN) {
            myMaze[tempY1][tempX1].setCellType(CellType.GUARDIAN);
        }
        if (myMaze[tempY2][tempX2].getCellType() != CellType.GUARDIAN) {
            myMaze[tempY2][tempX2].setCellType(CellType.GUARDIAN);
        }
        if (myMaze[tempY3][tempX3].getCellType() != CellType.GUARDIAN) {
            myMaze[tempY3][tempX3].setCellType(CellType.GUARDIAN);
        }
    }

    private boolean checkIfGuardianOnRelic(MazeCell guardian) {
        if (guardian.getCellType() == CellType.RELIC) {
            guardianOnRelic = guardian;
            return true;
        } else {
            return false;
        }
    }


    private void checkRelic(MazeCell tempCell) {
        MazeCell[][] myMaze = maze.getMyMaze();
        int yPos = tempCell.getYPosition();
        int xPos = tempCell.getXPosition();
        if (tempCell.getCellType() == CellType.RELIC) {
            myMaze[yPos][xPos].setCellType(CellType.EMPTY);
        }
        if (onRelic) {
            relicCount++;
            createRelic();
        }
        if (relicCount >= relicsNeeded) {
            hasWon = true;
        }
    }

    private void checkGuardianPath(Guardian guardian) {
        MazeCell[][] myMaze = maze.getMyMaze();
        int yPos = guardian.getYPosition();
        int xPos = guardian.getXPosition();
        boolean topWall = false;
        boolean bottomWall = false;
        boolean leftWall = false;
        boolean rightWall = false;
        if (myMaze[yPos + 1][xPos].isWall()) {
            bottomWall = true;
        }
        if (myMaze[yPos - 1][xPos].isWall()) {
            topWall = true;
        }
        if (myMaze[yPos][xPos + 1].isWall()) {
            rightWall = true;
        }
        if (myMaze[yPos][xPos - 1].isWall()) {
            leftWall = true;
        }
        if (topWall && leftWall && rightWall) {
            guardian.setPreviousMove("south");
        }
        if (bottomWall && leftWall && rightWall) {
            guardian.setPreviousMove("north");
        }
        if (topWall && bottomWall && rightWall) {
            guardian.setPreviousMove("west");
        }
        if (topWall && leftWall && bottomWall) {
            guardian.setPreviousMove("east");
        }
    }

    public boolean validMove(MazeCell newSpot) {
        return !newSpot.isWall();
    }


    public int getRelicCount() {
        return relicCount;
    }

    public int getRelicsNeeded() {
        return relicsNeeded;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public boolean isHasWon() {
        return hasWon;
    }

    public boolean isGameOver() {
        MazeCell[][] myMaze = maze.getMyMaze();
        if (gameOver){
            int tempX = hunter.getXPosition();
            int tempY = hunter.getYPosition();
            if (myMaze[tempY+1][tempX].getCellType() == CellType.GUARDIAN) {
                myMaze[tempY+1][tempX].setCellType(CellType.EMPTY);
            }
            else if (myMaze[tempY-1][tempX].getCellType() == CellType.GUARDIAN) {
                myMaze[tempY-1][tempX].setCellType(CellType.EMPTY);
            }
            else if (myMaze[tempY][tempX+1].getCellType() == CellType.GUARDIAN) {
                myMaze[tempY][tempX+1].setCellType(CellType.EMPTY);
            }
            else if (myMaze[tempY][tempX-1].getCellType() == CellType.GUARDIAN) {
                myMaze[tempY][tempX-1].setCellType(CellType.EMPTY);
            }
        }
        return gameOver;
    }

    public static MazeCell[][] getMyMaze() {
        return maze.getMyMaze();
    }

}

