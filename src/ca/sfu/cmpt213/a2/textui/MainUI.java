package ca.sfu.cmpt213.a2.textui;

import ca.sfu.cmpt213.a2.model.CellType;
import ca.sfu.cmpt213.a2.model.Game;
import ca.sfu.cmpt213.a2.model.Maze;
import ca.sfu.cmpt213.a2.model.MazeCell;

import java.util.Scanner;

/**
 * This class utilizes a portion from the model package to display the maze and get keyboard inputs for the next move.
 *
 * @author Daven Chohan
 */

public class MainUI {

    private static Game game;

    private static void instructions() {
        System.out.println("\nDIRECTIONS:\n" +
                "\tCollect 3 relics!\n" +
                "LEGEND:\n" +
                "\t#: Wall\n" +
                "\t@: You (the treasure hunter)\n" +
                "\t!: Guardian\n" +
                "\t^: Relic\n" +
                "\t.: Unexplored space\n" +
                "MOVES:\n" +
                "\tUse W (up), A (left), S (down) and D (right) to move.\n" +
                "\t(You must press enter after each move).");
    }

    private static void print(MazeCell[][] maze) {
        checkWin(maze);
        checkLoss(maze);
        printRegularMaze(maze);
        game.makeMove(question());
        checkMove(maze);
        while (!game.getExtraCommand().equals("nothing")) {
            checkExtraCommand(maze);
            game.makeMove(question());
            checkMove(maze);
        }
        print(maze);
    }

    private static void checkMove(MazeCell[][] maze) {
        while (!game.isCanMove()) {
            System.out.println("Invalid move: you cannot move through walls!");
            game.makeMove(question());
            checkExtraCommand(maze);
        }
    }

    private static void checkWin(MazeCell[][] maze) {
        if (game.isHasWon()) {
            System.out.println("Congratulations! You won!");
            printFullMaze(maze);
            System.exit(0);
        }
    }

    private static void checkLoss(MazeCell[][] maze) {
        if (game.isGameOver()) {
            System.out.println("Oh no! The hunter has been killed!");
            printFullMaze(maze);
            System.out.println("GAME OVER... please try again.");
            System.exit(0);
        }
    }

    private static void printRegularMaze(MazeCell[][] maze) {
        System.out.println("\nMaze:");
        for (int y = 0; y < Maze.getHEIGHT(); y++) {
            if (y > 0) {
                System.out.print("\n");
            }
            for (int x = 0; x < Maze.getWIDTH(); x++) {
                if (maze[y][x].getCellType() == CellType.WALL) {
                    System.out.print("#");
                }
                if (maze[y][x].getCellType() == CellType.EMPTY) {
                    System.out.print(' ');
                }
                if (maze[y][x].getCellType() == CellType.HUNTER) {
                    System.out.print('@');
                }
                if (maze[y][x].getCellType() == CellType.RELIC) {
                    System.out.print('^');
                }
                if (maze[y][x].getCellType() == CellType.GUARDIAN) {
                    System.out.print('!');
                }
                if (maze[y][x].getCellType() == CellType.DEAD_HUNTER) {
                    System.out.print('X');
                }
                if (maze[y][x].getCellType() == CellType.INVISIBLE_WALL) {
                    System.out.print('.');
                }
                if (maze[y][x].getCellType() == CellType.INVISIBLE_EMPTY) {
                    System.out.print('.');
                }
            }
        }
        System.out.println("\nTotal number of relics to be collected: " + (game.getRelicsNeeded()) + "\n" +
                "Number of relics currently in possession: " + game.getRelicCount());
    }

    private static void printFullMaze(MazeCell[][] maze) {
        System.out.println("\nMaze:");
        for (int y = 0; y < Maze.getHEIGHT(); y++) {
            if (y > 0) {
                System.out.print("\n");
            }
            for (int x = 0; x < Maze.getWIDTH(); x++) {
                if (maze[y][x].getCellType() == CellType.WALL) {
                    System.out.print("#");
                }
                if (maze[y][x].getCellType() == CellType.EMPTY) {
                    System.out.print(' ');
                }
                if (maze[y][x].getCellType() == CellType.HUNTER) {
                    System.out.print('@');
                }
                if (maze[y][x].getCellType() == CellType.RELIC) {
                    System.out.print('^');
                }
                if (maze[y][x].getCellType() == CellType.GUARDIAN) {
                    System.out.print('!');
                }
                if (maze[y][x].getCellType() == CellType.DEAD_HUNTER) {
                    System.out.print('X');
                }
                if (maze[y][x].getCellType() == CellType.INVISIBLE_WALL) {
                    System.out.print('#');
                }
                if (maze[y][x].getCellType() == CellType.INVISIBLE_EMPTY) {
                    System.out.print(' ');
                }
            }
        }
        System.out.println("\nTotal number of relics to be collected: " + (game.getRelicsNeeded()) + "\n" +
                "Number of relics currently in possession: " + game.getRelicCount());
    }


    private static void checkExtraCommand(MazeCell[][] maze) {
        String command = game.getExtraCommand();
        if (command.equals("?")) {
            instructions();
        }
        if (command.equals("C")) {
            System.out.println("Cheat activated: Only need 1 Relic to Win!");
        }
        if (command.equals("M")) {
            System.out.println("Cheat activated: Map revealed!");
            printFullMaze(maze);
        }
    }

    private static String question() {
        boolean validAnswer = false;
        String nextMove;
        System.out.print("Enter your move [WASD?]:");
        Scanner console = new Scanner(System.in);
        nextMove = console.nextLine();
        nextMove = nextMove.toUpperCase();
        if (nextMove.equals("W")) {
            validAnswer = true;
        }
        if (nextMove.equals("S")) {
            validAnswer = true;
        }
        if (nextMove.equals("A")) {
            validAnswer = true;
        }
        if (nextMove.equals("D")) {
            validAnswer = true;
        }
        if (nextMove.equals("?")) {
            validAnswer = true;
        }
        if (nextMove.equals("C")) {
            validAnswer = true;
        }
        if (nextMove.equals("M")) {
            validAnswer = true;
        }

        if (!validAnswer) {
            System.out.println("Invalid move. Please enter just A (left), S (down), D (right), or W (up).");
            nextMove = question();
        }
        return nextMove;
    }


    public static void main(String[] args) {
        instructions();
        game = new Game();
        print(Game.getMyMaze());
    }
}
