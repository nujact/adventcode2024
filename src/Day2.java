import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Day2 {
    public static void Start() {
        System.out.println("2024-2 start");

        // open file and loop through to gather raw inputs
        ArrayList<ArrayList<Long>> inputArray = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("./input2.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                gatherInputs2(line, inputArray);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        inputArray = new ArrayList<>();
//        inputArray.add(new ArrayList<>(List.of(57L, 59L, 61L, 64L, 65L, 62L, 66L )));

        System.out.println("inputArray size: " + inputArray.size());

        // process the inputs
        int safeCount = 0;
        int safeCount2 = 0;
        for (ArrayList<Long> row : inputArray) {
            //safeCount += processInputs2(row);
            ArrayList<Long> row2 = new ArrayList<>(row);
            int safe2 = processInputs2pt2(row, true);
            if (safe2 == 0 && row.size() < row2.size()) {
                safe2 = processInputs2pt2(row, false);
            }
            safeCount2 += safe2;
        }
        System.out.println("safeCount: " + safeCount);
        System.out.println("safeCount2: " + safeCount2);

        System.out.println("2024-2 end");
    }

    public static int processInputs2(ArrayList<Long> rowList) {
        int isSafeRisingOrFalling = 0;
        // validate that rowList is rising across the row
        for (int i = 0; i < rowList.size() - 1; i++) {
            if (rowList.get(i) < rowList.get(i + 1)) {
                isSafeRisingOrFalling = 1;
            } else {
                isSafeRisingOrFalling = 0;
                break;
            }
        }

        // validate only if rising is false
        if (isSafeRisingOrFalling == 0) {
            // validate that rowList is falling across the row
            for (int i = 0; i < rowList.size() - 1; i++) {
                if (rowList.get(i) > rowList.get(i + 1)) {
                    isSafeRisingOrFalling = 1;
                } else {
                    isSafeRisingOrFalling = 0;
                    break;
                }
            }
        }

        // validate that diff between each is less than or equal to 3
        int isSafeGradual = 0;
        if (isSafeRisingOrFalling == 1) {

            for (int i = 0; i < rowList.size() - 1; i++) {
                long diff = Math.abs(rowList.get(i + 1) - rowList.get(i));
                if (diff <= 3) {
                    isSafeGradual = 1;
                } else {
                    isSafeGradual = 0;
                    break;
                }
            }
        }
        return (isSafeRisingOrFalling * isSafeGradual);
    }

    public static int processInputs2pt2(ArrayList<Long> rowList, boolean allowForgive) {
        System.out.println("rowList: " + rowList);

        int forgiveRowNum = -1;
        int isSafeRisingOrFalling = 0;

        // validate that rowList is rising across the row
        for (int i = 0; i < rowList.size() - 1; i++) {
            if (rowList.get(i) < rowList.get(i + 1)) {
                isSafeRisingOrFalling = 1;
            } else {
                if (allowForgive) {
                    forgiveRowNum = i + 1;
                    allowForgive = false;
                    isSafeRisingOrFalling = 1;
                } else {
                    isSafeRisingOrFalling = 0;
                    break;
                }
            }
        }

        // validate only if rising is false
        if (isSafeRisingOrFalling == 0) {
            // validate that rowList is falling across the row
            for (int i = 0; i < rowList.size() - 1; i++) {
                if (i == forgiveRowNum) {
                    continue;
                }
                if (rowList.get(i) > rowList.get(i + 1)) {
                    isSafeRisingOrFalling = 1;
                } else {
                    if (allowForgive) {
                        forgiveRowNum = i + 1;
                        allowForgive = false;
                        isSafeRisingOrFalling = 1;
                    } else {
                        isSafeRisingOrFalling = 0;
                        break;
                    }
                }
            }
        }

        // validate that diff between each is less than or equal to 3
        int isSafeGradual = 0;
        if (isSafeRisingOrFalling == 1) {

            for (int i = 0; i < rowList.size() - 1; i++) {
                if (i == forgiveRowNum) {
                    continue;
                }
                long diff = Math.abs(rowList.get(i + 1) - rowList.get(i));
                if (diff <= 3) {
                    isSafeGradual = 1;
                } else {
                    if (allowForgive) {
                        forgiveRowNum = i + 1;
                        isSafeGradual = 1;
                    } else {
                        isSafeGradual = 0;
                        break;
                    }
                }
            }
        }

        if (forgiveRowNum != -1) {
            rowList.remove(forgiveRowNum);
        }

        if ((isSafeRisingOrFalling * isSafeGradual) == 0) {
            System.out.printf("rowList: %s isSafeRisingOrFalling: %d isSafeGradual: %d forgiveRowNum: %d final: %d\n",
                    rowList, isSafeRisingOrFalling, isSafeGradual, forgiveRowNum, (isSafeRisingOrFalling * isSafeGradual));
        }
        return (isSafeRisingOrFalling * isSafeGradual);
    }

    private static void gatherInputs2(String line, ArrayList<ArrayList<Long>> inputArray) {
        // split the line by space
        //System.out.println("line: " + line);
        String[] parts = line.split(" ");
        ArrayList<Long> rowList = new ArrayList<>();
        for (String part : parts) {
            rowList.add(Long.parseLong(part));
        }
        inputArray.add(rowList);
        return;
    }


}
