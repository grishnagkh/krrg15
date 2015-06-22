package at.connect4.game;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class ShowGame {

	public static void main(String[] arg) {
		final int cols = 7;// 7;
		final int rows = 6;// 6;

		final GameLogic player1 = new GameLogic();
		final GameLogic player2 = new GameLogic();

		player1.initializeGame(cols, rows, 1);
		player2.initializeGame(cols, rows, -1);

		player1.setSolver(new at.connect4.solver.Negamax(player1.getPlayerID(),
				player1, 3, new at.connect4.eval.PieceCounter(player1)));
		player2.setSolver(new at.connect4.solver.Negamax(player2.getPlayerID(),
				player2, 4, new at.connect4.eval.PieceCounter(player2)));

		// player1.setSolver(new Minimax(player1.getPlayerID(),
		// player1.getOpponentID(), player1, 2, new MCEval(player1)));
		// player2.setSolver(new Minimax(player2.getPlayerID(),
		// player2.getOpponentID(), player2, 3, new MCEval(player2)));

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					FourConnectGUI g = new FourConnectGUI(player1, player2,
							cols, rows);
					JFrame f = new JFrame();
					f.setSize(1000, 800);
					f.setTitle("Connect 4");
					f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					f.getContentPane().add(g);
					f.setVisible(true);
				} catch (Exception e) {
					System.out
							.println("There has been an error in the implementation of your at.connect4.game.GameLogic");
				}
			}
		});

	}
}
