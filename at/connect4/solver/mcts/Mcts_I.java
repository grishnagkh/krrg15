package at.connect4.solver.mcts;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author erich
 */
public interface Mcts_I{
    public void setBudget(int budget);
    public int getBudget();
    public Node_I walk(Node_I n);
    public Node_I expand(Node_I n);
    public double playout(Node_I n);
    public void backup(Node_I n, double value);
    public Node_I getBestChild(Node_I n, double c);
    public double getEpsilon();
    public void setEpsilon(double epsilon);
    public Node_I search(Node_I n);
}
