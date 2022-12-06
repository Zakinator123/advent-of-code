package aoc;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class Day06 implements DaySolver {
    @Override
    public String solvePuzzle1(List<String> input) {
        return getEndIndexOfWindowWithDistinctCharacters(4, input.get(0));
    }

    @Override
    public String solvePuzzle2(List<String> input) {
        return getEndIndexOfWindowWithDistinctCharacters(14, input.get(0));
    }

    private String getEndIndexOfWindowWithDistinctCharacters(int windowSize, String input) {
        var seenMap = new HashMap<Character, Integer>();
        char[] signalChars = input.toCharArray();

        int slidingWindowStart = 0;
        for (int i = 0; i < signalChars.length; i++) {
            if (i-slidingWindowStart == windowSize) return String.valueOf(i);
            Character c = signalChars[i];
            if (seenMap.containsKey(c) && seenMap.get(c) >= slidingWindowStart) slidingWindowStart = seenMap.get(c)+1;
            seenMap.put(c, i);
        }
        throw new RuntimeException();
    }
}
