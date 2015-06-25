package at.connect4.eval;

import java.util.Random;

import at.connect4.game.GameLogic;
import at.connect4.game.State;

public class MCEval implements Evaluator {

	Random random;
	GameLogic gl;
	int runs = 50;

	int pid, oid; // player id, opponent id
	int turn; // indicating which player's turn it is

	public MCEval(GameLogic gl) {
		random = new Random();
		this.gl = gl;
		pid = gl.getPlayerID();
		oid = -pid; // at.connect4.solver.Negamax only ;)
		turn = pid;
	}

	public void setRuns(int n){
		runs = n;
	}

	@Override
	public int eval(State s) {
		int randomCol;
		int score = 0;

		for (int i = 0; i < runs; i++) {

			State clone = new State(s);

			while (gl.TerminalTest(clone) == 0) {
				randomCol = random.nextInt(clone.openCols.size());
				clone.insertCoin(clone.openCols.get(randomCol), turn);
				turn = (turn == pid) ? oid : pid;
			}

			if (gl.TerminalTest(clone) == pid) {
				score += 2;
			} else if (gl.TerminalTest(clone) == oid) {
				// loss;
			} else {
				score++;
			}
		}
		return score;
	}
}
