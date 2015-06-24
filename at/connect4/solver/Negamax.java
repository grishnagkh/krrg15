package at.connect4.solver;

import at.connect4.eval.Evaluator;
import at.connect4.game.GameLogic;
import at.connect4.game.State;

/**
 *
 * simple negamax implementation without transposition tables
 *
 * @author stefan
 *
 */
public class Negamax implements ISolver {



	public static final int WIN = Integer.MAX_VALUE - 2;
	public static final int DRAW = 0;

	private int pid, oid, cutoff;
	private GameLogic gameLogic;
	private Evaluator e;

	private boolean first = true;

	public Negamax(int playerId, GameLogic gl, int depth, Evaluator e) {
		/*
		 * not: pid = -oid must hold, else the implementation of negamax does
		 * not work
		 */
		gameLogic = gl;
		cutoff = depth;
		this.e = e;
	}

	/**
	 * returns an integer representing the column to put in the next coin
	 */
	@Override
	public int getDecision(State s) {

		if(first){
			first = false;
			return s.gameBoard[0].length/2;
		}
			
		State child;
		int max = Integer.MIN_VALUE;
		int chosenMove = -1;
		for(int move: s.openCols){
			child = new State(s);	
			child.insertCoin(move, -child.getLastPlayer());

			int negaVal = negamax(child, cutoff, child.getLastPlayer());
			int minimaxVal = -negaVal;
			System.out.println("negamax value: " + negaVal );
			System.out.println("minimax value: " + minimaxVal );
			if(max < minimaxVal){
				chosenMove = move;
				max = minimaxVal;
			}
		}
		System.out.println("chosen move: " + chosenMove  +"\n ################ ");
		if(+WIN == max){
			javax.swing.JOptionPane.showMessageDialog(null, "we'll win! " + -s.getLastPlayer() );
		}
		if(-WIN == max){
			javax.swing.JOptionPane.showMessageDialog(null, "we'll loose! " + -s.getLastPlayer() );
		}
		return chosenMove;
	}

	private int negamax(State s, int d, int color) {
		int tt = 0;
		if((tt=gameLogic.TerminalTest(s)) != 0) {
			if(tt == color){
	System.out.println("WIN!");
				return -WIN;
			}else if(tt == -color){
	System.out.println("LOSS!");
				return WIN ; //LOSS;
			}else if(tt == 3){
	System.out.println("DRAW!");
				return DRAW;
			}
			
		}
		if (d == 0 ){ 
//	System.out.println("evaluating...");
//	s.printAll();
//	System.out.println("eval value: " + e.eval(s));
	
			return color * e.eval(s);
		}

		int act, best = Integer.MIN_VALUE;
		State child;
		for (int col : s.openCols) {
			child = new State(s);
			child.insertCoin(col, -child.getLastPlayer());
			act = -negamax(child, d - 1, -color);
//			System.out.println("child @depth " + d + " value " + act);
			best = Math.max(act, best);
		}
//		System.out.println("best child @depth " + d + " value " + best);
		return best;
	}

}
