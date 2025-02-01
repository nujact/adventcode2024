import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Day12 {

    public static void Start() {
        System.out.println("2024 Day 12 start");

        // open file and loop through to gather raw inputs
        StringBuilder inputMemory = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("./inputs/input12.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputMemory.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputMemory = new StringBuilder();
        inputMemory.append("AAAA\n" +
                "BBCD\n" +
                "BBCC\n" +
                "EEEC");

        System.out.println("inputMemory size: " + inputMemory.length());

        String[][] plotRaw = Arrays.stream(inputMemory.toString().split("\n"))
                .map(s -> s.split("")).toArray(String[][]::new);

        Plot[][] plots = new Plot[plotRaw.length][plotRaw[0].length];
        for (int y = 0; y < plotRaw.length; y++) {
            for (int x = 0; x < plotRaw[y].length; x++) {
                plots[y][x] = new Plot(plotRaw[y][x], x, y);
            }
        }

        // print plot
        System.out.println("plot: " + plots.length + "x" + plots[0].length);
        for (Plot[] row : plots) {
            for (Plot cell : row) {
                System.out.print(cell.plant);
            }
            System.out.println();
        }

        String[] plants = GetPlants(plots);
        System.out.println("plants: " + plants.length);
        for (String plant : plants) {
            System.out.println(plant);
        }

        // walk plots and build regions
        HashMap<Integer, Region> regions = new HashMap<>();
        int regionId = -1;

        for (int i = 0; i < plots.length; i++) {
            Plot[] plotRow = plots[i];
            for (int j = 0, plotRowLength = plotRow.length; j < plotRowLength; j++) {
                Plot plot = plotRow[j];

                // build list of neighbors
                int[][] addresses = new int[][]{
                        {i - 1, j}, // top
                        {i, j + 1}, // right
                        {i + 1, j}, // bottom
                        {i, j - 1} // left
                };
                for (int k = 0; k < addresses.length; k++) {
                    int[] address = addresses[k];
                    if (address[0] < 0 || address[0] == plots.length || address[1] < 0 || address[1] == plotRow.length) {
                        // remove out of bounds addresses
                        addresses[k] = null;
                    }
                }

                // see if I am regionless
                if (plot.RegionId == -1) {
                    for (int[] address : addresses) {
                        if (address != null) {
                            //System.out.println("neighbor: " + address[0] + "," + address[1]);
                            Plot neighborPlot = plots[address[0]][address[1]];
                            if (neighborPlot.plant.equals(plot.plant) && neighborPlot.RegionId != -1) {
                                // I am the same plant as my neighbor, and my neighbor is in a region
                                plot.RegionId = neighborPlot.RegionId;
                                regions.get(neighborPlot.RegionId).plots.add(plot);
                            }
                        }
                    }
                }

                if (plot.RegionId == -1) {
                    // I am regionless
                    // create new region
                    regionId++;
                    plot.RegionId = regionId;
                    Region newRegion = new Region(regionId);
                    newRegion.plots.add(plot);
                    regions.put(regionId, newRegion);
                }

                // spread to brothers
                for (int[] address : addresses) {
                    if (address != null) {
                        Plot neighborPlot = plots[address[0]][address[1]];
                        if (neighborPlot.plant.equals(plot.plant) && neighborPlot.RegionId == -1) {
                            neighborPlot.RegionId = plot.RegionId;
                            regions.get(plot.RegionId).plots.add(neighborPlot);
                        }
                    }
                }

                List<Map<String, Integer>> neighbors = new ArrayList<>();
            }
        }

        // print regions
        System.out.println("regions: \n" + regions.size());
        for (Region region : regions.values()) {
            System.out.print("Region " + region.id + " plots(" + region.plots.size() +" ):");
            for (Plot plot : region.plots) {
                System.out.print("  " + plot.x + "," + plot.y);
            }
            System.out.println();
        }

        System.out.println("2024 Day 12 end");
    }

    private static class Region {
        public int id;
        public List<Plot> plots;
        int area = 0;
        int perimeter = 0;

        public Region(int id) {
            this.id = id;
            this.plots = new ArrayList<>();
        }
    }

    private static class Plot {
        public String plant;
        public int x;
        public int y;
        public int RegionId;

        public Plot(String plant, int x, int y) {
            this.plant = plant;
            this.RegionId = -1;
            this.x = x;
            this.y = y;
        }
    }


    private static String[] GetPlants(Plot[][] plots) {
        List<String> plants = new ArrayList<>();
        for (int y = 0; y < plots.length; y++) {
            for (int x = 0; x < plots[y].length; x++) {
                if (!plants.contains(plots[y][x].plant)) {
                    plants.add(plots[y][x].plant);
                }
            }
        }
        return plants.toArray(new String[0]);
    }

}




