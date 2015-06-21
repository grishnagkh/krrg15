package at.connect4.solver.mcts;

import java.util.ArrayList;

import at.connect4.game.GameLogic;
import at.connect4.game.State;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author erich
 */
public class Node implements Node_I{
    public State state;
    public GameLogic logic;
    
    public Node(State state, GameLogic logic) {
        this.state = state;
        this.logic = logic;
    }
    
    public void setParent(Node_I n){
        throw new RuntimeException("to be implemented!");
    }
    
    public Node_I getParent(){
        throw new RuntimeException("to be implemented!");
    }
    
    public Object getAction(){
        throw new RuntimeException("to be implemented!");
    }
    
    public ArrayList<Node_I> getChildren(){
        throw new RuntimeException("to be implemented!");
    }
    
    public Node_I createChild(Object action){
        throw new RuntimeException("to be implemented!");
    }
    
    public void setChild(Node_I n){
        throw new RuntimeException("to be implemented!");
    }
    
    public ArrayList<Object> getUntriedActions(){
        throw new RuntimeException("to be implemented!");
    }
    
    public boolean hasUntriedActions(){
        throw new RuntimeException("to be implemented!");
    }
    
    public boolean isTerminal(){
        throw new RuntimeException("to be implemented!");
    }
    
    public double getValue(){
        throw new RuntimeException("to be implemented!");
    }
    
    public void setValue(double value){
        throw new RuntimeException("to be implemented!");
    }
    
    public int getVisits(){
        throw new RuntimeException("to be implemented!");
    }
    
    public void setVisits(int num){
        throw new RuntimeException("to be implemented!");
    }
    
    public double getReward(double outcome){
        throw new RuntimeException("to be implemented!");
    }
    
    public double getOutcome(){
        throw new RuntimeException("to be implemented!");
    }
    
}
