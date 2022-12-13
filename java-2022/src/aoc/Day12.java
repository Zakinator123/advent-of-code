package aoc;

import java.util.*;

import static aoc.Utilities.copy2dIntArray;

@SuppressWarnings("unused")
public class Day12 implements DaySolver {

    record Coordinate(int i, int j) {}
    record GridWithStartAndEndCoordinates(int[][] grid, Coordinate start, Coordinate end) {}

    public String solvePuzzle1(List<String> input) { return getShortestPathWithBfs(getGridWithStartAndEndCoordinates(input)).toString(); }
    public String solvePuzzle2(List<String> input) {
        final GridWithStartAndEndCoordinates gridWithStartAndEndCoordinates = getGridWithStartAndEndCoordinates(input);
        return getLowestStartCoordinatesFromGrid(gridWithStartAndEndCoordinates.grid)
                .stream()
                .map(startPoint -> getShortestPathWithBfs(new GridWithStartAndEndCoordinates(copy2dIntArray(gridWithStartAndEndCoordinates.grid), startPoint, gridWithStartAndEndCoordinates.end)))
                .filter(pathLength -> pathLength != -1)
                .sorted()
                .toList()
                .get(0).toString();
    }

    private List<Coordinate> getLowestStartCoordinatesFromGrid(int[][] grid) {
        List<Coordinate> lowestStartingPoints = new ArrayList<>();
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++)
                if (grid[i][j] == Character.getNumericValue('a')) lowestStartingPoints.add(new Coordinate(i, j));
        return lowestStartingPoints;
    }

    private Integer getShortestPathWithBfs(GridWithStartAndEndCoordinates gridWithStartAndEndCoordinates) {
        Queue<Coordinate> q = new ArrayDeque<>();
        q.add(gridWithStartAndEndCoordinates.start);
        int[][] grid = gridWithStartAndEndCoordinates.grid;
        int bfsCounter = 0;
        while (!q.isEmpty()) {
            final int qSize = q.size();
            for (int k = 0; k < qSize; k++) {
                Coordinate currCoord = Objects.requireNonNull(q.poll());
                int i = currCoord.i;
                int j = currCoord.j;
                if (i == gridWithStartAndEndCoordinates.end.i && j == gridWithStartAndEndCoordinates.end.j) return bfsCounter;
                addUnvisitedCoordinateToQueueIfHeightDifferenceIsTolerable(i+1, j, grid[i][j], grid, q);
                addUnvisitedCoordinateToQueueIfHeightDifferenceIsTolerable(i-1 , j, grid[i][j], grid, q);
                addUnvisitedCoordinateToQueueIfHeightDifferenceIsTolerable(i, j+1, grid[i][j], grid, q);
                addUnvisitedCoordinateToQueueIfHeightDifferenceIsTolerable(i, j-1, grid[i][j], grid, q);
                grid[i][j] = 0;
            }
            bfsCounter++;
        }
        return -1;
    }

    private void addUnvisitedCoordinateToQueueIfHeightDifferenceIsTolerable(int i, int j, int currentHeight, int[][] grid, Queue<Coordinate> q) {
        if (i >= 0 && j >= 0 && i < grid.length && j < grid[i].length && grid[i][j] <= currentHeight + 1 && grid[i][j] != 0) q.add(new Coordinate(i, j));
    }

    private static GridWithStartAndEndCoordinates getGridWithStartAndEndCoordinates(List<String> input) {
        char[][] charLines = input.stream().map(String::toCharArray).toArray(char[][]::new);
        Coordinate start = null;
        Coordinate end = null;
        int[][] grid = new int[input.size()][input.get(0).length()];

        for (int i = 0; i < input.size(); i++)
            for (int j = 0; j < input.get(i).length(); j++) {
                char currentChar = charLines[i][j];
                if (currentChar == 'E' ) {
                    end = new Coordinate(i, j);
                    grid[i][j] = Character.getNumericValue('z');
                    continue;
                }
                if (currentChar == 'S' ) {
                    start = new Coordinate(i, j);
                    grid[i][j] = Character.getNumericValue('a');
                    continue;
                }
                grid[i][j] = Character.getNumericValue(currentChar);
            }
        return new GridWithStartAndEndCoordinates(grid, start, end);
    }
}
