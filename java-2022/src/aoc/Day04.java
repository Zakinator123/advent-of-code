package aoc;

import java.util.List;

public class Day04 implements DaySolver {

    record Range(int beginRange, int endRange) {};

    record RangePair (Range range1, Range range2) {};
    @Override
    public String solvePuzzle1(List<String> input) {
        return String.valueOf(input.stream().map(this::getRangePair).filter(this::rangeFullyOverlaps).count());
    }
    @Override
    public String solvePuzzle2(List<String> input) {
        return String.valueOf(input.stream().map(this::getRangePair).filter(this::rangePartiallyOrFullyOverlaps).count());
    }

    private RangePair getRangePair(String inputLine) {
        String[] rangeStrings = inputLine.split(",");
        return new RangePair(getRange(rangeStrings[0]), getRange(rangeStrings[1]));
    }

    private boolean rangePartiallyOrFullyOverlaps(RangePair rangePair) {
        return rangeFullyOverlaps(rangePair) || rangePartiallyOverlaps(rangePair);
    }

    private Range getRange(String range) {
        String[] rangeValues = range.split("-");
        return new Range(Integer.parseInt(rangeValues[0]), Integer.parseInt(rangeValues[1]));
    }

    private boolean rangeFullyOverlaps(RangePair rangePair) {
        return rangeContainsRange(rangePair.range1, rangePair.range2) || rangeContainsRange(rangePair.range2, rangePair.range1);
    }

    private boolean rangePartiallyOverlaps(RangePair rangePair) {
        return intInRange(rangePair.range2.endRange, rangePair.range1) || intInRange(rangePair.range2.beginRange, rangePair.range1);
    }

    private boolean intInRange(int integer, Range range) {
        return range.beginRange <= integer && range.endRange >= integer;
    }

    private boolean rangeContainsRange(Range range1, Range range2) {
       return range1.beginRange <= range2.beginRange && range1.endRange >= range2.endRange;
    }

}
