package at.connect4.solver.mcts;

import at.connect4.game.GameLogic;
import at.connect4.game.State;

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author erich
 */
public class Node implements Node_I {
    public State state;
    public GameLogic logic;

    private ArrayList<Node_I> children;
    private Node_I parent;
    private Integer action;

    private ArrayList<Integer> untriedActions;
    private int visits;
    private double value;

    public Node(State state, GameLogic logic) {
        this.state = state;
        this.logic = logic;

        this.untriedActions = new ArrayList<>(state.openCols);

        this.visits = 0;
    }

    public void setParent(Node_I n) {
        this.parent = n;
    }

    public Node_I getParent() {
        return parent;
    }

    public Object getAction() {
        return action;
    }

    public void setAction(Object action) {
        if (action instanceof Integer) {
            this.action = (Integer) action;
        }
    }

    public ArrayList<Node_I> getChildren() {
        return children;
    }

    public Node_I createChild(Object action) {
        try {
            Integer chosenColumn = (Integer) action;
            int currentPlayer = state.getCurrentPlayer();

            State childState = new State(state);
            childState.insertCoin(chosenColumn, currentPlayer);

            GameLogic childLogic = new GameLogic();
            childLogic.board = childState;
            childLogic.playerID = this.logic.opponentID;
            childLogic.opponentID = this.logic.playerID;

            Node child = new Node(childState, childLogic);
            child.setParent(this);
            child.setAction(action);

            untriedActions.remove(action);

            return child;
        } catch (ClassCastException cce) {
            System.out.println("Action provided to create a child node is no Integer: " + action);
            return null;
        }
    }

    public void setChild(Node_I child) {
        children.add(child);
    }

    public ArrayList<Object> getUntriedActions() {
        return (ArrayList<Object>) (ArrayList<?>) untriedActions;
    }

    public boolean hasUntriedActions() {
        return !untriedActions.isEmpty();
    }

    public boolean isTerminal() {
        return logic.TerminalTest(state) != 0;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int num) {
        this.visits = num;
    }

    public double getReward(double outcome) {
        return (int) outcome == logic.playerID ? 1 : 0;
    }

    public double getOutcome() {
        int outcome = logic.TerminalTest(state);
        //Draw is neutral
        if (outcome == 3) {
            return 0;
        }

        return outcome;
    }

}
