import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Day3 {

    public static void Start() {
        System.out.println("2024 Day 3 start");

        // open file and loop through to gather raw inputs
        StringBuilder inputMemory = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("./input3.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputMemory.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        inputArray = new ArrayList<>();
//        inputArray.add(new ArrayList<>(List.of(57L, 59L, 61L, 64L, 65L, 62L, 66L )));

        System.out.println("inputMemory size: " + inputMemory.length());
        System.out.println("inputMemory: " + inputMemory);
        String cleanMemory = inputMemory.toString();

        System.out.println("cleanMemory: " + cleanMemory);
        String[] cleanMemory2 = cleanMemory.split("mul\\(");
        System.out.println("cleanMemory2: " + Arrays.toString(cleanMemory2));
        for (int i = 0; i < cleanMemory2.length; i++) {
            System.out.println("cleanMemory2 1: " + cleanMemory2[i]);
            if (cleanMemory2[i].isEmpty() || !cleanMemory2[i].contains(")")) {
                continue;
            }
            cleanMemory2[i] = chopAt(cleanMemory2[i], cleanMemory2[i].indexOf(")"));
            if (cleanMemory2[i].lastIndexOf(",") != cleanMemory2[i].indexOf(",")) {
                // remove the last comma
                cleanMemory2[i] = cleanMemory2[i].substring(0, cleanMemory2[i].lastIndexOf(","));
            }
            System.out.println("cleanMemory2 2: " + cleanMemory2[i]);
        }
        // now it's a list of strings that are comma separated numbers
        // split and multiply, skipping if split does not produce 2 numbers
        long total = 0;
        for (String s : cleanMemory2) {
            System.out.println("s: " + s);
            String[] nums = s.split(",");
            if (nums.length != 2) {
                continue;
            }

            boolean allNumbers = true;
            for (int i = 0; i < nums.length; i++) {
                if (!nums[i].matches("\\d+")) {
                    allNumbers = false;
                    break;
                }
            }
            if (!allNumbers) {
                continue;
            }

            try {
                long num1 = Long.parseLong(nums[0]);
                long num2 = Long.parseLong(nums[1]);
                total += (num1 * num2);
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException: " + e);
            }

        }
        System.out.println("total: " + total);

        System.out.println("2024 Day 3 end");
    }

    private static String chopAt(String str, int pos) {
        String result = str;
        if (pos > 0) {
            result = str.substring(0, pos);
        }
        return result;
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


}
