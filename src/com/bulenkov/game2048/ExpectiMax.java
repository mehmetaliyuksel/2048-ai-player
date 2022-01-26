package com.bulenkov.game2048;

import java.util.ArrayList;
import java.util.List;

public class ExpectiMax {

    // Getting expectimax
    public Node expectiMax(Node node, boolean isMaxNode, int depth, int ply) {

        addChildren(node, isMaxNode);
        if (ply * 2 == depth + 1) {
            if (node.getChildren().isEmpty()) {
                double val = node.getProbability() * heuristic1(node.getState().getTiles());
                if (Double.compare(val, (double) 0) == 0)
                    System.out.println();
                node.setValue(val);
            } else {

                double val = node.getChildren()
                        .stream()
                        .mapToDouble(child -> child.getProbability() * heuristic1(child.getState().getTiles()))
                        .sum();
                node.setValue(val);
                if (Double.compare(val, (double) 0) == 0)
                    System.out.println();
            }
            return node;
        }

        if (isMaxNode) {
            // if (node.getChildren().isEmpty()) {
            // //System.out.println();
            // node.setValue(expectiMax(node, false, depth + 1, ply).getValue());
            // } else {
            node.setValue(node.getChildren()
                    .stream()
                    .mapToDouble(child -> {
                        double val = expectiMax(child, false, depth + 1, ply).getValue();
                        System.out.println("VALLLLLLLLL=== " + val);
                        if (Double.compare(val, (double) 0) == 0)
                            System.out.println();
                        return val;
                    })
                    .max()
                    .getAsDouble());
            // }

        } else {
            // if (node.getChildren().isEmpty()) {
            // //System.out.println();
            // node.setValue(expectiMax(node, true, depth + 1, ply).getValue());
            // } else {
            node.setValue(node.getChildren()
                    .stream()
                    .mapToDouble(child -> {
                        Node newNode = expectiMax(child, true, depth + 1, ply);
                        return child.getProbability() * newNode.getValue();
                    })
                    .sum());
            // }

        }

        return node;
    }

    public void addChildren(Node node, boolean isMaxDepth) {

        if (!Game2048Util.canMove(node.getState().getTiles(), node.getState().getAvailableSpace()))
            return;

        if (isMaxDepth) {
            Tile[] tiles = node.getState().getTiles().clone();

            CheckNewState left = Game2048Util.left(tiles);
            if (left.isCanMove()) {
                Node newNode = new Node(
                        new State(left.getTiles(), node.getState().isIs2NewlyAdded(), getAvailableSpace(tiles)),
                        Direction.LEFT);
                node.getChildren().add(newNode);
            }

            tiles = node.getState().getTiles().clone();
            CheckNewState right = Game2048Util.right(tiles);
            if (right.isCanMove()) {
                Node newNode = new Node(
                        new State(right.getTiles(), node.getState().isIs2NewlyAdded(), getAvailableSpace(tiles)),
                        Direction.RIGHT);
                node.getChildren().add(newNode);
            }

            tiles = node.getState().getTiles().clone();
            CheckNewState down = Game2048Util.down(tiles);
            if (down.isCanMove()) {
                Node newNode = new Node(
                        new State(down.getTiles(), node.getState().isIs2NewlyAdded(), getAvailableSpace(tiles)),
                        Direction.DOWN);
                node.getChildren().add(newNode);
            }

            tiles = node.getState().getTiles().clone();
            CheckNewState up = Game2048Util.up(tiles);
            if (up.isCanMove()) {
                Node newNode = new Node(
                        new State(up.getTiles(), node.getState().isIs2NewlyAdded(), getAvailableSpace(tiles)),
                        Direction.UP);
                node.getChildren().add(newNode);
            }
        } else {
            for (int index = 0; index < node.getState().getAvailableSpace(); index++) {
                Tile[] tiles = Game2048.copyMyTiles(node.getState().getTiles());
                boolean isIs2NewlyAdded = Game2048Util.addTile(tiles);

                Node newNode = new Node(new State(tiles, isIs2NewlyAdded, getAvailableSpace(tiles)), Direction.LEFT);
                node.getChildren().add(newNode);
            }
        }

    }

    private int getAvailableSpace(Tile[] tiles) {
        int counter = 0;
        for (Tile t : tiles) {
            if (t.isEmpty()) {
                counter++;
            }
        }
        return counter;
    }

    public Result max(Tile[] state, int depth, int ply) {

        double maxUtility = 0;
        Result result = new Result(0, Direction.UP);

        // recursively traverse each next available state and set the max utility
        // score
        Tile[] newState = new Tile[state.length];
        newState = Game2048.copyMyTiles(state);
        // System.arraycopy(state, 0, newState, 0, state.length);
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
        // System.arraycopy(state, 0, newState, 0, state.length);
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
        // System.arraycopy(state, 0, newState, 0, state.length);
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
        // System.arraycopy(state, 0, newState, 0, state.length);
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

        if (numOfAvailableSpace - ply < 0)
            return heuristic1(state);

        for (int i = 0; i < numOfAvailableSpace; i++) {
            if (i > 0)
                availableSpacesOnBoard.get(i - 1).value = 0;

            //boolean isNewlyAddedTileIs2 = Game2048Util.addOneTile(availableSpacesOnBoard.get(i));
            //double possibility = (isNewlyAddedTileIs2) ? 0.9 * 1 / numOfAvailableSpace : 0.1 * 1 / numOfAvailableSpace;
            //scores.add(possibility * max(state, depth + 1, ply).getUtility());

            availableSpacesOnBoard.get(i).value = 2;
            double possibility = 0.9 * (1.0 / (numOfAvailableSpace) );
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

            //System.out.println("********************************************* " + possibility + "  NUMM: " + numOfAvailableSpace);
        return possibility * heuristic1(state);
    }

    public double calculateEvaluationScoreOfLeaves(Tile[] state, int ply) {
        List<Tile> availableSpacesOnBoard = Game2048Util.availableSpace(state);
        List<Double> scores = new ArrayList<>();

        if (availableSpacesOnBoard.size() - ply < 0)
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

                heur += WEIGHTMATRIX3[i] * state[i].value;
            }
        }

         //return Math.pow(heur, Math.log(Game2048Util.availableSpace(state).size()));
        //return heur * Math.pow(5, mergeCounter) * maxValue;
        // return 1;
        //System.out.println("++++++++++y+++++++++++++++++++++++++++++++++++++++++++ " + heur);
        return heur;
    }

}
