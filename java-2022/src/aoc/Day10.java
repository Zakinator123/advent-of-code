package aoc;

import java.util.List;

@SuppressWarnings("unused")
public class Day10 implements DaySolver {

    enum InstructionType {
        ADDX(2),
        NOOP(1);
        private final int numCycles;
        InstructionType(int numCycles) { this.numCycles = numCycles; }
        public int getNumCycles() { return this.numCycles; }
    }

    static final class Instruction {
        private final InstructionType instructionType;
        private final Integer value;
        private int numCyclesRemaining;

        Instruction(InstructionType instructionType, Integer value) {
            this.instructionType = instructionType;
            this.value = value;
            this.numCyclesRemaining = instructionType.numCycles;
        }
    }

    static class InstructionPipeline {
        private final List<Instruction> instructions;
        private int currentInstructionIndex;
        int cycle;
        int registerValue;
        boolean pipelineComplete;

        public InstructionPipeline(List<Instruction> parsedInstructions) {
            instructions = parsedInstructions;
            cycle = 1;
            currentInstructionIndex = 0;
            registerValue = 1;
            pipelineComplete = false;
        }

        public void applyCycle() {
            Instruction currentInstruction = instructions.get(currentInstructionIndex);
            currentInstruction.numCyclesRemaining--;

            if (currentInstruction.numCyclesRemaining == 0) {
                currentInstructionIndex++;
                if (currentInstruction.instructionType == InstructionType.ADDX) registerValue = registerValue + currentInstruction.value;
            }
            cycle++;
            if (currentInstructionIndex == instructions.size()) pipelineComplete = true;
        }
    }

    @Override
    public String solvePuzzle1(List<String> input) {
        InstructionPipeline pipeline = getInstructionPipeline(input);
        int signalStrengthSum = 0;
        int nextSignalStrengthIndex = 20;

        while (!pipeline.pipelineComplete) {
            pipeline.applyCycle();
            int signalStrengthSumIncrease = nextSignalStrengthIndex == pipeline.cycle ? pipeline.cycle * pipeline.registerValue : 0;
            signalStrengthSum += signalStrengthSumIncrease;
            if (signalStrengthSumIncrease != 0) nextSignalStrengthIndex += 40;
        }
        return String.valueOf(signalStrengthSum);
    }

    @Override
    public String solvePuzzle2(List<String> input) {
        InstructionPipeline pipeline = getInstructionPipeline(input);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 40; j++) {
                int currentSpritePosition = pipeline.registerValue;
                if (Math.abs(currentSpritePosition - j) <= 1) System.out.print("#");
                else System.out.print(".");
                pipeline.applyCycle();
            }
            System.out.println();
        }
        return "";
    }

    private InstructionPipeline getInstructionPipeline(List<String> input) {
        return new InstructionPipeline(input.stream().map(inputLine -> {
            String[] instruction = inputLine.split(" ");
            Integer value = instruction.length == 1 ? null : Integer.parseInt(instruction[1]);
            return new Instruction(InstructionType.valueOf(instruction[0].toUpperCase()), value);
        }).toList());
    }
}
