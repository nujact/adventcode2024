import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Day5 {

    public static void Start() {
        System.out.println("2024 Day 5 start");

        // open file and loop through to gather raw inputs
        StringBuilder inputMemory = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("./inputs/input5.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputMemory.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        inputMemory = new StringBuilder();
//        inputMemory.append(
//                "47|53\n" +
//                        "97|13\n" +
//                        "97|61\n" +
//                        "97|47\n" +
//                        "75|29\n" +
//                        "61|13\n" +
//                        "75|53\n" +
//                        "29|13\n" +
//                        "97|29\n" +
//                        "53|29\n" +
//                        "61|53\n" +
//                        "97|53\n" +
//                        "61|29\n" +
//                        "47|13\n" +
//                        "75|47\n" +
//                        "97|75\n" +
//                        "47|61\n" +
//                        "75|61\n" +
//                        "47|29\n" +
//                        "75|13\n" +
//                        "53|13\n" +
//                        "\n" +
//                        "75,47,61,53,29\n" +
//                        "97,61,53,29,13\n" +
//                        "75,29,13\n" +
//                        "75,97,47,61,53\n" +
//                        "61,13,29\n" +
//                        "97,13,75,29,47\n"
//        );

        System.out.println("inputMemory size: " + inputMemory.length());
        //System.out.println("inputMemory: " + inputMemory);

        List<String> inputRules = getPageOrderRules(inputMemory);
        List<List<String>> inputUpdates = getUpdates(inputMemory);

        // print page order rules
        System.out.println("inputRules:" + inputRules.size());
        for (int i = 0; i < inputRules.size(); i++) {
            System.out.println(inputRules.get(i));
        }
        // print updates
        System.out.println("\ninputUpdates:" + inputUpdates.size());
        for (int i = 0; i < inputUpdates.size(); i++) {
            System.out.println(inputUpdates.get(i));
        }

        //Part1(inputUpdates, inputRules);
        Part2(inputUpdates, inputRules);

        System.out.println("2024 Day 5 end");
    }


    private static void Part2(List<List<String>> inputUpdates, List<String> inputRules) {
        // walk thru updateList
        // if invalid, move to correct order
        // loop while still finding invalids

        int middleOfTruth = 0;
        boolean allValid = false;

        // build list of invalid updates
        List<Integer> invalidUpdates = new ArrayList<>();
        for (int j = 0; j < inputUpdates.size(); j++) {
            List<String> update = inputUpdates.get(j);
            allValid = true;
            for (int i = 0; i < update.size(); i++) {
                // validate each page of this update
                if (ValidateOrder(inputRules, i, update)) {
                } else {
                    allValid = false;
                }
            }
            if (!allValid) invalidUpdates.add(j);
        }

        allValid = false;
        while (!allValid) {

            allValid = true;
            for (int i = 0; i < invalidUpdates.size(); i++) {
                List<String> update = inputUpdates.get(invalidUpdates.get(i));

                // loop thru pages of this update
                for (int j=0; j<update.size(); j++) {
                    // if movement is made
                    if (ValidateOrder(inputRules, j, update)) {
                    } else {
                        allValid = false;
                        update = MoveOrder(inputRules, j, update);
                        inputUpdates.set(invalidUpdates.get(i), update);
                    }
                }
//                if (!allValid) break;
            }

        }

        // walk invalid list and calc sum of mids
        for (Integer invalidUpdate : invalidUpdates) {
            List<String> update = inputUpdates.get(invalidUpdate);
            System.out.print("update "+ update);
            String middlePage = update.get(getMiddlePage(update));
            System.out.println("  mid "+ middlePage);
            var val = Integer.parseInt(middlePage);
            middleOfTruth += val;
        }

        System.out.println("middleSum "+middleOfTruth);
    }

    private static void Part1(List<List<String>> inputUpdates, List<String> inputRules) {
        // walk thru updateList
        boolean allValid = true;
        int middleOfTruth = 0;
        for (List<String> update : inputUpdates) {

            allValid = true;
            for (int i = 0; i < update.size(); i++) {
                String updatePage = update.get(i);
                // validate each page of this update
                if (ValidateOrder(inputRules, i, update)) {

                } else {
                    allValid = false;
                }
            }

            if (allValid) {
                String middlePage = update.get(getMiddlePage(update));
                var val = Integer.parseInt(middlePage);
                middleOfTruth += val;
            }

            System.out.println("# " + update + allValid);
        }

        System.out.println("middleSum "+middleOfTruth);
    }

    private static int getMiddlePage(List<String> update) {
        return (int) Math.round((double) (update.size() / 2) - .5);
    }

    private static List<String> MoveOrder(List<String> inputRules, int thisIndex, List<String> update) {// find pages that should occur before updatePage
        List<String> mustFollow = new ArrayList<>();
        var updatePage = update.get(thisIndex);
        for (int i = 0; i < inputRules.size(); i++) {
            if (inputRules.get(i).contains("|" + updatePage)) {
                String page = inputRules.get(i).split("\\|")[0];
                mustFollow.add(page);
            }
        }

        // for this update, does this current entry have pages AFTER this that are in the mustFollow list
        boolean validOrder = true;
        for (int i = thisIndex + 1; i < update.size(); i++) {
            String pg = update.get(i);

            for (String mustFollowPg : mustFollow) {
                if (mustFollowPg.equals(pg)) {
                    validOrder = false;
                    update = ListAddAt(update, i, updatePage);
                    update = ListRemAt(update, thisIndex, updatePage);
                    break;
                }
            }
            if (!validOrder) break;
        }

        return update;
    }

    private static List<String> ListRemAt(List<String> orgList, int posRem, String remElement) {
        List<String> newList = new ArrayList<>();
        for (int i = 0; i < orgList.size(); i++) {
            String pg = orgList.get(i);
            if (i==posRem && pg.equals(remElement)) {
                continue;
            }
            newList.add(pg);
        }
        return newList;
    }

    private static List<String> ListAddAt(List<String> orgList, int posAddAfter, String newElement) {
        List<String> newList = new ArrayList<>();
        for (int i = 0; i < orgList.size(); i++) {
            String pg = orgList.get(i);
            newList.add(pg);
            if (i==posAddAfter) {
                newList.add(newElement);
            }
        }
        return newList;
    }

    private static boolean ValidateOrder(List<String> inputRules, int thisIndex, List<String> update) {
        // find pages that should occur before updatePage
        List<String> mustFollow = new ArrayList<>();
        var updatePage = update.get(thisIndex);

        //System.out.println("\nupdatePage " + thisIndex + " " + updatePage);

        for (int i = 0; i < inputRules.size(); i++) {
            if (inputRules.get(i).contains("|" + updatePage)) {
                String page = inputRules.get(i).split("\\|")[0];
                mustFollow.add(page);
                //System.out.print(" " + page);
            }
        }

        //System.out.println();

        // for this update, does this current entry have pages AFTER this that are in the mustFollow list
        boolean validOrder = true;
        for (int i = thisIndex + 1; i < update.size(); i++) {
            if (mustFollow.contains(update.get(i))) {
                validOrder = false;
            }
        }

        return validOrder;
    }

    private static List<String> getPageOrderRules(StringBuilder inputMemory) {
        // split by new line into array
        String[] inputRows = inputMemory.toString().split("\n");
        List<String> pageOrderRulesList = new ArrayList<>();
        for (int i = 0; i < inputRows.length; i++) {
            // add rows till empty line
            if (inputRows[i].isEmpty()) break;
            pageOrderRulesList.add(inputRows[i]);
        }
        return pageOrderRulesList;
    }

    private static List<List<String>> getUpdates(StringBuilder inputMemory) {
        // split by new line into array
        String[] inputRows = inputMemory.toString().split("\n");
        List<List<String>> updateList = new ArrayList<>();
        boolean seenEmptyLine = false;
        for (int i = 0; i < inputRows.length; i++) {
            // skip rows till empty line
            if (!seenEmptyLine) {
                if (inputRows[i].isEmpty()) {
                    seenEmptyLine = true;
                }
                continue;
            }
            var update = inputRows[i].split(",");
            updateList.add(List.of(update));
        }
        return updateList;
    }

}

