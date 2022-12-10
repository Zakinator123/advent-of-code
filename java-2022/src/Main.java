import aoc.DaySolver;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class Main {

    public static void main(String[] args) throws Exception {
        final String dayNumber = args[0];

        Class<?> clazz = Class.forName(format("aoc.Day%s", dayNumber));
        DaySolver daySolver = (DaySolver) clazz.getDeclaredConstructor().newInstance();

        Path pathToInputFile = Paths.get(requireNonNull(Main.class.getResource(format("resources/day%s.txt", dayNumber))).toURI());
        List<String> input = Files.readAllLines(pathToInputFile);

        System.out.println(daySolver.solvePuzzle1(input));
        System.out.println(daySolver.solvePuzzle2(input));
    }
}
