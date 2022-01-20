package com.bulenkov.game2048;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private State state;
    private List<Node> children;
    private double value;

    public Node(State state) {
        this.setState(state);
        this.children = new ArrayList<>();
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getProbability() {
        return (1 / getState().getAvailableSpace()) * (getState().isIs2NewlyAdded() ? 0.9 : 0.1);
    }

}