package aoc;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.*;

public class Day07 implements DaySolver {


    @Data
    @RequiredArgsConstructor
    static class File {
        private final List<File> files;
        private final boolean isDir;
        private final String name;
        private int size;
    }
    @Override
    public String solvePuzzle1(List<String> input) {
        File rootDirectory = getPopulatedFileSystemTree(input);
        return String.valueOf(traverseTreeAndSumDirectoriesWithSizesLessThanMaxSize(100000, rootDirectory));
    }

    @Override
    public String solvePuzzle2(List<String> input) {
        File rootDirectory = getPopulatedFileSystemTree(input);
        int minAmountOfSpaceToBeFreed = 30000000 - (70000000 - rootDirectory.getSize());
        return String.valueOf(traverseTreeAndFindMinSizeDirectoryToBeDeleted(minAmountOfSpaceToBeFreed, rootDirectory));
    }

    private int traverseTreeAndFindMinSizeDirectoryToBeDeleted(int minAmountOfSpaceToBeFreed, File file) {
        if (!file.isDir()) return 0;
        if (file.getSize() <= minAmountOfSpaceToBeFreed) return 0;

        int currentSmallestDirectorySize = file.getSize();
        for (File currFile : file.getFiles()) {
            if (file.isDir() && file.getSize() >= minAmountOfSpaceToBeFreed) {
                int smallestSize = traverseTreeAndFindMinSizeDirectoryToBeDeleted(minAmountOfSpaceToBeFreed, currFile);
                if (smallestSize == 0) continue;
                currentSmallestDirectorySize = Math.min(currentSmallestDirectorySize, smallestSize);
            }
        }
        return currentSmallestDirectorySize;
    }

    private File getPopulatedFileSystemTree(List<String> input) {
        File rootDirectory = buildDirectoryTree(input);
        traverseTreeAndPopulateSizes(rootDirectory);
        return rootDirectory;
    }

    private int traverseTreeAndSumDirectoriesWithSizesLessThanMaxSize(int maxSize, File file) {
        if (!file.isDir()) return 0;
        int currDirectorySize = file.getSize();
        int sumOfEligibleSubDirectories = 0;
        for (File currFile : file.getFiles()) {
            if (currFile.isDir()) sumOfEligibleSubDirectories += traverseTreeAndSumDirectoriesWithSizesLessThanMaxSize(maxSize, currFile);
        }
        return currDirectorySize <= maxSize ? currDirectorySize + sumOfEligibleSubDirectories : sumOfEligibleSubDirectories;
    }

    int traverseTreeAndPopulateSizes(File file) {
        if (!file.isDir()) return 0;
        int fileSize = 0;
        for (File currFile : file.files) {
            if (currFile.isDir()) fileSize += traverseTreeAndPopulateSizes(currFile);
            else fileSize += currFile.getSize();
        }
        file.setSize(fileSize);
        return fileSize;
    }

    private File buildDirectoryTree(List<String> input) {
        Deque<File> directoryStack = new ArrayDeque<File>();
        File rootDirectory = new File(new ArrayList<>(), true, "/");
        directoryStack.push(rootDirectory);

        for (int i = 1; i < input.size(); i++) {
            String currentLine = input.get(i);
            if (currentLine.contains("$ ls")) {

                List<File> directoryFiles = new ArrayList<>();

                for (int j = i + 1; j < input.size(); j++) {
                    String currentDirLine = input.get(j);
                    if (currentDirLine.contains("$")) break;
                    directoryFiles.add(getFileFromLine(currentDirLine));
                    i = j-1;
                }
                var currentDir = directoryStack.peek();
                assert currentDir != null;
                currentDir.files.addAll(directoryFiles);
            } else if (currentLine.contains("$ cd ..")) {
                directoryStack.pop();
            } else if (currentLine.contains("$ cd")) {
                String[] s = currentLine.split(" ");
                String directoryNameToEnter = s[2];
                File currentDir = directoryStack.peek();
                File dirToEnter = currentDir.files.stream().filter(file -> file.name.equals(directoryNameToEnter)).findFirst().get();
                directoryStack.push(dirToEnter);
            }
        }

        return rootDirectory;
    }

    private File getFileFromLine(String currentDirLine) {
        if (currentDirLine.contains("dir")) return new File(new ArrayList<>(), true, currentDirLine.substring(4));

        String[] s = currentDirLine.split(" ");
        File file = new File(Collections.emptyList(), false, s[1]);
        file.size = Integer.parseInt(s[0]);
        return file;
    }
}
