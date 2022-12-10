package aoc;

import lombok.Data;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("unused")
public class Day09 implements DaySolver {
    record Coordinate(int x, int y) {}
    record Maneuver(int x, int y) {}
    record MoveAction(Character direction, int number) {}
    private final Coordinate startingCoordinate = new Coordinate(20, 20);

    private Maneuver getManeuver(Coordinate prevKnotRelativePosition) {
        int x = prevKnotRelativePosition.x;
        int y = prevKnotRelativePosition.y;

        // TL
        if (x == -1 && y ==2 || x == -2 && y == 1 || x == -2 && y == 2)
            return new Maneuver(-1, 1);
        // L
        if (x == -2 && y == 0)
            return new Maneuver(-1, 0);
        //BL
        if (x == -2 && y == -1 || x == -1 && y == -2 || x == -2 && y == -2)
            return new Maneuver(-1, -1);
        // B
        if (x == 0 && y == -2)
            return new Maneuver(0, -1);
        // BR
        if (x == 1 && y == -2 || x == 2 && y == -1 || x == 2 && y == -2)
            return new Maneuver(1, -1);
        // R
        if (x == 2 && y == 0)
            return new Maneuver(1, 0);
        // TR
        if (x == 2 && y == 1 || x == 1 && y == 2 || x == 2 && y == 2)
            return new Maneuver(1, 1);
        // T
        if (x == 0 && y == 2)
            return new Maneuver(0,1);

        throw new RuntimeException();
    }
    @Data
    static class Knot {
        private Coordinate currCoordinate;
        private Knot nextKnot;
        private boolean isHead = false;
        char symbol;
        public boolean isTail() {
            return nextKnot == null;
        }
    }

    @Override
    public String solvePuzzle1(List<String> input) {
        Knot head = initializeKnotSnake(2);
        return getVisitedTailCoordinates(input, head);
    }
    @Override
    public String solvePuzzle2(List<String> input) {
        Knot head = initializeKnotSnake(10);
        return getVisitedTailCoordinates(input, head);
    }

    private String getVisitedTailCoordinates(List<String> input, Knot head) {
        List<MoveAction> moveActions = this.parseMoveActions(input);
        Set<Coordinate> visitedTailCoordinates = executeMovesAndGetVisitedTailCoordinates(moveActions, head);
        return String.valueOf(visitedTailCoordinates.size());
    }

    public Knot initializeKnotSnake(int snakeSize) {
        Knot head = new Knot();
        head.isHead = true;
        Knot currentKnot = head;
        int i = 0;
        while (i < snakeSize) {
            currentKnot.currCoordinate = startingCoordinate;
            currentKnot.symbol = currentKnot.isHead ? 'H' : Character.forDigit(i, 10);
            if (i == snakeSize - 1) break;
            currentKnot.nextKnot = new Knot();
            currentKnot = currentKnot.nextKnot;
            currentKnot.isHead = false;
            i++;
        }

        return head;
    }

    public void printSnake(Knot head) throws Exception {
        int sizeOfGrid = 40;
        char[][] grid = new char[sizeOfGrid][sizeOfGrid];
        Knot currKnot = head;

        while (currKnot != null) {
            grid[sizeOfGrid - currKnot.getCurrCoordinate().y - 1][currKnot.getCurrCoordinate().x] = currKnot.symbol;
            currKnot = currKnot.nextKnot;
        }

        Path path = Paths.get(requireNonNull(this.getClass().getResource("../resources/snake.txt")).toURI());
        for (int i = 0; i < sizeOfGrid; i++) {
            char[] chars = grid[i];
            for (int j = 0; j < chars.length; j++) {
                char c = chars[j];
                if (c == 0) grid[i][j] = '.';
            }
            Files.writeString(path, new String(chars));
            Files.writeString(path, "\n");
        }
    }

    private Set<Coordinate> executeMovesAndGetVisitedTailCoordinates(List<MoveAction> moveActions, Knot head) {
        var visitedTailCoordinates = new HashSet<Coordinate>();
        visitedTailCoordinates.add(startingCoordinate);

        for (var moveAction : moveActions) {
            for (int i = 0; i < moveAction.number; i++) {
                var currKnot = head;
                var prevKnot = head;
                do {
                    if (currKnot.isHead()) {
                        setNewCoordinateSimpleDirectionChange(currKnot, moveAction.direction);
                        currKnot = currKnot.nextKnot;
                        continue;
                    }
                    if (currKnotTouchingPrevKnot(prevKnot, currKnot)) break;
                    Coordinate relativePositionDifference = new Coordinate(
                            prevKnot.currCoordinate.x - currKnot.currCoordinate.x,
                            prevKnot.currCoordinate.y - currKnot.currCoordinate.y);

                    applyManeuver(currKnot, getManeuver(relativePositionDifference));

                    if (currKnot.isTail()) visitedTailCoordinates.add(currKnot.currCoordinate);
                    prevKnot = currKnot;
                    currKnot = currKnot.nextKnot;
                } while (!prevKnot.isTail());

                try {
                    printSnake(head);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }
        return visitedTailCoordinates;
    }

    private void applyManeuver(Knot currKnot, Maneuver maneuver) {
        Coordinate newCoord = new Coordinate(currKnot.getCurrCoordinate().x + maneuver.x, currKnot.getCurrCoordinate().y + maneuver.y);
        currKnot.setCurrCoordinate(newCoord);
    }

    private boolean currKnotTouchingPrevKnot(Knot prevKnot, Knot currKnot) {
        Coordinate prevKnotCoordinate = prevKnot.getCurrCoordinate();
        Coordinate currKnotCoordinate = currKnot.getCurrCoordinate();
        return Math.abs(currKnotCoordinate.x - prevKnotCoordinate.x) <= 1 && Math.abs(currKnotCoordinate.y - prevKnotCoordinate.y) <= 1;
    }

    private void setNewCoordinateSimpleDirectionChange(Knot knot, Character direction) {
        Coordinate currCoordinate = knot.getCurrCoordinate();
        Coordinate newCoord = switch (direction) {
            case 'U' -> new Coordinate(currCoordinate.x, currCoordinate.y+1);
            case 'D' -> new Coordinate(currCoordinate.x, currCoordinate.y-1);
            case 'R' -> new Coordinate(currCoordinate.x+1, currCoordinate.y);
            case 'L' -> new Coordinate(currCoordinate.x-1, currCoordinate.y);
            default -> throw new RuntimeException();
        };
        knot.setCurrCoordinate(newCoord);
    }

    private List<MoveAction> parseMoveActions(List<String> input) {
        return input.stream()
                .map(inputLine -> {
                    String[] inputStrings = inputLine.split(" ");
                    return new MoveAction(inputStrings[0].charAt(0), Integer.parseInt(inputStrings[1]));
                })
                .toList();
    }
}
