package com.bulenkov.game2048;

import java.util.ArrayList;
import java.util.List;

public class ExpectiMax {

    // Getting expectimax
    // public double expectiMax(Node node, boolean isMaxNode) {
    //
    // // Base case
    // if (node.getChildren().isEmpty()) {
    // return node.getValue();
    // }
    //
    // if (isMaxNode) {
    // return node.getChildren()
    // .stream()
    // .mapToDouble(child -> expectiMax(child, false))
    // .max()
    // .getAsDouble();
    // }
    //
    // return node.getChildren()
    // .stream()
    // .mapToDouble(child -> child.getProbability() * expectiMax(child, true))
    // .sum();
    // }

    public Result max(Tile[] state, int depth, int ply) {

        double maxUtility = 0;
        Result result = null;

        // recursively traverse each next available state and set the max utility score
        Tile[] newState = new Tile[state.length];
        System.arraycopy(state, 0, newState, 0, state.length);

        if (Game2048Util.left(newState)) {
            double newUtility = chance(newState, depth + 1, ply);
            System.out.println("LEFT : newUtil ----------" + newUtility + "maxUtil :" + maxUtility);

            if (newUtility > maxUtility) {
                maxUtility = newUtility;
                result = new Result(newUtility, Direction.LEFT);
            }
        }

        System.arraycopy(state, 0, newState, 0, state.length);
        CheckNewState right = Game2048Util.right(newState);
        if (right.isCanMove()) {
            double newUtility = chance(right.getTiles(), depth + 1, ply);
            System.out.println("RIGHT : newUtil ----------" + newUtility + "maxUtil :" + maxUtility);

            if (newUtility > maxUtility) {
                maxUtility = newUtility;
                result = new Result(newUtility, Direction.RIGHT);
            }
        }

        System.arraycopy(state, 0, newState, 0, state.length);
        CheckNewState up = Game2048Util.up(newState);
        if (up.isCanMove()) {
            double newUtility = chance(up.getTiles(), depth + 1, ply);
            System.out.println("UP : newUtil ----------" + newUtility + "maxUtil :" + maxUtility);
            if (newUtility > maxUtility) {
                maxUtility = newUtility;
                result = new Result(newUtility, Direction.UP);
            }
        }

        System.arraycopy(state, 0, newState, 0, state.length);
        CheckNewState dowm = Game2048Util.down(newState);
        if (dowm.isCanMove()) {
            double newUtility = chance(dowm.getTiles(), depth + 1, ply);
            System.out.println("DOWN : newUtil ----------" + newUtility + "maxUtil :" + maxUtility);

            if (newUtility > maxUtility) {
                maxUtility = newUtility;
                result = new Result(newUtility, Direction.DOWN);
            }
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
                .sum();

    }

    public double evaluate(Tile[] state, boolean isNewlyAddedTileIs2, int numOfAvailableSpace) {

        double possibility = (isNewlyAddedTileIs2) ? 0.9 * 1 / numOfAvailableSpace : 0.1 * 1 / numOfAvailableSpace;

        return possibility * heuristic1(state);
    }

    public double calculateEvaluationScoreOfLeaves(Tile[] state) {
        List<Tile> availableSpacesOnBoard = Game2048Util.availableSpace(state);
        List<Double> scores = new ArrayList<>();

        for (int i = 0; i < availableSpacesOnBoard.size(); i++) {

            if (i > 0)
                availableSpacesOnBoard.get(i - 1).value = 0;

            scores.add(evaluate(state, Game2048Util.addOneTile(availableSpacesOnBoard.get(i)),
                    availableSpacesOnBoard.size()));
        }

        return scores.stream()
                .mapToDouble(a -> a)
                .sum();
    }

    public double heuristic1(Tile[] state) {
        int heur = 1;

        for (int i = 0; i < state.length; i++) {
            if (i == 0 || i == 3 || i == 12 || i == 15)
                if (state[i].value != 0)
                    heur += 100000;
                else
                    heur += 1;
        }

        return heur;
    }
}
