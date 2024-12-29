import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Day1 {

    public static void Start() {
        System.out.println("2024-1 start");

        // open file and loop through to gather raw inputs
        ArrayList<Long> leftList = new ArrayList<>();
        ArrayList<Long> rightList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("./input1.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                gatherInputs1(line, leftList, rightList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("leftList: " + leftList.size());
        System.out.println("rightList: " + rightList.size());

        // process the inputs
        leftList.sort(null);
        rightList.sort(null);
//        for (int i = 0; i < leftList.size(); i++) {
//            System.out.println("leftList: " + leftList.get(i) + " rightList: " + rightList.get(i));
//        }

        long totalDiff = 0L;
        for (int i = 0; i < leftList.size(); i++) {
            totalDiff += processInputs1Pt1(leftList.get(i), rightList.get(i));
        }
        System.out.println("totalDiff: " + totalDiff);

        long totalDiff2 = 0L;
        for (Long aLong : leftList) {
            totalDiff2 += processInputs1Pt2(aLong, rightList);
        }
        System.out.println("totalDiff2: " + totalDiff2);

        System.out.println("2024-1 end");
    }

    private static Long processInputs1Pt2(Long aLong, ArrayList<Long> rightList) {
        int rightCount = Collections.frequency(rightList, aLong);
        return aLong * rightCount;
    }

    private static Long processInputs1Pt1(Long aLong, Long aLong1) {
        return Math.abs(aLong - aLong1);
    }

    private static void gatherInputs1(String line, ArrayList<Long> leftList, ArrayList<Long> rightList) {
        // split the line by space
        //System.out.println("line: " + line);
        String[] parts = line.split(" {3}");
        leftList.add(Long.parseLong(parts[0]));
        rightList.add(Long.parseLong(parts[1]));
        return;
    }
}
