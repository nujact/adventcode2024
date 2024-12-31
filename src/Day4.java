import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day4 {

    public static void Start() {
        System.out.println("2024 Day 4 start");

        // open file and loop through to gather raw inputs
        StringBuilder inputMemory = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("./input4.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputMemory.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        inputMemory = new StringBuilder();
//        inputMemory.append(
//                "MMMSXXMASM\n" +
//                        "MSAMXMSMSA\n" +
//                        "AMXSXMAAMM\n" +
//                        "MSAMASMSMX\n" +
//                        "XMASAMXAMM\n" +
//                        "XXAMMXXAMA\n" +
//                        "SMSMSASXSS\n" +
//                        "SAXAMASAAA\n" +
//                        "MAMMMXMMMM\n" +
//                        "MXMXAXMASX");

        System.out.println("inputMemory size: " + inputMemory.length());
        //System.out.println("inputMemory: " + inputMemory);

        char[][] inputMatrix = getInputMatrix(inputMemory);
        Cell[][] cellMatrix = getCellMatrix(inputMemory);

        // print matrix
        System.out.println("inputMatrix[X][Y]: X " + inputMatrix.length + " Y " + inputMatrix[0].length);
        for (int i = 0; i < inputMatrix.length; i++) {
            for (int j = 0; j < inputMatrix[i].length; j++) {
                System.out.print(inputMatrix[i][j]);
            }
            System.out.println();
        }

        System.out.println("\ncellMatrix:");
        for (int row = 0; row < cellMatrix.length; row++) {
            for (int col = 0; col < cellMatrix[row].length; col++) {
                System.out.print(cellMatrix[row][col].value);
            }
            System.out.println();
        }

        //part1(cellMatrix);
        part2(cellMatrix);


        System.out.println("2024 Day 4 end");
    }

    private static void part2(Cell[][] cellMatrix) {
        // search for MAS MAS in X direction

        // for each cell in cellMatrix
        // calc 4 letter words in compassDirections around it
        for (int row = 0; row < cellMatrix.length; row++) {
            for (int col = 0; col < cellMatrix[row].length; col++) {
                System.out.print(cellMatrix[row][col].value + " r,c " + row + "," + col + " ");
                CalcMasList(cellMatrix, row, col);
                System.out.println("masList: " + cellMatrix[row][col].starList.size() + " " + cellMatrix[row][col].starList);
            }
            System.out.println();
        }
        System.out.println();

        // count hits of MAS in all starlists
        int masCount = 0;

        for (int row = 0; row < cellMatrix.length; row++) {
            for (int col = 0; col < cellMatrix[row].length; col++) {
                // search for A in middle of X-MAS
                if (cellMatrix[row][col].value.equals("A")) {
                    String nwWord = "";
                    String neWord = "";
                    String swWord = "";
                    String seWord = "";
                    // check if valid to get corner
                    if (row - 1 >= 0 && col - 1 >= 0) {
                        nwWord = cellMatrix[row - 1][col - 1].starList.get("SE");
                    }
                    if (row - 1 >= 0 && col + 1 <= cellMatrix[row].length - 1) {
                        neWord = cellMatrix[row - 1][col + 1].starList.get("SW");
                    }
                    if (row + 1 <= cellMatrix.length - 1 && col - 1 >= 0) {
                        swWord = cellMatrix[row + 1][col - 1].starList.get("NE");
                    }
                    if (row + 1 <= cellMatrix.length - 1 && col + 1 <= cellMatrix[row].length - 1) {
                        seWord = cellMatrix[row + 1][col + 1].starList.get("NW");
                    }

                    // count MAS in corners
                    int foundMas = 0;
                    if (nwWord.equals("MAS")) {
                        foundMas++;
                    }
                    if (neWord.equals("MAS")) {
                        foundMas++;
                    }
                    if (swWord.equals("MAS")) {
                        foundMas++;
                    }
                    if (seWord.equals("MAS")) {
                        foundMas++;
                    }
                    // if 2 or more MAS found in corners, increment xmas count
                    if (foundMas >= 2) {
                        masCount++;
                    }
                }

            }
        }

        System.out.println("masCount: " + masCount);
        System.out.println();
    }

    private static void part1(Cell[][] cellMatrix) {
        // for each cell in cellMatrix
        // calc 4 letter words in compassDirections around it
        for (int row = 0; row < cellMatrix.length; row++) {
            for (int col = 0; col < cellMatrix[row].length; col++) {
                System.out.print(cellMatrix[row][col].value + " r,c " + row + "," + col + " ");
                CalcStarList(cellMatrix, row, col);
                System.out.println("starList: " + cellMatrix[row][col].starList.size() + " " + cellMatrix[row][col].starList);
            }
            System.out.println();
        }
        System.out.println();

        // count hits of XMAS in all starlists
        int xmasCount = 0;
        for (int row = 0; row < cellMatrix.length; row++) {
            for (int col = 0; col < cellMatrix[row].length; col++) {
                for (String starWord : cellMatrix[row][col].starList.values()) {
                    if (starWord.equals("XMAS")) {
                        xmasCount++;
                    }
                }
            }

        }
        System.out.println("xmasCount: " + xmasCount);
        System.out.println();
    }

    private static void CalcMasList(Cell[][] cellMatrix, int row, int col) {
        for (String compassDirection : CompassDirections) {
            // only care about NW NE SW SE
            switch (compassDirection) {
                case "N", "E", "S", "W":
                    break;

                case "NE":
                    if (row - 2 < 0 || col + 2 > cellMatrix[row].length - 1) {
                        break;
                    }
                    cellMatrix[row][col].starList.put(compassDirection, cellMatrix[row][col].value +
                            cellMatrix[row - 1][col + 1].value +
                            cellMatrix[row - 2][col + 2].value);
                    break;

                case "SE":
                    if (row + 2 > cellMatrix.length - 1 || col + 2 > cellMatrix[row].length - 1) {
                        break;
                    }
                    cellMatrix[row][col].starList.put(compassDirection, cellMatrix[row][col].value +
                            cellMatrix[row + 1][col + 1].value +
                            cellMatrix[row + 2][col + 2].value);
                    break;

                case "SW":
                    if (row + 2 > cellMatrix.length - 1 || col - 2 < 0) {
                        break;
                    }
                    cellMatrix[row][col].starList.put(compassDirection, cellMatrix[row][col].value +
                            cellMatrix[row + 1][col - 1].value +
                            cellMatrix[row + 2][col - 2].value);
                    break;

                case "NW":
                    if (row - 2 < 0 || col - 2 < 0) {
                        break;
                    }
                    cellMatrix[row][col].starList.put(compassDirection, cellMatrix[row][col].value +
                            cellMatrix[row - 1][col - 1].value +
                            cellMatrix[row - 2][col - 2].value);
                    break;

                default:
                    System.out.println("Invalid compass direction");
                    break;
            }
        }
    }

    private static void CalcStarList(Cell[][] cellMatrix, int row, int col) {
        for (String compassDirection : CompassDirections) {
            switch (compassDirection) {
                case "N":
                    if (row - 3 < 0) {
                        break;
                    }
                    cellMatrix[row][col].starList.put(compassDirection, cellMatrix[row][col].value +
                            cellMatrix[row - 1][col].value +
                            cellMatrix[row - 2][col].value +
                            cellMatrix[row - 3][col].value);
                    break;

                case "NE":
                    if (row - 3 < 0 || col + 3 > cellMatrix[row].length - 1) {
                        break;
                    }
                    cellMatrix[row][col].starList.put(compassDirection, cellMatrix[row][col].value +
                            cellMatrix[row - 1][col + 1].value +
                            cellMatrix[row - 2][col + 2].value +
                            cellMatrix[row - 3][col + 3].value);
                    break;

                case "E":
                    if (col + 3 > cellMatrix[row].length - 1) {
                        break;
                    }
                    cellMatrix[row][col].starList.put(compassDirection, cellMatrix[row][col].value +
                            cellMatrix[row][col + 1].value +
                            cellMatrix[row][col + 2].value +
                            cellMatrix[row][col + 3].value);
                    break;

                case "SE":
                    if (row + 3 > cellMatrix.length - 1 || col + 3 > cellMatrix[row].length - 1) {
                        break;
                    }
                    cellMatrix[row][col].starList.put(compassDirection, cellMatrix[row][col].value +
                            cellMatrix[row + 1][col + 1].value +
                            cellMatrix[row + 2][col + 2].value +
                            cellMatrix[row + 3][col + 3].value);
                    break;

                case "S":
                    if (row + 3 > cellMatrix.length - 1) {
                        break;
                    }
                    cellMatrix[row][col].starList.put(compassDirection, cellMatrix[row][col].value +
                            cellMatrix[row + 1][col].value +
                            cellMatrix[row + 2][col].value +
                            cellMatrix[row + 3][col].value);
                    break;

                case "SW":
                    if (row + 3 > cellMatrix.length - 1 || col - 3 < 0) {
                        break;
                    }
                    cellMatrix[row][col].starList.put(compassDirection, cellMatrix[row][col].value +
                            cellMatrix[row + 1][col - 1].value +
                            cellMatrix[row + 2][col - 2].value +
                            cellMatrix[row + 3][col - 3].value);
                    break;

                case "W":
                    if (col - 3 < 0) {
                        break;
                    }
                    cellMatrix[row][col].starList.put(compassDirection, cellMatrix[row][col].value +
                            cellMatrix[row][col - 1].value +
                            cellMatrix[row][col - 2].value +
                            cellMatrix[row][col - 3].value);
                    break;

                case "NW":
                    if (row - 3 < 0 || col - 3 < 0) {
                        break;
                    }
                    cellMatrix[row][col].starList.put(compassDirection, cellMatrix[row][col].value +
                            cellMatrix[row - 1][col - 1].value +
                            cellMatrix[row - 2][col - 2].value +
                            cellMatrix[row - 3][col - 3].value);
                    break;

                default:
                    System.out.println("Invalid compass direction");
                    break;
            }
        }

    }

    private static final List<String> CompassDirections = List.of("N", "NE", "E", "SE", "S", "SW", "W", "NW");

    private static class Cell {
        String value = "";
        Map<String, String> starList = new HashMap<String, String>();

        Cell(char inChar) {
            value = String.valueOf(inChar);
            for (String compassDirection : CompassDirections) {
                starList.put(compassDirection, "");
            }
        }

    }


    private static char[][] getInputMatrix(StringBuilder inputMemory) {
        // split by new line into array
        String[] inputRows = inputMemory.toString().split("\n");
        // convert to matrix of characters
        char[][] inputMatrix = new char[inputRows.length][inputRows[0].length()];
        for (int i = 0; i < inputRows.length; i++) {
            inputMatrix[i] = inputRows[i].toCharArray();
        }
        return inputMatrix;
    }


    // convert input rows of letters to matrix of Cells
    private static Cell[][] getCellMatrix(StringBuilder inputMemory) {
        // split by new line into array
        String[] inputRows = inputMemory.toString().split("\n");
        // convert to matrix of Cells
        Cell[][] cellMatrix = new Cell[inputRows.length][inputRows[0].length()];
        for (int row = 0; row < inputRows.length; row++) {
            for (int col = 0; col < inputRows[row].length(); col++) {
                cellMatrix[row][col] = new Cell(inputRows[row].toCharArray()[col]);
            }
        }
        return cellMatrix;
    }
}

