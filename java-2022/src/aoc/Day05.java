package aoc;

import java.util.*;
import java.util.stream.Collectors;

public class Day05 implements DaySolver {

    record PuzzleData (List<List<Integer>> modifications, Map<Integer, Deque<Character>> stacks) {}

    @Override
    public String solvePuzzle1(List<String> input) {
        PuzzleData puzzleData = getPuzzleData(input);
        modifyStacks(puzzleData);
        return puzzleData.stacks.values().stream().map(stack -> String.valueOf(stack.peek())).collect(Collectors.joining());
    }

    @Override
    public String solvePuzzle2(List<String> input) {
        PuzzleData puzzleData = getPuzzleData(input);
        modifyStacksV2(puzzleData);
        return puzzleData.stacks.values().stream().map(stack -> String.valueOf(stack.peek())).collect(Collectors.joining());
    }

    private PuzzleData getPuzzleData(List<String> input) {
        List<String> initialStacksConfiguration = new ArrayList<>();
        int modificationInputStart = 0;
        for (String s : input) {
            modificationInputStart++;
            if (s.isBlank()) break;
            initialStacksConfiguration.add(s);
        }

        List<List<Integer>> parsedModifications = parseModifications(input.subList(modificationInputStart, input.size()));
        Map<Integer, Deque<Character>> stacks = getStacksFromInput(initialStacksConfiguration);
        PuzzleData puzzleData = new PuzzleData(parsedModifications, stacks);
        return puzzleData;
    }

    private List<List<Integer>> parseModifications(List<String> unParsedModifications) {
        return unParsedModifications.stream()
                .map(modification -> modification.replace("move ", ""))
                .map(modification -> modification.replace(" from ", ","))
                .map(modification -> modification.replace(" to ", ","))
                .map(modificationString -> Arrays.asList(modificationString.split(",")))
                .map(modificationCharList -> modificationCharList.stream().map(string -> Integer.parseInt(string)).toList())
                .toList();
    }

    private void modifyStacks(PuzzleData puzzleData) {
        for (List<Integer> modification : puzzleData.modifications) {
            for (int i = 0; i < modification.get(0); i++) {
                Character poppedStackElement = puzzleData.stacks.get(modification.get(1)).pop();
                puzzleData.stacks.get(modification.get(2)).push(poppedStackElement);
            }
        }
    }

    private void modifyStacksV2(PuzzleData puzzleData) {
        for (List<Integer> modification : puzzleData.modifications) {
            Deque<Character> characterStack = new ArrayDeque<Character>();
            for (int i = 0; i < modification.get(0); i++) {
                Character poppedStackElement = puzzleData.stacks.get(modification.get(1)).pop();
                characterStack.push(poppedStackElement);
            }

            for (int i = 0; i < modification.get(0); i++) {
                puzzleData.stacks.get(modification.get(2)).push(characterStack.pop());
            }
        }
    }

    private Map<Integer, Deque<Character>> getStacksFromInput(List<String> initialStacks) {
        String stackLabelLine= initialStacks.get(initialStacks.size()-1);
        List<Integer> actualStackValueIndices = new ArrayList<>();
        Map<Integer, Deque<Character>> stacks = new HashMap<>();
        for (int i = 0; i < stackLabelLine.length(); i++) {
            if (stackLabelLine.charAt(i) != ' ') {
                stacks.put(stacks.size()+1, new ArrayDeque<>());
                actualStackValueIndices.add(i);
            }
        }

        for (int i = initialStacks.size()-2; i >= 0; i--) {
            String currentLine = initialStacks.get(i);
            for (int j = 0; j < actualStackValueIndices.size(); j++) {
                if (actualStackValueIndices.get(j) >= currentLine.length()) continue;
                Character currentChar = currentLine.charAt(actualStackValueIndices.get(j));
                if (currentChar.equals(' ')) continue;
                stacks.get(j+1).push(currentChar);
            }
        }

        return stacks;
    }
}
