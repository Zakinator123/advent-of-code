package aoc;

import java.util.List;

import static aoc.Day02.GameResult.*;
import static aoc.Day02.RPSHand.*;

public class Day02 implements DaySolver {

    enum RPSHand {
        ROCK(1),
        PAPER(2),
        SCISSORS(3);

        private final int handValue;

        RPSHand(int handValue) {
            this.handValue = handValue;
        }
    }

    enum GameResult {
        WIN(6),
        DRAW(3),
        LOSS(0);

        private final int gameResultScoreValue;

        GameResult(int gameResultScoreValue) {
            this.gameResultScoreValue = gameResultScoreValue;
        }
    }

    private RPSHand encryptedHandConverter(String encryptedHandName) {
        return switch (encryptedHandName) {
            case "A", "X" -> ROCK;
            case "B", "Y" -> PAPER;
            case "C", "Z" -> SCISSORS;
            default -> throw new IllegalStateException("Unexpected value: " + encryptedHandName);
        };
    }

    private GameResult encryptedGameResultConverter(String encryptedGameResultName) {
        return switch (encryptedGameResultName) {
            case "X" -> LOSS;
            case "Y" -> DRAW;
            case "Z" -> WIN;
            default -> throw new IllegalStateException("Unexpected value: " + encryptedGameResultName);
        };
    }

    int getGameResult(RPSHand opponentsHand, RPSHand myHand) {
        GameResult gameResult = null;
        if (opponentsHand.equals(myHand)) gameResult = DRAW;
        else if (opponentsHand.equals(ROCK) && myHand.equals(PAPER)) gameResult = WIN;
        else if (opponentsHand.equals(ROCK) && myHand.equals(SCISSORS)) gameResult = LOSS;
        else if (opponentsHand.equals(SCISSORS) && myHand.equals(ROCK)) gameResult = WIN;
        else if (opponentsHand.equals(SCISSORS) && myHand.equals(PAPER)) gameResult = LOSS;
        else if (opponentsHand.equals(PAPER) && myHand.equals(ROCK)) gameResult = LOSS;
        else if (opponentsHand.equals(PAPER) && myHand.equals(SCISSORS)) gameResult = WIN;

        return gameResult.gameResultScoreValue + myHand.handValue;
    }

    int getGameResult(RPSHand opponentsHand, GameResult gameResult) {
        RPSHand myHand = null;
        if (gameResult.equals(DRAW)) myHand = opponentsHand;
        else if (opponentsHand.equals(ROCK) && gameResult.equals(WIN)) myHand = PAPER;
        else if (opponentsHand.equals(ROCK) && gameResult.equals(LOSS)) myHand = SCISSORS;
        else if (opponentsHand.equals(SCISSORS) && gameResult.equals(WIN)) myHand = ROCK;
        else if (opponentsHand.equals(SCISSORS) && gameResult.equals(LOSS)) myHand = PAPER;
        else if (opponentsHand.equals(PAPER) && gameResult.equals(WIN)) myHand = SCISSORS;
        else if (opponentsHand.equals(PAPER) && gameResult.equals(LOSS)) myHand = ROCK;

        return gameResult.gameResultScoreValue + myHand.handValue;
    }

    @Override
    public String solvePuzzle1(List<String> input) {
        return String.valueOf(input.stream()
                .map(row -> getGameResult(encryptedHandConverter(row.split(" ")[0]), encryptedHandConverter(row.split(" ")[1])))
                .mapToInt(Integer::intValue)
                .sum());
    }

    @Override
    public String solvePuzzle2(List<String> input) {
        return String.valueOf(input.stream()
                .map(row -> getGameResult(encryptedHandConverter(row.split(" ")[0]), encryptedGameResultConverter(row.split(" ")[1])))
                .mapToInt(Integer::intValue)
                .sum());
    }
}