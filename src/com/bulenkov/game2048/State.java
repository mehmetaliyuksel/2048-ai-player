package com.bulenkov.game2048;

public class State {

    private Tile[] tiles;
    private double utility;
    private boolean is2NewlyAdded;
    private int availableSpace;

    public State(Tile[] tiles, boolean is2NewlyAdded, int availableSpace) {
        this.tiles = tiles;
        this.is2NewlyAdded = is2NewlyAdded;
        this.availableSpace = availableSpace;
        setUtility(Math.random());

    }

    public int getAvailableSpace() {
        return availableSpace;
    }

    public void setAvailableSpace(int availableSpace) {
        this.availableSpace = availableSpace;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public void setTiles(Tile[] tiles) {
        this.tiles = tiles;
    }

    public double getUtility() {
        return utility;
    }

    public void setUtility(double utility) {
        this.utility = utility;
    }

    public boolean isIs2NewlyAdded() {
        return is2NewlyAdded;
    }

    public void setIs2NewlyAdded(boolean is2NewlyAdded) {
        this.is2NewlyAdded = is2NewlyAdded;
    }

}
