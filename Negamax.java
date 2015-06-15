/**
 *
 * simple negamax implementation without transposition tables
 *
 * @author stefan
 *
 */
public class Negamax implements ISolver {

	private int pid, oid, cutoff;
	private GameLogic gameLogic;
	private Evaluator e;

	public Negamax(int playerId, GameLogic gl, int depth, Evaluator e) {
		/*
		 * not: pid = -oid must hold, else the implementation of negamax does
		 * not work
		 */
		pid = playerId;
		gameLogic = gl;
		cutoff = depth;
		this.e = e;
	}

	/**
	 * returns an integer representing the column to put in the next coin
	 */
	@Override
	public int getDecision(State s) {
		System.out.println("wake up negamax!");

		State child;
		int chosenVal = Integer.MIN_VALUE, chosenMove = -1;
		for (int col : s.openCols) {
			child = new State(s);
			child.insertCoin(col, pid); // we set our move
			int negaVal = -negamax(child, cutoff - 1, -pid); //opponents negaval
			if (negaVal > chosenVal) {
				chosenVal = negaVal;
				chosenMove = col;
			}
		}
		return chosenMove;
	}

	private int negamax(State s, int d, int color) {

		System.out
				.println("hello, this is negamax and i'll stay alive forever, and no one knows why");
		System.out.println("currently i am at color " + color + " and depth "
				+ d);
		if (d == 0 || gameLogic.TerminalTest(s) != 0) {
			System.out.println("I'll return " + (color * e.eval(s)) + " here");
			return color * e.eval(s);
		}
		int act, best = Integer.MIN_VALUE;
		State child;
		for (int col : s.openCols) {
			child = new State(s);
			child.insertCoin(col, color);
			act = -negamax(child, d - 1, -color);
			best = Math.max(act, best);
		}
		return best;
	}

}
