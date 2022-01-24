package com.bulenkov.game2048;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.DoubleStream;

public class ExpectiMax {


    // Getting expectimax
//    public double expectiMax(Node node, boolean isMaxNode) {
//
//        // Base case
//        if (node.getChildren().isEmpty()) {
//            return node.getValue();
//        }
//
//        if (isMaxNode) {
//            return node.getChildren()
//                    .stream()
//                    .mapToDouble(child -> expectiMax(child, false))
//                    .max()
//                    .getAsDouble();
//        }
//
//        return node.getChildren()
//                .stream()
//                .mapToDouble(child -> child.getProbability() * expectiMax(child, true))
//                .sum();
//    }

    public Result max(Tile[] state, int depth, int ply) {

        double maxUtility = 0;
        Result result = null;

        // recursively traverse each next available state and set the max utility score

        Tile[] newState = state.clone();

        if (Game2048Util.left(newState)) {
            double newUtility = chance(newState, depth + 1, ply);

            if (newUtility > maxUtility)
                result = new Result(newUtility, Direction.LEFT);
        }

        newState = state.clone();

        if (Game2048Util.right(newState)) {
            double newUtility = chance(newState, depth + 1, ply);

            if (newUtility > maxUtility)
                result = new Result(newUtility, Direction.RIGHT);
        }

        newState = state.clone();

        if (Game2048Util.up(newState)) {
            double newUtility = chance(newState, depth + 1, ply);

            if (newUtility > maxUtility)
                result = new Result(newUtility, Direction.UP);
        }

        newState = state.clone();

        if (Game2048Util.down(newState)) {
            double newUtility = chance(newState, depth + 1, ply);

            if (newUtility > maxUtility)
                result = new Result(newUtility, Direction.DOWN);
        }

        return result;


    }

    public double chance(Tile[] state, int depth, int ply) {
        List<Double> scores = new ArrayList<>();

        // if depth is reached calculate all possible chances' evaluation score
        if (depth + 1 == ply * 2) {
            return calculateEvaluationScoreOfLeaves(state);
        }

        // else, continue recursively with the next max
        List<Tile> availableSpacesOnBoard = Game2048Util.availableSpace(state);
        int numOfAvailableSpace = availableSpacesOnBoard.size();
        for (Tile tile : availableSpacesOnBoard) {
            boolean isNewlyAddedTileIs2 = Game2048Util.addOneTile(tile);
            double possibility = (isNewlyAddedTileIs2) ? 0.9 * 1 / numOfAvailableSpace : 0.1 * 1 / numOfAvailableSpace;
            scores.add(possibility * max(state, depth + 1, ply).getUtility());
        }

        return scores.stream()
                .mapToDouble(a -> a)
                .average()
                .getAsDouble();

    }

    public double evaluate(Tile[] state, boolean isNewlyAddedTileIs2, int numOfAvailableSpace) {

        double possibility = (isNewlyAddedTileIs2) ? 0.9 * 1 / numOfAvailableSpace : 0.1 * 1 / numOfAvailableSpace;

        return possibility * heuristic1(state);
    }

    public double calculateEvaluationScoreOfLeaves(Tile[] state) {
        List<Tile> availableSpacesOnBoard = Game2048Util.availableSpace(state);
        List<Double> scores = new ArrayList<>();

        for (Tile tile : availableSpacesOnBoard) {
            scores.add(evaluate(state, Game2048Util.addOneTile(tile), availableSpacesOnBoard.size()));
        }

        return scores.stream()
                .mapToDouble(a -> a)
                .average()
                .getAsDouble();
    }

    public double heuristic1(Tile[] state) {
        return 1;
    }
}
