package at.connect4.eval;

import at.connect4.game.State;

public interface Evaluator {
	public int eval(State s);
}
