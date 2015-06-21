package at.connect4.solver;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import at.connect4.game.State;

/**
 *
 * @author erich
 */
public interface ISolver {
    public int getDecision(State s);
}
