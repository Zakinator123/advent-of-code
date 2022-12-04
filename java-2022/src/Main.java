import aoc.Day01;
import aoc.Day02;
import aoc.Day03;
import aoc.DaySolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class Main {

    private static final Map<String, DaySolver> daySolverMap = Map.of(
            "01", new Day01(),
            "02", new Day02(),
            "03", new Day03());

    public static void main(String[] args) {

        if (args.length == 0) throw new RuntimeException("A 'day' CLI argument must be provided to the application.");
        var day = args[0];

        var inputStream = ClassLoader.getSystemResourceAsStream(format("resources/day%s.txt", day));
        if (inputStream == null) throw new RuntimeException("Could not read in resource file.");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            var input = reader.lines().collect(Collectors.toList());

            System.out.println(daySolverMap.get(day).solvePuzzle1(input));
            System.out.println(daySolverMap.get(day).solvePuzzle2(input));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
