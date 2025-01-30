import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day10 {

    public static void Start() {
        System.out.println("2024 Day 10 start");

        // open file and loop through to gather raw inputs
        StringBuilder inputMemory = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("./inputs/input10.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputMemory.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        inputMemory = new StringBuilder();
//        inputMemory.append("89010123\n" +
//                "78121874\n" +
//                "87430965\n" +
//                "96549874\n" +
//                "45678903\n" +
//                "32019012\n" +
//                "01329801\n" +
//                "10456732");

        System.out.println("inputMemory size: " + inputMemory.length());

        // build map of inputs
        String[] inputArray = inputMemory.toString().split("\n");
        Integer[][] map = new Integer[inputArray.length][inputArray[0].length()];
        for (int i = 0; i < inputArray.length; i++) {
            for (int j = 0; j < inputArray[i].length(); j++) {
                map[i][j] = Integer.parseInt(inputArray[i].substring(j, j + 1));
            }
        }

        // print map
        System.out.println("map size: " + map.length + "x" + map[0].length);
        for (Integer[] row : map) {
            for (Integer cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }

        List<Integer[]> trailHeads = new ArrayList<>();
        // find all trail heads, aka all cells with a value of 0
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 0) {
                    trailHeads.add(new Integer[]{i, j});
                }
            }
        }

        // print trail heads
        System.out.println("trailHeads size: " + trailHeads.size());
        for (Integer[] trailHead : trailHeads) {
            System.out.println(trailHead[0] + ", " + trailHead[1]);
        }

        // find all trails
        List<Integer[]> trails = new ArrayList<>();

        for (Integer[] trailHead : trailHeads) {
            if (TakeNextStep(trailHead, map, trailHead, trails)) {
                System.out.println("trail found for trailHead: " + trailHead[0] + "," + trailHead[1]);
            }
        }

        // print trails
        System.out.println("trails size: " + trails.size());
        for (Integer[] trail : trails) {
            System.out.println("head " + trail[0] + "," + trail[1] + " tail " + trail[2] + "," + trail[3]);
        }

        // remove duplicate trails, only count each tail once
        List<Integer[]> destTrails = new ArrayList<>();
        for (Integer[] trail : trails) {
            boolean seenTail = false;
            for (Integer[] destTrail : destTrails) {
                if (destTrail[0].equals(trail[0]) &&
                        destTrail[1].equals(trail[1]) &&
                        destTrail[2].equals(trail[2]) &&
                        destTrail[3].equals(trail[3])) {
                    seenTail = true;
                    break;
                }
            }
            if (!seenTail) {
                destTrails.add(new Integer[]{trail[0], trail[1], trail[2], trail[3]});
            }
        }

        // print destTrails
        System.out.println("destTrails size: " + destTrails.size());
        for (Integer[] destTrail : destTrails) {
            System.out.println("head " + destTrail[0] + "," + destTrail[1] + " tail " + destTrail[2] + "," + destTrail[3]);
        }

        // print trailheads with score
        int trailScore = 0;
        int totalScore = 0;
        for (Integer[] trailHead : trailHeads) {
            trailScore = 0;
            for (Integer[] trail : destTrails) {
                if (trail[0].equals(trailHead[0]) && trail[1].equals(trailHead[1])) {
                    trailScore++;
                }
            }
            System.out.println("trailHead " + trailHead[0] + "," + trailHead[1] + " score " + trailScore);
            totalScore += trailScore;
        }
        System.out.println("totalScore " + totalScore);
        // 617 is right part 1

        // print trails with scores
        totalScore = 0;
        for (Integer[] trailHead : trailHeads) {
            trailScore = 0;
            for (Integer[] trail : trails) {
                if (trail[0].equals(trailHead[0]) && trail[1].equals(trailHead[1])) {
                    trailScore++;
                }
            }
            System.out.println("trailHead " + trailHead[0] + "," + trailHead[1] + " score " + trailScore);
            totalScore += trailScore;
        }
        System.out.println("totalScore " + totalScore);
        // 1477 is right part 2

        System.out.println("2024 Day 10 end");
    }

    private static boolean TakeNextStep(Integer[] currentPos, Integer[][] map, Integer[] startingPos, List<Integer[]> trails) {
        // look in 4 directions, see if I can step up +1
        boolean foundTrail = false;
        int currentElevation = map[currentPos[0]][currentPos[1]];
        for (Integer[] possibleStep : GetPossibleSteps(currentPos, map)) {
            Integer possibleElevation = map[possibleStep[0]][possibleStep[1]];
            if (possibleElevation == currentElevation + 1) {
                if (possibleElevation == 9) {
                    trails.add(new Integer[]{startingPos[0], startingPos[1], possibleStep[0], possibleStep[1]});
                    foundTrail = true;
                } else TakeNextStep(possibleStep, map, startingPos, trails);
            }
        }
        return foundTrail;
    }

    private static Integer[][] GetPossibleSteps(Integer[] currentPos, Integer[][] map) {
        Integer[][] possibleSteps = {
                {currentPos[0] - 1, currentPos[1]},
                {currentPos[0] + 1, currentPos[1]},
                {currentPos[0], currentPos[1] - 1},
                {currentPos[0], currentPos[1] + 1}
        };
        for (int i = 0; i < possibleSteps.length; i++) {
            if (possibleSteps[i][0] < 0 || possibleSteps[i][0] > map.length - 1 || possibleSteps[i][1] < 0 || possibleSteps[i][1] > map[0].length - 1) {
                possibleSteps[i] = null;
            }
        }
        List<Integer[]> resultSteps = new ArrayList<>();
        for (Integer[] possibleStep : possibleSteps) {
            if (possibleStep != null) {
                resultSteps.add(possibleStep);
            }
        }

        return resultSteps.toArray(new Integer[0][0]);
    }

}




