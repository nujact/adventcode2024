import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day8 {

    public static void Start() {
        System.out.println("2024 Day 8 start");

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

        inputMemory = new StringBuilder();
        inputMemory.append("............\n" +
                "........0...\n" +
                ".....0......\n" +
                ".......0....\n" +
                "....0.......\n" +
                "......A.....\n" +
                "............\n" +
                "............\n" +
                "........A...\n" +
                ".........A..\n" +
                "............\n" +
                "............"
        );

        System.out.println("inputMemory size: " + inputMemory.length());
        //System.out.println("inputMemory: " + inputMemory);

        Matrix matrix = new Matrix(inputMemory.toString());
        matrix.CalcAntiNodes();

        matrix.Print();

        int countAntiNodes = 0;
        for (int row = 0; row < matrix.cellMatrix.length; row++) {
            for (int col = 0; col < matrix.cellMatrix[row].length; col++) {
                Cell cell = matrix.cellMatrix[row][col];
                if (!cell.antiNodeList.isEmpty()) {
                    countAntiNodes++;
                    System.out.print(" " + row + "," + col + " " + cell.antennaFrequency + " antiNodes: ");
                    for (String antiNode : cell.antiNodeList) {
                        System.out.print(antiNode + " ");
                    }
                    System.out.println();
                }
            }
        }
        System.out.println("countAntiNodes: " + countAntiNodes);

        System.out.println("2024 Day 8 end");
    }

    public static class Cell {
        String antennaFrequency;
        List<String> antiNodeList;
        List<Integer[]> brotherList;
    }

    public static class Matrix {
        String[] inputStrings;
        Cell[][] cellMatrix;

        Matrix(String inputString) {
            inputStrings = inputString.split("\n");
            cellMatrix = new Cell[inputStrings.length][inputStrings[0].length()];
            for (int j = 0; j < cellMatrix.length; j++) {
                for (int i = 0; i < cellMatrix[j].length; i++) {
                    cellMatrix[j][i] = new Cell();
                    char inputStr = inputStrings[j].charAt(i);
                    if (inputStr != '.')
                        cellMatrix[j][i].antennaFrequency = inputStr + "";
                    else
                        cellMatrix[j][i].antennaFrequency = "";
                    cellMatrix[j][i].antiNodeList = new ArrayList<>();
                }
            }
        }

        public void Print() {
            System.out.print("\nMatrix:\n");
            for (Cell[] matrixRow : cellMatrix) {
                for (Cell cell : matrixRow) {
                    if (!cell.antennaFrequency.isEmpty()) System.out.print(cell.antennaFrequency);
                    else if (!cell.antiNodeList.isEmpty()) System.out.print("#");
                    else System.out.print(".");
                }
                System.out.println();
            }
            System.out.println();
        }

        public void CalcAntiNodes() {
            // loop through all cells, rows first, and calculate antiNodes
            System.out.println("CalcAntiNodes ");
            for (int row = 0; row < cellMatrix.length; row++) {
                for (int col = 0; col < cellMatrix[row].length; col++) {
                    // get the cell that is being processed
                    Cell cell = cellMatrix[row][col];
                    if (!cell.antennaFrequency.isEmpty()) {
                        // cell has an antenna, find its brothers
                        System.out.print("\n" + row + "," + col + " antennaFreq " + cell.antennaFrequency);
                        cell.brotherList = GetBrotherList(cell.antennaFrequency, row, col);
                        System.out.print(" brothers: " + cell.brotherList.size());

                        for (Integer[] brother : cell.brotherList) {
                            int brotherRow = brother[0];
                            int brotherCol = brother[1];

                            int fromAntiNodeRow = row - (brotherRow - row);
                            int fromAntiNodeCol = col - (brotherCol - col);
                            System.out.print(" bro " + brotherRow + "," + brotherCol + " from-anti " + fromAntiNodeRow + "," + fromAntiNodeCol);
                            if (fromAntiNodeRow >= 0 && fromAntiNodeCol >= 0 && fromAntiNodeRow < cellMatrix.length && fromAntiNodeCol < cellMatrix[0].length) {
                                cellMatrix[fromAntiNodeRow][fromAntiNodeCol].antiNodeList.add(cell.antennaFrequency);
                                System.out.print(" +++ ");
                            } else System.out.print(" --- ");

                            int intoAntiNodeRow = brotherRow + (brotherRow - row);  // 1,8 into 4,4 = 7,0
                            int intoAntiNodeCol = brotherCol + (brotherCol - col);
                            System.out.print(" into-anti " + intoAntiNodeRow + "," + intoAntiNodeCol);
                            if (intoAntiNodeRow >= 0 && intoAntiNodeCol >= 0 && intoAntiNodeRow < cellMatrix.length && intoAntiNodeCol < cellMatrix[0].length) {
                                cellMatrix[intoAntiNodeRow][intoAntiNodeCol].antiNodeList.add(cell.antennaFrequency);
                                System.out.print(" +++ ");
                            } else System.out.print(" --- ");
                        }

                    }
                }
            }
        }

        public List<Integer[]> GetBrotherList(String antennaFrequency, int row, int col) {
            List<Integer[]> brotherList = new ArrayList<>();

//            boolean needNewLine = false;
            for (int j = 0; j < cellMatrix.length; j++) {
                for (int i = 0; i < cellMatrix[j].length; i++) {
                    if (cellMatrix[j][i].antennaFrequency.equals(antennaFrequency) && j != row && i != col) {
                        Integer[] brother = new Integer[]{j, i};
//                        if (!needNewLine)
//                            System.out.print(row + "," + col + " antennaFreq " + antennaFrequency + " brothers");
//                        System.out.print(" " + brother[0] + "," + brother[1]);
//                        needNewLine = true;
                        brotherList.add(brother);
                    }
                }
            }
//            if (needNewLine) System.out.println();

            return brotherList;
        }


    }


}




