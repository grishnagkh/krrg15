package at.connect4.game;

import at.connect4.solver.mcts.Uct4connect;
import at.connect4.solver.ISolver;
import at.connect4.eval.Evaluator;
import at.connect4.*;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ShowGame {

	public static void main(String[] arg) {
		final int cols = 7;// 7;
		final int rows = 6;// 6;

		final GameLogic player1 = new GameLogic();
		final GameLogic player2 = new GameLogic();

		player1.initializeGame(cols, rows, 1);
		player2.initializeGame(cols, rows, -1);


		Evaluator pc1 = new at.connect4.eval.PieceCounter(player1);
		Evaluator pc2 = new at.connect4.eval.PieceCounter(player1);
		Evaluator mc1 = new at.connect4.eval.MCEval(player1);
		Evaluator mc2 = new at.connect4.eval.MCEval(player2);

		int nruns1, nruns2;

		nruns1= 100;
		//nruns1= 100;
		//nruns1= 150;
		//nruns1= 200;
		//nruns2 = 50;
		nruns2 = 200;
		//nruns2 = 150;
//		nruns2 = 200;

		((at.connect4.eval.MCEval) mc1).setRuns(nruns1);
		((at.connect4.eval.MCEval) mc2).setRuns(nruns2);

		int depth1, depth2;

//		depth1 = 3;
//		depth2 = 3;
		depth1 = 4;
		depth2 = 4;
	//	depth1 = 5;
	//	depth2 = 5;
//		depth1 = 6;
//		depth2 = 6;


		ISolver pcsolver1 = new at.connect4.solver.Negamax(player1.getPlayerID(), player1, depth1, pc1);
		ISolver pcsolver2 = new at.connect4.solver.Negamax(player2.getPlayerID(), player2, depth2, pc2);
		ISolver mcsolver1 = new at.connect4.solver.Negamax(player1.getPlayerID(), player1, depth1, mc1);
		ISolver mcsolver2 = new at.connect4.solver.Negamax(player2.getPlayerID(), player2, depth2, mc2);


		int time;
		time = 1;
		time = 2;
		time = 3;

		ISolver uct1 = new Uct4connect(player1.playerID, player1.opponentID, player1, time);
		ISolver uct2 = new Uct4connect(player2.playerID, player2.opponentID, player1, time);

		player1.setSolver(pcsolver1);
		player2.setSolver(pcsolver2);
//		player1.setSolver(mcsolver1);
//		player2.setSolver(mcsolver2);
//		player1.setSolver(uct1);
//		player2.setSolver(uct2);


//		player1.setSolver(new at.connect4.solver.Negamax(player1.getPlayerID(),
//				player1, 6, new at.connect4.eval.PieceCounter(player1)));
//		player2.setSolver(new at.connect4.solver.Negamax(player2.getPlayerID(),
//				player2, 6, new at.connect4.eval.PieceCounter(player2)));

		
		//player1.setSolver(new at.connect4.solver.Minimax(player1.getPlayerID(),
		// player1.getOpponentID(), player1, 3, new at.connect4.eval.PieceCounter(player1)));
		// player2.setSolver(new at.connect4.solver.Minimax(player2.getPlayerID(),
		// player2.getOpponentID(), player2, 3, new at.connect4.eval.PieceCounter(player2)));

        //player1.setSolver(new Uct4connect(player1.playerID, player1.opponentID, player1, 1));
        //player2.setSolver(new Uct4connect(player2.playerID, player2.opponentID, player2, 1));
/*
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
*/
		int winner = letPlay(player1,player2);
		if(winner == player1.playerID){
			System.out.println("Winner: Player 1");			
		}else if(winner == player2.playerID){
			System.out.println("Winner: Player 2");			
		}else{
			System.out.println("DRAW");
		}
	}

	

	public static int letPlay(GameLogic player1, GameLogic player2){
		int winner = 0;
		int playerTurn = player1.playerID;
		while (winner == 0){
            int col=-1;
            if (playerTurn == player1.playerID){
                col = player1.decideNextMove();                    
                player2.insertCoin(col,player1.playerID);
                player1.insertCoin(col,player1.playerID);
                winner = player1.gameFinished();
                playerTurn = player2.playerID;                   
            }
            else{
                col = player2.decideNextMove();               
                player1.insertCoin(col,player2.playerID);
                player2.insertCoin(col,player2.playerID);
                winner = player2.gameFinished();
                playerTurn = player1.playerID;
                       
            }
        }
		return winner;
	}
}
