package aoc;

import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unused")
public class Day08 implements DaySolver {

    record TreeCoordinate(int i, int j){}

    @Override
    public String solvePuzzle1(List<String> input) {
        var seenTrees = new HashSet<TreeCoordinate>();
        char[][] grid = getGrid(input);

        topToBottom(seenTrees, grid);
        leftToRight(seenTrees, grid);
        bottomToTop(seenTrees, grid);
        rightToLeft(seenTrees, grid);
        return String.valueOf(seenTrees.size());
    }

    private static char[][] getGrid(List<String> input) {
        List<char[]> gridList =  input.stream().map(String::toCharArray).toList();
        return gridList.toArray(new char[0][]);
    }

    @Override
    public String solvePuzzle2(List<String> input) {
        char[][] grid = getGrid(input);
        int maxScenicScore = 0;
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid.length; j++)
                maxScenicScore = Math.max(maxScenicScore, getScenicScoreOfTree(i, j, grid));
        return String.valueOf(maxScenicScore);
    }

    private int getScenicScoreOfTree(int i, int j, char[][] grid) {
        int currentTreeHeight = grid[i][j];
        int leftScenicScore = rightToLeftScenicScore(grid, i, j, currentTreeHeight);
        int rightScenicScore = leftToRightScenicScore(grid, i, j, currentTreeHeight);
        int bottomToTopScenicScore = bottomToTopScenicScore(grid, i, j, currentTreeHeight);
        int topToBottomScenicScore = topToBottomScenicScore(grid, i, j, currentTreeHeight);

        return (leftScenicScore * rightScenicScore * bottomToTopScenicScore * topToBottomScenicScore);
    }

    private int bottomToTopScenicScore(char[][] grid, int iCoord, int jCoord, int currTreeHeight) {
        int numTrees = 0;
        for (int i = iCoord - 1; i >= 0; i--) {
            numTrees++;
            if (grid[i][jCoord] >= currTreeHeight) break;
        }
        return numTrees;
    }

    private int topToBottomScenicScore(char[][] grid, int iCoord, int jCoord, int currTreeHeight) {
        int numTrees = 0;
        for (int i = iCoord + 1; i < grid.length; i++) {
            numTrees++;
            if (grid[i][jCoord] >= currTreeHeight) break;
        }
        return numTrees;
    }

    private int leftToRightScenicScore(char[][] grid, int iCoord, int jCoord, int currTreeHeight) {
        int numTrees = 0;
        for (int j = jCoord + 1; j < grid.length; j++) {
            numTrees++;
            if (grid[iCoord][j] >= currTreeHeight) break;
        }
        return numTrees;
    }

    private int rightToLeftScenicScore(char[][] grid, int iCoord, int jCoord, int currTreeHeight) {
        int numTrees = 0;
        for (int j = jCoord - 1; j >= 0; j--) {
            numTrees++;
            if (grid[iCoord][j] >= currTreeHeight) break;
        }
        return numTrees;
    }

    private void bottomToTop(HashSet<TreeCoordinate> seenTreeCoordinates, char[][] grid) {
        for (int j = 0; j < grid[0].length; j++) {
            int maxHeightSeen = -1;
            for (int i = grid.length - 1; i >= 0; i--)
                maxHeightSeen = getMaxHeightSeen(seenTreeCoordinates, grid, i, maxHeightSeen, j);
        }
    }

    private void rightToLeft(HashSet<TreeCoordinate> seenTreeCoordinates, char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            int maxHeightSeen = -1;
            for (int j = grid[i].length - 1; j >= 0; j--)
                maxHeightSeen = getMaxHeightSeen(seenTreeCoordinates, grid, i, maxHeightSeen, j);
        }
    }

    private void topToBottom(HashSet<TreeCoordinate> seenTreeCoordinates, char[][] grid) {
        for (int j = 0; j < grid[0].length; j++) {
            int maxHeightSeen = -1;
            for (int i = 0; i < grid.length; i++)
                maxHeightSeen = getMaxHeightSeen(seenTreeCoordinates, grid, i, maxHeightSeen, j);
        }
    }

    private void leftToRight(HashSet<TreeCoordinate> seenTreeCoordinates, char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            int maxHeightSeen = -1;
            for (int j = 0; j < grid[i].length; j++)
                maxHeightSeen = getMaxHeightSeen(seenTreeCoordinates, grid, i, maxHeightSeen, j);
        }
    }

    private int getMaxHeightSeen(HashSet<TreeCoordinate> seenTreeCoordinates, char[][] grid, int i, int maxHeightSeen, int j) {
        int currentHeight = Integer.parseInt(String.valueOf(grid[i][j]));
        if (currentHeight <= maxHeightSeen) return maxHeightSeen;
        TreeCoordinate currTreeCoordinate = new TreeCoordinate(i, j);
        if (seenTreeCoordinates.contains(currTreeCoordinate)) return currentHeight;
        seenTreeCoordinates.add(currTreeCoordinate);
        maxHeightSeen = currentHeight;
        return maxHeightSeen;
    }
}
