package com.bulenkov.game2048;

import java.util.ArrayList;
import java.util.List;

public class ExpectiMax {


    public Result max(Tile[] state, int depth, int ply) {

        double maxUtility = 0;
        Result result = null;// new Result(0, Direction.UP);

        // recursively traverse each next available state and set the max utility

        Tile[] newState;
        newState = Game2048.copyMyTiles(state);

        CheckNewState left = Game2048Util.left(newState);
        if (left.isCanMove()) {
            double newUtility = chance(left.getTiles(), depth + 1, ply);
            System.out.println(
                    "DEPTH: " + depth + "-- LEFT : newUtil ----------" + newUtility + "------maxUtil :"
                            + maxUtility);

            if (newUtility >= maxUtility) {
                maxUtility = newUtility;
                result = new Result(newUtility, Direction.LEFT);
            }
        }
        newState = Game2048.copyMyTiles(state);

        CheckNewState right = Game2048Util.right(newState);
        if (right.isCanMove()) {
            double newUtility = chance(right.getTiles(), depth + 1, ply);
            System.out.println(
                    "DEPTH: " + depth + "-- RIGHT : newUtil ---------- " + newUtility + "------maxUtil:" + maxUtility);

            if (newUtility >= maxUtility) {
                maxUtility = newUtility;
                result = new Result(newUtility, Direction.RIGHT);
            }
        }

        newState = Game2048.copyMyTiles(state);

        CheckNewState up = Game2048Util.up(newState);
        if (up.isCanMove()) {
            double newUtility = chance(up.getTiles(), depth + 1, ply);
            System.out
                    .println("DEPTH: " + depth + "-- UP : newUtil ----------" + newUtility +
                            "------maxUtil :" + maxUtility);
            if (newUtility >= maxUtility) {
                maxUtility = newUtility;
                result = new Result(newUtility, Direction.UP);
            }
        }
        newState = Game2048.copyMyTiles(state);

        CheckNewState down = Game2048Util.down(newState);
        if (down.isCanMove()) {
            double newUtility = chance(down.getTiles(), depth + 1, ply);
            System.out.println(
                    "DEPTH: " + depth + "-- DOWN : newUtil ----------" + newUtility + "------maxUtil :"
                            + maxUtility);

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

        if (numOfAvailableSpace == 0)
            return heuristic1(state);

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

        return possibility * heuristic1(state);
    }

    public double calculateEvaluationScoreOfLeaves(Tile[] state, int ply) {
        List<Tile> availableSpacesOnBoard = Game2048Util.availableSpace(state);
        List<Double> scores = new ArrayList<>();

        if (availableSpacesOnBoard.size() == 0)
            return heuristic1(state);

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

    /*
     * 0 1 2 3
     *
     * 4 3 3 4
     * 3 2 2 3
     * 3 2 2 3
     * 4 3 3 4
     */
    public double heuristic1(Tile[] state) {
        double heur = 0;
        int mergeCounter = 1;
        int maxValue = 0;
        int[] WEIGHTMATRIX = {
                17, 15, 13, 11,
                15, 9, 10, 11,
                13, 10, 8, 8,
                11, 11, 8, 8
        };
        int[] WEIGHTMATRIX3 = {
                50, 30, 20, 20,
                30, 20, 15, 15,
                15, 5, 0, 0,
                -5, -5, -10, -15
        };
        double[] WEIGHTMATRIX2 = {
                Math.pow(2, 15), Math.pow(2, 14), Math.pow(2, 13), Math.pow(2, 12),
                Math.pow(2, 8), Math.pow(2, 9), Math.pow(2, 10), Math.pow(2, 11),
                Math.pow(2, 7), Math.pow(2, 6), Math.pow(2, 5), Math.pow(2, 4),
                Math.pow(2, 0), Math.pow(2, 1), Math.pow(2, 2), Math.pow(2, 3)
        };

        double[] WEIGHTMATRIX4 =
                {0.135759, 0.121925, 0.102812, 0.099937,
                        0.0997992, 0.0888405, 0.076711, 0.0724143,
                        0.060654, 0.0562579, 0.037116, 0.0161889,
                        0.0125498, 0.00992495, 0.00575871, 0.00335193};

        // int DEPTHMAP[] = { 6, 6, 6, 6, 5, 5, 5, 5, 5, 5, 4, 4,
        // 4, 4, 4, 4 };

        for (int i = 0; i < state.length; i++) {
            if (state[i].value != 0) {
                if (state[i].value >= maxValue)
                    maxValue = state[i].value;
                // double logValue = (Math.log10(state[i].value) / Math.log10(2));
                // if (i == 0 || i == 3 || i == 12 || i == 15)
                // heur += state[i].value * 4;
                // else if (i == 1 || i == 2 || i == 4 || i == 7 || i == 8 || i == 11 || i == 13
                // || i == 14)
                // heur -= logValue * 2;
                // else
                // heur -= state[i].value;
                if ((i + 1) % 4 != 0) {
                    if (state[i + 1] == state[i])
                        mergeCounter++;
                }

                if (i <= 11) {
                    if (state[i + 4] == state[i])
                        mergeCounter++;
                }

                heur += WEIGHTMATRIX4[i] * state[i].value;
            }
        }

        //return Math.pow(heur, Math.log(Game2048Util.availableSpace(state).size()));
        //return heur * Math.pow(5, mergeCounter) * maxValue;
        // return 1;
        //System.out.println("++++++++++y+++++++++++++++++++++++++++++++++++++++++++ " + heur);
        return heur;
    }

}
