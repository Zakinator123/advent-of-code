package aoc;

import java.util.ArrayList;
import java.util.List;

public class Utilities {
    static List<List<String>> parseInputDelimitedByBlankLines(List<String> input) {
        // Add blank line to end of input if one is not already there.
        if (!input.get(input.size()-1).isBlank()) input.add("");

        var parsedInput = new ArrayList<List<String>>();
        var currentList = new ArrayList<String>();
        for (var line : input) {
            if (line.isBlank()) {
                parsedInput.add(currentList);
                currentList = new ArrayList<>();
            } else currentList.add(line);
        }
        return parsedInput;
    }
}
