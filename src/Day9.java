import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day9 {

    public static void Start() {
        System.out.println("2024 Day 9 start");

        // open file and loop through to gather raw inputs
        StringBuilder inputMemory = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("./inputs/input9.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputMemory.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        inputMemory = new StringBuilder();
//        inputMemory.append("2333133121414131402");

        System.out.println("inputMemory size: " + inputMemory.length());

        // convert to disk map
        DiskMap diskMap = new DiskMap(inputMemory.toString());

        // print disk map
        diskMap.Print();

        // defragment disk map
//        while (diskMap.DefragmentOnce()) {
//        }
        diskMap.DefragmentWholeFilesOnce();


        // print defragmented disk map
        System.out.println("defragmented disk map");
        diskMap.Print();

        // calculate checksum
        String checksum = diskMap.CalculateChecksum();
        System.out.println("checksum: " + checksum);
        // 6225730762521 is correct part 1
        // 6250605700557 is correct part 2

        System.out.println("2024 Day 9 end");
    }

    private static class DiskMap {
        List<Integer> Map;

        DiskMap(String inputString) {
            Map = new ArrayList<>();
            int fileId = 0;
            boolean needNewFileId = false;
            for (int i = 0; i < inputString.length(); i++) {
                int currentInt = Integer.parseInt(inputString.substring(i, i + 1));
                if (i % 2 == 0) {
                    // odd, file size
                    if (needNewFileId) {
                        fileId++;
                        needNewFileId = false;
                    }
                    for (int takeCtr = 0; takeCtr < currentInt; takeCtr++) {
                        Map.add(fileId);
                    }
                } else {
                    // even, empty space
                    for (int takeCtr = 0; takeCtr < currentInt; takeCtr++) {
                        Map.add(-1);
                    }
                    needNewFileId = true;
                }
            }
        }

        boolean DefragmentWholeFilesOnce() {
            int maxFileId = GetLastFileId();

            boolean moved = false;
            for (int fileIdCtr = maxFileId; fileIdCtr > -1; fileIdCtr--) {
                int fileIdCtrLength = GetFileLength(fileIdCtr);
                int positionFileCtr = GetFirstPositionByFileId(fileIdCtr);
                int positionFirstEmptyThatFitsCtr = GetFirstEmptyPositionByLength(fileIdCtrLength);
                if (positionFirstEmptyThatFitsCtr < positionFileCtr && positionFirstEmptyThatFitsCtr != -1) {
                    // empty space that fits this file prior to file pos, so move it
                    for (int i = 0; i < fileIdCtrLength; i++) {
                        Map.set(positionFirstEmptyThatFitsCtr + i, Map.get(positionFileCtr + i));
                        Map.set(positionFileCtr + i, -1);
                    }
                    moved = true;
                }
            }
            return moved;
        }

        private int GetFirstEmptyPositionByLength(int fileLength) {
            int pos = -1;
            boolean foundEmpty = false;
            int emptyCtr = 0;
            for (int i = 0; i < Map.size(); i++) {
                if (Map.get(i) == -1) {
                    if (!foundEmpty) pos = i;
                    foundEmpty = true;
                    emptyCtr++;
                } else {
                    if (foundEmpty && emptyCtr >= fileLength) break;
                    foundEmpty = false;
                    emptyCtr = 0;
                    pos = -1;
                }
            }
            return pos;
        }

        private int GetFirstPositionByFileId(int fileId) {
            int pos = -1;
            for (int i = 0; i < Map.size(); i++) {
                if (Map.get(i) == fileId) {
                    pos = i;
                    break;
                }
            }
            return pos;
        }

        private int GetFileLength(int fileId) {
            int length = 0;
            boolean foundStart = false;
            for (int i = 0; i < Map.size(); i++) {
                if (Map.get(i) == fileId) {
                    length++;
                    foundStart = true;
                }
                if (foundStart && Map.get(i) != fileId) {
                    break;
                }
            }
            return length;
        }

        private int GetLastFileId() {
            int lastFileId = -1;
            for (int i = Map.size() - 1; i > -1; i--) {
                if (Map.get(i) != -1) {
                    lastFileId = Map.get(i);
                    break;
                }
            }
            return lastFileId;
        }

        boolean DefragmentOnce() {
            int positionLastUsed = GetLastUsedPosition();
            int positionFirstEmpty = GetFirstEmptyPosition();
            boolean moved = false;
            if (positionFirstEmpty < positionLastUsed) {
                // empty space prior to last used position, so move it
                Map.set(positionFirstEmpty, Map.get(positionLastUsed));
                Map.set(positionLastUsed, -1);
                moved = true;
            }
            return moved;
        }

        private int GetFirstEmptyPosition() {
            for (int i = 0; i < Map.size(); i++) {
                if (Map.get(i) == -1) {
                    return i;
                }
            }
            return -1;
        }

        int GetLastUsedPosition() {
            for (int i = Map.size() - 1; i >= 0; i--) {
                if (Map.get(i) != -1) {
                    return i;
                }
            }
            return -1;
        }

        public String CalculateChecksum() {
            Long checksum = 0L;
            for (int i = 0; i < Map.size(); i++) {
                if (Map.get(i) != -1) {
                    checksum += Map.get(i) * i;
                }
            }
            return checksum.toString();
        }

        public void Print() {
            for (int i = 0; i < Map.size(); i++) {
                int output = Map.get(i);
                if (output == -1) {
                    System.out.print(". ");
                } else {
                    System.out.print(output + " ");
                }
            }
            System.out.println();
        }
    }

}




