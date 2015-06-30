package at.connect4.solver.mcts;

import at.connect4.game.GameLogic;
import at.connect4.game.State;

import java.util.ArrayList;

/**
 * Created by Verena on 26.06.2015.
 */
public class NodeVerena implements Node_I {
    private final Integer action;
    public State state;
    public GameLogic logic;

    private double value;
    private int visits;
    private ArrayList<Object> untriedActions;
    private ArrayList<Node_I> children;
    private Node_I parent;

    public NodeVerena(State state, GameLogic logic, Integer action) {
        this.state = state;
        this.logic = logic;
        this.value = 0;
        this.visits = 0;
        this.action = action;

        // create empty list with children
        children = new ArrayList<>();

        // set untriedActions for this.node
        untriedActions = new ArrayList<>();

        for (Integer column : state.openCols){
            untriedActions.add(column);
        }
    }

    public void setParent(Node_I n){
        this.parent = n;
    }

    public Node_I getParent(){
        return this.parent;
    }

    public Object getAction(){
        return this.action;
    }

    public ArrayList<Node_I> getChildren(){
        return this.children;
    }

    public Node_I createChild(Object action) {
        State nextState = new State(state);
        nextState.insertCoin((Integer) action, state.getCurrentPlayer());

        return new NodeVerena(nextState, logic, (Integer) action);
    }

    public void setChild(Node_I n){
        this.children.add(n);
    }

    public ArrayList<Object> getUntriedActions() {
        if (untriedActions == null || untriedActions.isEmpty()) {
            return null;
        }

        return untriedActions;
    }

    public boolean hasUntriedActions(){
        if (untriedActions.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isTerminal(){
        if (logic.TerminalTest(state) == 0) {
            return false;
        } else {
            return true;
        }
    }

    public double getValue(){
        return this.value;
    }

    public void setValue(double value){
        this.value = value;
    }

    public int getVisits(){
        return this.visits;
    }

    public void setVisits(int num) {
        this.visits = num;
    }

    public double getReward(double outcome){
        if (outcome == logic.getPlayerID()) {
            return 1;
        }

        if (outcome == logic.getOpponentID()) {
            return 0;
        }

        if (outcome == 3) {
            return 0.5;
        }

        return -1;
    }

    public double getOutcome(){
        return logic.TerminalTest(state);
    }
}
