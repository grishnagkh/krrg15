/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.connect4.solver.mcts;

import java.util.ArrayList;

/**
 *
 * @author erich
 */
public interface Node_I {
    public void setParent(Node_I n);
    public Node_I getParent();
    public Object getAction();
    public ArrayList<Node_I> getChildren();
    public Node_I createChild(Object action);
    public void setChild(Node_I n);
    public ArrayList<Object> getUntriedActions();
    public boolean hasUntriedActions();
    public boolean isTerminal();
    public double getValue();
    public void setValue(double value);
    public int getVisits();
    public void setVisits(int num);
    public double getReward(double outcome);
    public double getOutcome();
}
