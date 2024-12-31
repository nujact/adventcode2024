import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day5 {

    public static void Start() {
        System.out.println("2024 Day 5 start");

        // open file and loop through to gather raw inputs
        StringBuilder inputMemory = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("./inputs/input4.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputMemory.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputMemory = new StringBuilder();
        inputMemory.append(
                "47|53\n" +
                        "97|13\n" +
                        "97|61\n" +
                        "97|47\n" +
                        "75|29\n" +
                        "61|13\n" +
                        "75|53\n" +
                        "29|13\n" +
                        "97|29\n" +
                        "53|29\n" +
                        "61|53\n" +
                        "97|53\n" +
                        "61|29\n" +
                        "47|13\n" +
                        "75|47\n" +
                        "97|75\n" +
                        "47|61\n" +
                        "75|61\n" +
                        "47|29\n" +
                        "75|13\n" +
                        "53|13\n" +
                        "\n" +
                        "75,47,61,53,29\n" +
                        "97,61,53,29,13\n" +
                        "75,29,13\n" +
                        "75,97,47,61,53\n" +
                        "61,13,29\n" +
                        "97,13,75,29,47\n"
        );

        System.out.println("inputMemory size: " + inputMemory.length());
        //System.out.println("inputMemory: " + inputMemory);

        List<String> inputRules = getPageOrderRules(inputMemory);
        List<List<String>> inputUpdates = getUpdates(inputMemory);

//        // print page order rules
//        System.out.println("inputRulesRows:" + inputRulesRows.length);
//        for (int i = 0; i < inputRulesRows.length; i++) {
//            System.out.println(inputRulesRows[i]);
//        }
//        // print updates
//        System.out.println("\ninputUpdateRows:" + inputUpdateRows.length);
//        for (int i = 0; i < inputUpdateRows.length; i++) {
//            System.out.println(inputUpdateRows[i]);
//        }

        // walk thru updateList
        for (List<String> update : inputUpdates) {
            for (String updatePage : update) {
                ValidateOrder(inputRules, updatePage, update);
            }
        }



        //part1(cellMatrix);
        //part2(cellMatrix);


        System.out.println("2024 Day 5 end");
    }

    private static void ValidateOrder(List<String> inputRules, String updatePage, List<String> update) {
        // find pages that should occur before updatePage
        if (inputRules.contains("|" + updatePage)) {
            System.out.println("updatePage: " + updatePage + " found in inputRules");
            // find pages that should occur before updatePage
            for (String rule : inputRules.stream().allMatch( rule -> rule.contains("|" + updatePage))) {
                System.out.println("rule: " + rule);

            }
        }



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

    private static List<String> getPageOrderRules(StringBuilder inputMemory) {
        // split by new line into array
        String[] inputRows = inputMemory.toString().split("\n");
        List<String> pageOrderRulesList = new ArrayList<>();
        for (int i = 0; i < inputRows.length; i++) {
            // add rows till empty line
            if (inputRows[i].isEmpty()) break;
            pageOrderRulesList.add(inputRows[i]);
        }
        return pageOrderRulesList;
    }

    private static List<List<String>> getUpdates(StringBuilder inputMemory) {
        // split by new line into array
        String[] inputRows = inputMemory.toString().split("\n");
        List<List<String>> updateList  = new ArrayList<>();
        boolean seenEmptyLine = false;
        for (int i = 0; i < inputRows.length; i++) {
            // skip rows till empty line
            if (!seenEmptyLine) {
                if (inputRows[i].isEmpty()) {
                    seenEmptyLine = true;
                }
                continue;
            }
            var update = inputRows[i].split(",");
            updateList.add(List.of(update));
        }
        return updateList;
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

