import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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

        // create array of inputs - split by space, parse to long
        long[] stones = Arrays.stream(inputMemory.toString().split(" "))
                .mapToLong(Long::parseLong).toArray();

        // print stones
        System.out.print("starting stones: ");
        for (long stone : stones) {
            System.out.print(stone + " ");
        }
        System.out.println();

        int blinkCount = 75;
        long stoneCount = 0;
        // init timer to measure performance
        long startTime;
        long endTime;
        System.out.println("Blinking " + blinkCount + " times, final row");
        for (long stone : stones) {
            // start timer
            startTime = System.nanoTime();
            long blinkRecurseCount = BlinkRecurse(stone, blinkCount);
            endTime = System.nanoTime();
            stoneCount += blinkRecurseCount;
            System.out.println("blinkRecurseCount: " + blinkRecurseCount + " stoneCount: " + stoneCount + " time: " + (endTime - startTime) / 1000000 + "ms");
        }

        System.out.println("\nstoneCount: " + stoneCount);

        // 218079 is the correct answer part 1
        // 259,755,538,429,618 is the correct answer part 2
        System.out.println("2024 Day 11 end");
    }

    private static long leftStone = 0;
    private static long rightStone = 0;
    private static long stoneCount = 0;
    private static String valueString = "";
    private static Map<Map<Long, Integer>, Long> cachedBlinks = new HashMap<>();

    private static long BlinkRecurse(long stone, int blinkCount) {
        if (blinkCount <= 0) {
            return 1;
        }

        // check cache
        if (cachedBlinks.containsKey(Map.of(stone, blinkCount))) {
            long cachedBlinkResult = cachedBlinks.get(Map.of(stone, blinkCount));
            return cachedBlinkResult;
        }

        // this is the core of a blink step - 3 rules
        // rule 1 - flip 0 to 1
        // rule 2 - even length of number - split into halves (string split)
        // rule 3 - otherwise multiply by 2024
        if (stone == 0) {
            stoneCount = BlinkRecurse(1, blinkCount - 1);
        } else if (String.valueOf(stone).length() % 2 == 0) {
            // split into 2 stones, drop leading 0 on the right stone
            valueString = String.valueOf(stone);
            leftStone = Long.parseLong(valueString.substring(0, valueString.length() / 2));
            stoneCount = BlinkRecurse(leftStone, blinkCount - 1);

            valueString = String.valueOf(stone);
            rightStone = Long.parseLong(valueString.substring(valueString.length() / 2));
            stoneCount += BlinkRecurse(rightStone, blinkCount - 1);
        } else {
            stoneCount = BlinkRecurse(stone * 2024, blinkCount - 1);
        }

        // update cache
        if (!cachedBlinks.containsKey(Map.of(stone, blinkCount)))
            cachedBlinks.put(Map.of(stone, blinkCount), stoneCount);

        return stoneCount;
    }
}




