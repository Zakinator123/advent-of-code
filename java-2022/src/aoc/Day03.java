package aoc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day03 implements DaySolver {
    @Override
    public String solvePuzzle1(List<String> input) {
        return String.valueOf(input.stream().map(this::getItemPresentInBothCompartments).map(this::getItemPriority).mapToInt(Integer::intValue).sum());
    }

    @Override
    public String solvePuzzle2(List<String> input) {
        List<Character> badges = new ArrayList<>();
        for (int i = 0; i < input.size(); i+= 3) {
            badges.add(getBadgeForElfGroupof3(input, i));
        }

        return String.valueOf(badges.stream().map(this::getItemPriority).mapToInt(Integer::intValue).sum());
    }

    private Character getBadgeForElfGroupof3(List<String> input, int i) {
        String firstElf = input.get(i);
        Set<Character> seen1 = new HashSet<>();
        for (Character c : firstElf.toCharArray()) seen1.add(c);

        String secondElf = input.get(i +1);
        Set<Character> seen2 = new HashSet<>();
        for (Character c : secondElf.toCharArray()) if (seen1.contains(c)) seen2.add(c);

        String thirdElf = input.get(i +2);
        for (Character c : thirdElf.toCharArray()) if (seen2.contains(c)) return c;

        throw new RuntimeException("Could not find badge for elf group.");
    }

    private int getItemPriority(Character item) {
        int upperCaseAdjuster = (Character.isUpperCase(item) ? 26 : 0);
        return Character.getNumericValue(item) - 9 + upperCaseAdjuster;
    }

    private Character getItemPresentInBothCompartments(String bagContents) {
        String leftCompartment = bagContents.substring(0, bagContents.length()/2);
        String rightCompartment = bagContents.substring(bagContents.length()/2);

        Set<Character> leftHashSet = new HashSet<>();

        for (int i = 0; i < leftCompartment.length(); i++) {
            Character c = leftCompartment.charAt(i);
            leftHashSet.add(c);
        }

        Character badItem = null;
        for (int i = 0; i < rightCompartment.length(); i++) {
            Character c = rightCompartment.charAt(i);
            if (leftHashSet.contains(c)){
                badItem = c;
                break;
            }
        }

        return badItem;
    }
}
