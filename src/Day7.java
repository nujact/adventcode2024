import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day7 {

    public static void Start() {
        System.out.println("2024 Day 7 start");

        // open file and loop through to gather raw inputs
        StringBuilder inputMemory = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("./inputs/input7.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputMemory.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        inputMemory = new StringBuilder();
//        inputMemory.append("190: 10 19\n" +
//                "3267: 81 40 27\n" +
//                "83: 17 5\n" +
//                "156: 15 6\n" +
//                "7290: 6 8 6 15\n" +
//                "161011: 16 10 13\n" +
//                "192: 17 8 14\n" +
//                "21037: 9 7 18 13\n" +
//                "292: 11 6 16 20"
//        );

        System.out.println("inputMemory size: " + inputMemory.length());
        //System.out.println("inputMemory: " + inputMemory);

        List<Equation> equations = new ArrayList<>();
        String[] inputStrings = inputMemory.toString().split("\n");
        for (String inputString : inputStrings) {
            Equation equation = new Equation(inputString);
            equations.add(equation);
        }

        for (Equation equation : equations) {
            System.out.println("Equation " + equation.TargetResult + " " + equation.InputValues);
        }
        System.out.println();

        for (Equation equation : equations) {
            List<Long> allResults = new ArrayList<>();
            CalcNextInput(equation.InputValues, 1, equation.InputValues.get(0), "+", allResults);
            CalcNextInput(equation.InputValues, 1, equation.InputValues.get(0), "*", allResults);
            CalcNextInput(equation.InputValues, 1, equation.InputValues.get(0), "|", allResults);
            equation.PossibleResults = allResults;
        }

        // find equation with target result
        Long sumSuccessTargets = 0L;
        for (Equation equation : equations) {
            if (equation.PossibleResults.contains(equation.TargetResult)) {
                System.out.println("success Equation " + equation.TargetResult + " " + equation.InputValues + " " + equation.PossibleResults);
                sumSuccessTargets += equation.TargetResult;
            }
        }
        System.out.println("\nsumSuccessTargets: " + sumSuccessTargets + "\n");
        // 1509440454730 is too high
        // 23896792601   is too low
        // 1399219271639 pt 1
        // 275791737999003

        System.out.println("2024 Day 7 end");
    }

    private static void CalcNextInput(List<Integer> inputList, int index, long result, String operator, List<Long> allResults) {
        int value = inputList.get(index);
        if (operator.equals("+")) result = result + value;
        else if (operator.equals("*")) result = result * value;
        else if (operator.equals("|")) {
            String res = Long.toString(result) + Long.toString(value);
            result = Long.parseLong(res);
        }

        // if no more inputs, add to allResults
        if (index == inputList.size() - 1) allResults.add(result);
        else {
            // handle 2 operators, + and *
            CalcNextInput(inputList, index + 1, result, "+", allResults);
            CalcNextInput(inputList, index + 1, result, "*", allResults);
            CalcNextInput(inputList, index + 1, result, "|", allResults);
        }
    }

    public static class Equation {
        Long TargetResult;
        List<Integer> InputValues;
        List<Long> PossibleResults;

        Equation(String inputString) {
            String[] splitString = inputString.split(": ");
            TargetResult = Long.parseLong(splitString[0]);
            InputValues = new ArrayList<>();
            for (String input : splitString[1].split(" ")) {
                InputValues.add(Integer.parseInt(input));
            }
            PossibleResults = new ArrayList<>();
        }
    }

}
