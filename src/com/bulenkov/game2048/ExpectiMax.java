package com.bulenkov.game2048;

import java.util.ArrayList;
import java.util.List;

public class ExpectiMax {


    public Result max(Tile[] state, int depth, int ply) {

        double maxUtility = 0;
        Result result = new Result(0, Direction.UP);

        // recursively traverse each next available state and set the max utility

        Tile[] newState;
        newState = Game2048.copyMyTiles(state);

        CheckNewState left = Game2048Util.left(newState);
        if (left.isCanMove()) {
            double newUtility = chance(left.getTiles(), depth + 1, ply);


            if (newUtility >= maxUtility) {
                maxUtility = newUtility;
                result = new Result(newUtility, Direction.LEFT);
            }
        }
        newState = Game2048.copyMyTiles(state);

        CheckNewState right = Game2048Util.right(newState);
        if (right.isCanMove()) {
            double newUtility = chance(right.getTiles(), depth + 1, ply);

            if (newUtility >= maxUtility) {
                maxUtility = newUtility;
                result = new Result(newUtility, Direction.RIGHT);
            }
        }

        newState = Game2048.copyMyTiles(state);

        CheckNewState up = Game2048Util.up(newState);
        if (up.isCanMove()) {
            double newUtility = chance(up.getTiles(), depth + 1, ply);

            if (newUtility >= maxUtility) {
                maxUtility = newUtility;
                result = new Result(newUtility, Direction.UP);
            }
        }
        newState = Game2048.copyMyTiles(state);

        CheckNewState down = Game2048Util.down(newState);
        if (down.isCanMove()) {
            double newUtility = chance(down.getTiles(), depth + 1, ply);

            if (newUtility >= maxUtility) {
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
            return calculateEvaluationScoreOfLeaves(state, ply);
        }

        // else, continue recursively with the next max
        List<Tile> availableSpacesOnBoard = Game2048Util.availableSpace(state);
        int numOfAvailableSpace = availableSpacesOnBoard.size();

        for (int i = 0; i < numOfAvailableSpace; i++) {
            if (i > 0)
                availableSpacesOnBoard.get(i - 1).value = 0;


            availableSpacesOnBoard.get(i).value = 2;
            double possibility = 0.9 * (1.0 / (numOfAvailableSpace));
            scores.add(possibility * max(state, depth + 1, ply).getUtility());

            possibility = 0.1 * (1.0 / (numOfAvailableSpace));
            availableSpacesOnBoard.get(i).value = 4;

            scores.add(possibility * max(state, depth + 1, ply).getUtility());

        }

        return scores.stream()
                .mapToDouble(a -> a)
                .sum();

    }

    public double evaluate(Tile[] state, boolean isNewlyAddedTileIs2, int numOfAvailableSpace) {

        double possibility = (isNewlyAddedTileIs2) ? 0.9 * 1 / numOfAvailableSpace : 0.1 * 1 / numOfAvailableSpace;

        return possibility * heuristic(state);
    }

    public double calculateEvaluationScoreOfLeaves(Tile[] state, int ply) {
        List<Tile> availableSpacesOnBoard = Game2048Util.availableSpace(state);
        List<Double> scores = new ArrayList<>();

        if (availableSpacesOnBoard.size() == 0)
            return heuristic(state);

        for (int i = 0; i < availableSpacesOnBoard.size(); i++) {

            if (i > 0)
                availableSpacesOnBoard.get(i - 1).value = 0;


            availableSpacesOnBoard.get(i).value = 2;
            scores.add(evaluate(state,
                    true,
                    availableSpacesOnBoard.size()));

            availableSpacesOnBoard.get(i).value = 4;
            scores.add(evaluate(state,
                    false,
                    availableSpacesOnBoard.size()));
        }

        return scores.stream()
                .mapToDouble(a -> a)
                .sum();
    }

    public double heuristic(Tile[] state) {
        double heur = 0;
        int mergeCounter = 1;
        int maxValue = 0;


        int[] WEIGHTMATRIX1 = {
                310, 290, 270, 250,
                220, 210, 200, 170,
                150, 130, 110, 90,
                70, 50, 30, 10
        };

        int[] WEIGHTMATRIX2 = {
                310, 290, 270, 250,
                270, 250, 160, 140,
                150, 130, 60, 40,
                60, 40, 6, 4
        };


        for (int i = 0; i < state.length; i++) {
            if (state[i].value != 0) {
                if (state[i].value >= maxValue)

                    if ((i + 1) % 4 != 0) {
                        if (state[i + 1] == state[i])
                            mergeCounter++;
                    }

                if (i <= 11) {
                    if (state[i + 4] == state[i])
                        mergeCounter++;
                }

                heur += WEIGHTMATRIX2[i] * state[i].value;
            }
        }


        return heur; //-->>> heur1 && heur2
        //return (heur * 0.7) + (Game2048Util.availableSpace(state).size() * 0.3); //--> heur3
        //return (Game2048Util.availableSpace(state).size() * 0.5) + (mergeCounter * 0.2) + (maxValue * 0.3); //--> heur4
    }

}
