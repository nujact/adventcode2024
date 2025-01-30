import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Day11 {

    public static void Start() {
        System.out.println("2024 Day 11 start");

        // open file and loop through to gather raw inputs
        StringBuilder inputMemory = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("./inputs/input11.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputMemory.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputMemory = new StringBuilder();
        inputMemory.append("0 1 10 99 999");

        System.out.println("inputMemory size: " + inputMemory.length());

        Stones stones = new Stones(inputMemory.toString());
        stones.Print();
        stones.Blink();
        System.out.println("After blink");
        stones.Print();

        System.out.println("2024 Day 11 end");
    }

    private static class Stones {
        public Integer[] values;

        public Stones(String inputMemory) {
            String[] inputArray = inputMemory.split(" ");
            values = new Integer[inputArray.length];
            for (int i = 0; i < inputArray.length; i++) {
                values[i] = Integer.parseInt(inputArray[i]);
            }
        }

        public void Blink() {
            for (int i = 0; i < values.length; i++) {
                values[i] = ApplyRules(values[i]);
            }
        }

        private int ApplyRules(int value) {
            if (value == 0) {
                return 1;
            } else if (String.valueOf(value).length() % 2 == 0) {
                System.out.println("Even number of digits: " + value);
                return value / 2;
            } else {
                return value * 2024;
            }
        }

        public void Print() {
            for (int i = 0; i < values.length; i++) {
                System.out.print(values[i] + " ");
            }
            System.out.println();
        }
    }

}




