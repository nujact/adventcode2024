import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day6 {

    public static void Start() {
        System.out.println("2024 Day 6 start");

        // open file and loop through to gather raw inputs
        StringBuilder inputMemory = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("./inputs/input6.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputMemory.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        inputMemory = new StringBuilder();
//        inputMemory.append("....#.....\n" +
//                ".........#\n" +
//                "..........\n" +
//                "..#.......\n" +
//                ".......#..\n" +
//                "..........\n" +
//                ".#..^.....\n" +
//                "........#.\n" +
//                "#.........\n" +
//                "......#..."
//        );

        System.out.println("inputMemory size: " + inputMemory.length());
        //System.out.println("inputMemory: " + inputMemory);

        Matrix matrix = new Matrix(inputMemory.toString());

        Part1(matrix);

        System.out.println("2024 Day 6 end");
    }

    private static void Part1(Matrix matrix) {
        matrix.Print();

        while (matrix.MoveGuard()) {

        }
        matrix.Print();
        System.out.println("Guard "+ matrix.guard.row +" "+ matrix.guard.col +" "+ matrix.guard.direction );

        int visitedCells = 0;
        for (Cell[] row : matrix.cellMatrix) {
            for (Cell col : row) {
                if (col.visited) visitedCells++;
            }
        }
        System.out.println("visited count " + visitedCells);
    }

    public static class Matrix {
        String[] inputStrings;
        Cell[][] cellMatrix;
        Guard guard;

        Matrix(String inputString) {
            inputStrings = inputString.split("\n");
            cellMatrix = new Cell[inputStrings.length][inputStrings[0].length()];
            for (int j = 0; j < cellMatrix.length; j++) {
                for (int i = 0; i < cellMatrix[j].length; i++) {
                    cellMatrix[j][i] = new Cell();
                    char inputStr = inputStrings[j].charAt(i);
                    if (inputStr == '#') {
                        cellMatrix[j][i].obstructed = true;
                    }
                    if (inputStr == '^') {
                        guard = new Guard();
                        guard.row = j;
                        guard.col = i;
                        cellMatrix[j][i].visited = true;
                    }
                }
            }

        }

        public void Print() {
            System.out.print("\nMatrix:\n");
            for (int j = 0; j < cellMatrix.length; j++) {
                Cell[] row = cellMatrix[j];
                for (int i = 0; i < row.length; i++) {
                    Cell col = row[i];
                    System.out.print(col.obstructed ? "#" : (guard.row == j && guard.col == i) ? '^' : col.visited ? "x" : ".");
                }
                System.out.println("");
            }
        }

        public boolean MoveGuard() {
            boolean validMove = true;

            switch (guard.direction) {
                case "N":
                    if (guard.row - 1 < 0) {
                        validMove = false;
                        break;
                    }
                case "E":
                    if (guard.col + 1 > cellMatrix[0].length - 1) {
                        validMove = false;
                        break;
                    }
                case "S":
                    if (guard.row + 1 > cellMatrix.length - 1) {
                        validMove = false;
                        break;
                    }
                case "W":
                    if (guard.col - 1 < 0) {
                        validMove = false;
                        break;
                    }
                default:
                    break;
            }

            if (validMove) {
                switch (guard.direction) {
                    case "N":
                        guard.row--;
                        break;
                    case "E":
                        guard.col++;
                        break;
                    case "S":
                        guard.row++;
                        break;
                    case "W":
                        guard.col--;
                        break;
                }
                var cell = cellMatrix[guard.row][guard.col];
                if (cell.obstructed) {
                    // backup and turn right
                    switch (guard.direction) {
                        case "N":
                            guard.row++;
                            guard.direction = "E";
                            break;
                        case "E":
                            guard.col--;
                            guard.direction = "S";
                            break;
                        case "S":
                            guard.row--;
                            guard.direction = "W";
                            break;
                        case "W":
                            guard.col++;
                            guard.direction = "N";
                            break;
                    }
                } else {
                    cellMatrix[guard.row][guard.col].visited = true;
                }
            }

            return validMove;
        }
    }

    public static class Cell {
        boolean obstructed = false;
        boolean visited = false;
    }

    private static final List<String> CompassDirections = List.of("N", "E", "S", "W");

    public static class Guard {
        int row = 0;
        int col = 0;
        String direction = "N";
    }
}

