package aoc;

import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class Day09 implements DaySolver {
    record Coordinate(int x, int y) {}
    record Maneuver(int x, int y) {}
    record MoveAction(Character direction, int number) {}
    private final Coordinate startingCoordinate = new Coordinate(0, 0);

    private Maneuver getManeuverToFixSnake(Coordinate prevKnotRelativePosition) {
        int x = prevKnotRelativePosition.x;
        int y = prevKnotRelativePosition.y;

        if (x == -1 && y ==2 || x == -2 && y == 1 || x == -2 && y == 2) return new Maneuver(-1, 1);      // TL
        if (x == -2 && y == 0) return new Maneuver(-1, 0);                                               // L
        if (x == -2 && y == -1 || x == -1 && y == -2 || x == -2 && y == -2) return new Maneuver(-1, -1); // BL
        if (x == 0 && y == -2) return new Maneuver(0, -1);                                               // B
        if (x == 1 && y == -2 || x == 2 && y == -1 || x == 2 && y == -2) return new Maneuver(1, -1);     // BR
        if (x == 2 && y == 0) return new Maneuver(1, 0);                                                 // R
        if (x == 2 && y == 1 || x == 1 && y == 2 || x == 2 && y == 2) return new Maneuver(1, 1);         // TR
        if (x == 0 && y == 2) return new Maneuver(0,1);                                                  // T
        throw new RuntimeException();
    }

    @Data
    static class Knot {
        private Coordinate currCoordinate;
        private Knot nextKnot;
        private boolean isHead = false;
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
            if (i == snakeSize - 1) break;
            currentKnot.nextKnot = new Knot();
            currentKnot = currentKnot.nextKnot;
            currentKnot.isHead = false;
            i++;
        }
        return head;
    }

    private Set<Coordinate> executeMovesAndGetVisitedTailCoordinates(List<MoveAction> moveActions, Knot head) {
        HashSet<Coordinate> visitedTailCoordinates = new HashSet<>(List.of(startingCoordinate));
        for (var moveAction : moveActions) {
            for (int i = 0; i < moveAction.number; i++) {
                var currKnot = head;
                var prevKnot = head;
                while (currKnot != null) {
                    if (currKnot.isHead()) {
                        setNewCoordinateSimpleDirectionChange(currKnot, moveAction.direction);
                        currKnot = currKnot.nextKnot;
                        continue;
                    }
                    if (currKnotTouchingPrevKnot(prevKnot, currKnot)) break;
                    Coordinate relativePositionDifference = new Coordinate(
                            prevKnot.currCoordinate.x - currKnot.currCoordinate.x,
                            prevKnot.currCoordinate.y - currKnot.currCoordinate.y);

                    Maneuver maneuver = getManeuverToFixSnake(relativePositionDifference);
                    currKnot.setCurrCoordinate(new Coordinate(currKnot.getCurrCoordinate().x + maneuver.x, currKnot.getCurrCoordinate().y + maneuver.y));

                    if (currKnot.isTail()) visitedTailCoordinates.add(currKnot.currCoordinate);

                    prevKnot = currKnot;
                    currKnot = currKnot.nextKnot;
                }
            }
        }
        return visitedTailCoordinates;
    }

    private boolean currKnotTouchingPrevKnot(Knot prevKnot, Knot currKnot) {
        Coordinate prevKnotCoordinate = prevKnot.getCurrCoordinate();
        Coordinate currKnotCoordinate = currKnot.getCurrCoordinate();
        return Math.abs(currKnotCoordinate.x - prevKnotCoordinate.x) <= 1 && Math.abs(currKnotCoordinate.y - prevKnotCoordinate.y) <= 1;
    }

    private void setNewCoordinateSimpleDirectionChange(Knot knot, Character direction) {
        Coordinate currCoordinate = knot.getCurrCoordinate();
        knot.setCurrCoordinate(switch (direction) {
            case 'U' -> new Coordinate(currCoordinate.x, currCoordinate.y+1);
            case 'D' -> new Coordinate(currCoordinate.x, currCoordinate.y-1);
            case 'R' -> new Coordinate(currCoordinate.x+1, currCoordinate.y);
            case 'L' -> new Coordinate(currCoordinate.x-1, currCoordinate.y);
            default -> throw new RuntimeException();});
    }

    private List<MoveAction> parseMoveActions(List<String> input) {
        return input.stream().map(inputLine -> {
                    String[] inputStrings = inputLine.split(" ");
                    return new MoveAction(inputStrings[0].charAt(0), Integer.parseInt(inputStrings[1]));
                }).toList();
    }
}
