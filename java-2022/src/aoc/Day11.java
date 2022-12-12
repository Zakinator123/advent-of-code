package aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static aoc.Day11.OperationType.*;

@SuppressWarnings("unused")
public class Day11 implements DaySolver {

    enum OperationType {ADD, MULTIPLY, SELF_MULTIPLY}
    record ItemWorryLevelAndDestination(Long worryLevel, int monkey) {}
    record Monkey(
            List<Long> itemWorryLevels,
            OperationType operationType,
            Integer operand,
            int divisibleBy,
            int trueMonkey,
            int falseMonkey
    ) {}

    public String solvePuzzle1(List<String> input) { return getMonkeyBusiness(input, 3, 20); }
    public String solvePuzzle2(List<String> input) { return getMonkeyBusiness(input, 1, 10000); }

    private String getMonkeyBusiness(List<String> input, int worryLevelDivider, int numRounds) {
        List<Monkey> monkeyList = parseMonkeyInput(Utilities.parseInputDelimitedByBlankLines(input));
        long[] monkeyInspections = getMonkeyInspectionFrequencies(monkeyList, worryLevelDivider, numRounds);
        return String.valueOf(Arrays.stream(monkeyInspections).sorted().skip(monkeyInspections.length - 2).reduce(1, (el1, el2) -> el1*el2));
    }

    private long[] getMonkeyInspectionFrequencies(List<Monkey> monkeyList, int worryLevelDivider, int numRounds) {
        var monkeyInspections = new long[monkeyList.size()];
        final int productOfDivisors = monkeyList.stream().map(monkey -> monkey.divisibleBy).reduce(1, (monkey1Divisor, monkey2Divisor) -> monkey1Divisor * monkey2Divisor);
        IntStream.range(0, numRounds).forEach(i ->
                IntStream.range(0, monkeyList.size()).forEach(j -> {
                    Monkey monkey = monkeyList.get(j);
                    monkey.itemWorryLevels
                            .stream()
                            .map(item -> getNewWorryLevelAndDestinationMonkey(item, monkey, worryLevelDivider, productOfDivisors))
                            .forEach(itemWorryLevelAndDestination -> monkeyList.get(itemWorryLevelAndDestination.monkey).itemWorryLevels.add(itemWorryLevelAndDestination.worryLevel));
                    monkeyInspections[j] += monkey.itemWorryLevels.size();
                    monkey.itemWorryLevels.clear();
                }));
        return monkeyInspections;
    }

    private ItemWorryLevelAndDestination getNewWorryLevelAndDestinationMonkey(Long item, Monkey monkey, int worryLevelDivider, int productOfDivisors) {
        long worryLevel = switch (monkey.operationType) {
            case ADD -> item + monkey.operand;
            case MULTIPLY -> item * monkey.operand;
            case SELF_MULTIPLY -> item * item;
        };

        if (worryLevelDivider != 1) worryLevel = worryLevel / worryLevelDivider;
        else if (worryLevel > ((long) monkey.divisibleBy * monkey.divisibleBy)) worryLevel = worryLevel % productOfDivisors;

        int monkeyToThrowTo = worryLevel % monkey.divisibleBy == 0 ? monkey.trueMonkey : monkey.falseMonkey;
        return new ItemWorryLevelAndDestination(worryLevel, monkeyToThrowTo);
    }

    private List<Monkey> parseMonkeyInput(List<List<String>> input) {
        return input.stream()
                .map(monkeyStrings -> {
                    List<Long> items = new ArrayList<>(Arrays
                            .stream(monkeyStrings.get(1).substring(18).split(", "))
                            .map(Long::parseLong)
                            .toList());
                    String opStr = monkeyStrings.get(2);
                    OperationType operationType = getOperationType(opStr);
                    Integer operand = operationType != SELF_MULTIPLY ? Integer.parseInt(opStr.substring(25)) : null;
                    int divisibleBy = Integer.parseInt(monkeyStrings.get(3).substring(21));
                    int trueMonkey = Integer.parseInt(monkeyStrings.get(4).substring(29));
                    int falseMonkey = Integer.parseInt(monkeyStrings.get(5).substring(30));

                    return new Monkey(items, operationType, operand, divisibleBy, trueMonkey, falseMonkey);
                })
                .toList();
    }

    private OperationType getOperationType(String s) {
        if (s.charAt(23) == '+' ) return ADD;
        if (s.contains("old * old")) return SELF_MULTIPLY;
        return MULTIPLY;
    }
}
