package at.connect4.solver.mcts;

import at.connect4.game.GameLogic;
import at.connect4.game.State;
import at.connect4.solver.ISolver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author erich
 */
public class Uct4connect extends Uct implements ISolver {
    public int opponentID;
    public int playerID;
    public GameLogic logic;
    
    public Uct4connect(int playerID, int opponentID, GameLogic logic, int secs) {
        this.playerID = playerID;
        this.opponentID = opponentID;
        this.logic = logic;
        super.setBudget(secs);
    }
    
    @Override
    public int getDecision(State s){
        Node n = new Node(s,this.logic);
        Node x = (Node) super.search(n);
        System.out.println(x.getAction());
        return (Integer) x.getAction();
    }
}
