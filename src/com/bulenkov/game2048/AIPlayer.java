//package com.bulenkov.game2048;
//
//public class AIPlayer {
//
//    private Node rootNode;
//
//    public AIPlayer() {
//    }
//
//    public Direction getNextMove(Tile[] tiles, Ply ply) {
//
//        rootNode = new Node(new State(tiles, false, getAvailableSpace(tiles)));
//
//        switch (ply) {
//            case PLY_1:
//                generateTree(rootNode, 1, true);
//                break;
//            case PLY_2:
//                generateTree(rootNode, 2, true);
//                break;
//            case PLY_3:
//                generateTree(rootNode, 3, true);
//                break;
//            case PLY_4:
//                generateTree(rootNode, 4, true);
//                break;
//        }
//
//        return Direction.RIGHT;
//        // return Direction.DOWN;
//        // return Direction.LEFT;
//        // return Direction.UP;
//    }
//
//    public void generateTree(Node node, int ply, boolean isMaxDepth) {
//
//        if (!Game2048Util.canMove(node.getState().getTiles(), node.getState().getAvailableSpace()))
//            return;
//
//        if (isMaxDepth) {
//            Tile[] tiles = node.getState().getTiles().clone();
//            if (Game2048Util.left(tiles)) {
//                tiles = node.getState().getTiles().clone();
//                Game2048Util.left(tiles);
//
//                Node newNode = new Node(new State(tiles, node.getState().isIs2NewlyAdded(), getAvailableSpace(tiles)));
//                node.getChildren().add(newNode);
//                generateTree(newNode, ply, !isMaxDepth);
//            }
//
//            tiles = node.getState().getTiles().clone();
//            if (Game2048Util.right(tiles).isCanMove()) {
//                tiles = node.getState().getTiles().clone();
//                Game2048Util.right(tiles);
//
//                Node newNode = new Node(new State(tiles, node.getState().isIs2NewlyAdded(), getAvailableSpace(tiles)));
//                node.getChildren().add(newNode);
//                generateTree(newNode, ply, !isMaxDepth);
//            }
//
//            tiles = node.getState().getTiles().clone();
//            if (Game2048Util.down(tiles)) {
//                tiles = node.getState().getTiles().clone();
//                Game2048Util.up(tiles);
//
//                Node newNode = new Node(new State(tiles, node.getState().isIs2NewlyAdded(), getAvailableSpace(tiles)));
//                node.getChildren().add(newNode);
//                generateTree(newNode, ply, !isMaxDepth);
//            }
//
//            tiles = node.getState().getTiles().clone();
//            if (Game2048Util.up(tiles)) {
//                tiles = node.getState().getTiles().clone();
//                Game2048Util.down(tiles);
//
//                Node newNode = new Node(new State(tiles, node.getState().isIs2NewlyAdded(), getAvailableSpace(tiles)));
//                node.getChildren().add(newNode);
//                generateTree(newNode, ply, !isMaxDepth);
//            }
//        } else {
//            for (int index = 0; index < node.getState().getAvailableSpace(); index++) {
//                Tile[] tiles = node.getState().getTiles().clone();
//                boolean isIs2NewlyAdded = Game2048Util.addTile(tiles);
//
//                Node newNode = new Node(new State(tiles, isIs2NewlyAdded, getAvailableSpace(tiles)));
//                node.getChildren().add(newNode);
//                generateTree(newNode, ply, isMaxDepth);
//            }
//        }
//
//    }
//
//    private int getAvailableSpace(Tile[] tiles) {
//        int counter = 0;
//        for (Tile t : tiles) {
//            if (t.isEmpty()) {
//                counter++;
//            }
//        }
//        return counter;
//    }
//}