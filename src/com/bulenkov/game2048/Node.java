package com.bulenkov.game2048;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private State state;
    private List<Node> children;
    private Direction direction;
    private double value;

    public Node(State state, Direction direction) {
        this.state = state;
        children = new ArrayList<Node>();
        this.direction = direction;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public double getProbability() {
        int space = getState().getAvailableSpace();
        return (1.0 / ((space == 0) ? 1 : space) * (getState().isIs2NewlyAdded() ? 0.9 : 0.1));
    }

}