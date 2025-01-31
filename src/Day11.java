import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

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

//        inputMemory = new StringBuilder();
//        inputMemory.append("125 17");

        System.out.println("inputMemory size: " + inputMemory.length());

        Stones stones = new Stones(inputMemory.toString());
        stones.Print();

        for (int i = 1; i < 76; i++) {
            stones.Blink();
            System.out.println("After blink " + i + " stones count: " + stones.values.size());
            //stones.Print();
        }

        System.out.println("Stones count: " + stones.values.size());
        // 218079 is the correct answer part 1

//        for (int i = 1; i < 51; i++) {
//            stones.Blink();
//            System.out.println("After blink " + i);
//            //stones.Print();
//        }
//        System.out.println("Stones count: " + stones.values.size());
//        //

        System.out.println("2024 Day 11 end");
    }

    private static class Stones {
        public ArrayList<Long> values = new ArrayList<>();

        public Stones(String inputMemory) {
            String[] inputArray = inputMemory.split(" ");
            for (int i = 0; i < inputArray.length; i++) {
                values.add(Long.parseLong(inputArray[i]));
            }
        }

        public void Blink() {
            for (int i = 0; i < values.size(); i++) {
                if (ApplyRulesOnArrayElement(i)) {
                    i++;
                }
            }
        }

        private boolean ApplyRulesOnArrayElement(int position) {
            Long value = values.get(position);
            int halfLength;
            long leftStone;
            long rightStone;
            boolean inserted = false;
            String valueString = String.valueOf(value);
            if (value == 0) {
                values.set(position, 1L);
            } else if (valueString.length() % 2 == 0) {
                // split into 2 stones, drop leading 0 on the right stone
                halfLength = valueString.length() / 2;
                leftStone = Long.parseLong(valueString.substring(0, halfLength));
                rightStone = Long.parseLong(valueString.substring(halfLength));
                values.set(position, leftStone);
                values.add(position + 1, rightStone);
                inserted = true;
            } else {
                // multiply by 2024
                values.set(position, value * 2024);
            }
            return inserted;
        }

        public void Print() {
            for (int i = 0; i < values.size(); i++) {
                System.out.print(values.get(i) + " ");
            }
            System.out.println();
        }
    }

}




