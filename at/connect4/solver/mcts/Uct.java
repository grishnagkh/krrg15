/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.connect4.solver.mcts;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author erich
 */
public class Uct implements Mcts_I{
    protected int budget = Integer.MAX_VALUE;
    protected double epsilon = 1/Math.sqrt(2);
    protected Random random = new Random();
    
    @Override
    public void setEpsilon(double epsilon){
        this.epsilon = epsilon;
    }
    
    @Override
    public double getEpsilon(){
        return this.epsilon;
    }
    
    @Override
    public void setBudget(int budget){
        this.budget = budget;
    }
    @Override
    public int getBudget(){
        return this.budget;
    }
    
    @Override
    public Node_I search(Node_I n){
        long start = System.currentTimeMillis();
        long end = System.currentTimeMillis();
        long secs = (end-start)/1000;
        
        Node_I x;
        double val;
        
        while(secs < this.budget){
            x = this.walk(n);
            val = this.playout(x);
            this.backup(x, val);
            
            end = System.currentTimeMillis();
            secs = (end-start)/1000;
        }
        
        return this.getBestChild(n, 0);
    }
    
    @Override
    public Node_I walk(Node_I n){
        while(!n.isTerminal()){
            if(n.hasUntriedActions()){
                return this.expand(n);
            }
            else{
                n = this.getBestChild(n, this.epsilon);
            }
        }
        
        return n;
    }
    
    @Override
    public Node_I expand(Node_I n){
        ArrayList <Object> actions = n.getUntriedActions();
        Node_I x = n.createChild(actions.get(this.random.nextInt(actions.size())));
        n.setChild(x);
        
        return x;
    }
    
    @Override
    public double playout(Node_I n){
        ArrayList <Object> actions;
        
        while(!n.isTerminal()){
            actions = n.getUntriedActions();
            n = n.createChild(actions.get(this.random.nextInt(actions.size()))); 
        }
        
        return n.getOutcome();
    }
    
    @Override
    public void backup(Node_I n, double outcome){
        while(n != null){
            n.setVisits(n.getVisits()+1);
            n.setValue(n.getValue()+n.getReward(outcome));  
            n = n.getParent();
        }
    }
    
    @Override
    public Node_I getBestChild(Node_I n, double c){
        Node_I best = null;
        double bestVal = Double.MIN_VALUE;
        double curVal;
        
        for(Node_I x : n.getChildren()){
            curVal = (x.getValue()/(x.getVisits()+(this.random.nextDouble()*1e-6))) + c*Math.sqrt((2*Math.log(n.getVisits()))/(x.getVisits()+(this.random.nextDouble()*1e-6)));
            if(curVal > bestVal){
                bestVal = curVal;
                best = x;
            }
        }
        
        return best;
    }
}
