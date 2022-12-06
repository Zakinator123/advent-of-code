package aoc;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

@SuppressWarnings("unused")
public class Day01 implements DaySolver {

    @Override
    public String solvePuzzle1(List<String> input) {
        List<Integer> elvesWithHighestCalories = getKGreatestCaloriesAmounts(input, 1);
        return String.valueOf(elvesWithHighestCalories.get(0));
    }

    @Override
    public String solvePuzzle2(List<String> input) {
        List<Integer> nElvesWithCalories = getKGreatestCaloriesAmounts(input, 2);
        return String.valueOf(nElvesWithCalories.stream().mapToInt(Integer::intValue).sum());
    }

    private List<Integer> getKGreatestCaloriesAmounts(List<String> input, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        int currentCaloriesForElf = 0;
        for (String s : input) {
            if (s.isBlank()) {
                if (minHeap.size() < k) minHeap.add(currentCaloriesForElf);
                else if (Objects.requireNonNull(minHeap.peek()) < currentCaloriesForElf) {
                    minHeap.poll();
                    minHeap.add(currentCaloriesForElf);
                }
                currentCaloriesForElf = 0;
            } else {
                currentCaloriesForElf += Integer.parseInt(s);
            }
        }
        return minHeap.stream().sorted(Collections.reverseOrder()).toList();
    }
}
