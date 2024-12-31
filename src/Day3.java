import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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
        String inputStart = inputMemory.toString();

//        long totalPt1 = getTotalPt1(inputStart);
//        System.out.println("total Pt1: " + totalPt1);

        long totalPt2 = getTotalPt2(inputStart);
        System.out.println("total Pt2: " + totalPt2);

        System.out.println("2024 Day 3 end");
    }

    private static long getTotalPt2(String inputStart) {
        Pattern regex = Pattern.compile("do\\(\\)|don't\\(\\)|mul\\((\\d+),(\\d+)\\)");
        Stream<MatchResult> list = regex.matcher(inputStart).results();
        long total = 0;
        boolean doFlag = true;  // true = do, false = don't
        MatchResult[] array = list.toArray(MatchResult[]::new);
        for (int i = 0; i < array.length; i++) {
            MatchResult match = array[i];
            System.out.println("match: " + match.group(0) + " ");
            if (match.group(0).equals("do()")) {
                doFlag = true;
                continue;
            } else if (match.group(0).equals("don't()")) {
                doFlag = false;
                continue;
            }
            if (!doFlag) {
                // skip adding, but continue to next
                continue;
            }
            try {
                long num1 = Long.parseLong(match.group(1));
                long num2 = Long.parseLong(match.group(2));
                total += (num1 * num2);
                //System.out.println("num1: " + num1 + " num2: " + num2 + " total: " + total);
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException: " + e);
            }
        }
        return total;
    }

    private static long getTotalPt1(String inputStart) {
        Pattern regex = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
        Stream<MatchResult> list = regex.matcher(inputStart).results();
        long total = 0;
        MatchResult[] array = list.toArray(MatchResult[]::new);
        for (int i = 0; i < array.length; i++) {
            MatchResult match = array[i];
            System.out.println("match: " + match.group(0) + " ");
            try {
                long num1 = Long.parseLong(match.group(1));
                long num2 = Long.parseLong(match.group(2));
                total += (num1 * num2);
                //System.out.println("num1: " + num1 + " num2: " + num2 + " total: " + total);
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException: " + e);
            }
        }
        return total;
    }

}
