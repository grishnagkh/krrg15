package at.connect4.game;

import at.connect4.solver.mcts.Uct4connect;
import at.connect4.solver.ISolver;
import at.connect4.eval.Evaluator;
import at.connect4.*;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.*;

public class ShowGame {
	static final int cols = 7;// 7;
	static final int rows = 6;// 6;

	public static void write(String text, String filename){
		try{
			PrintWriter out = new PrintWriter(filename);
			out.println(text);
			out.flush();
			out.close();
		}catch(Exception e){}
	}

	public static GameLogic getPCGameLogic(int depth, int pid){
		GameLogic gl = new GameLogic();
		gl.initializeGame(cols, rows, pid);
		Evaluator e = new at.connect4.eval.PieceCounter(gl);
		ISolver solver =  new at.connect4.solver.Negamax(gl.getPlayerID(), gl, depth, e);
		gl.setSolver(solver);
		return gl;
	}

	public static GameLogic getMCGameLogic(int depth, int pid, int nRuns){
		GameLogic gl = new GameLogic();
		gl.initializeGame(cols, rows, pid);
		at.connect4.eval.MCEval e = new at.connect4.eval.MCEval(gl);
		e.setRuns(nRuns);
		ISolver solver = new at.connect4.solver.Negamax(gl.getPlayerID(), gl, depth, e);
		gl.setSolver(solver);
		return gl;
	}
	public static GameLogic getUCTGameLogic(int pid, int time){
		GameLogic gl = new GameLogic();
		gl.initializeGame(cols, rows, pid);
		ISolver solver = new Uct4connect(gl.playerID, gl.opponentID, gl, time);
		gl.setSolver(solver);
		return gl;
	}


	public static void main(String[] arg) {
		fullEval();
	}
	public static void fullEval(){

		int[] runVal = {100,150,200};			
		int[] searchTime = {1,2,3};		
		int[] pcSearchDepths = {3,4,5,6};
		int[] mcSearchDepths = {3,4};
		StringBuffer sb = new StringBuffer();


		int filecnt = -1;
write(sb.toString(), ++filecnt +".txt");

		/* Piece counter VS Piece Counter */
 		for(int depth1: pcSearchDepths){
			for(int depth2: pcSearchDepths){
				String s = 
					"piece counter (" + depth1 + 
					") VS piece counter (" + depth2 + "):" 
					+ letPlay(getPCGameLogic(depth1, 1), getPCGameLogic(depth2, -1));
				System.out.println(s);
				sb.append(s + "\n");
			}
		}
 
write(sb.toString(), ++filecnt +".txt");

		/* mceval vs mceval */
		for(int runs1: runVal){
			for(int runs2: runVal){
				for(int d1: mcSearchDepths){
					for(int d2: mcSearchDepths){
						String s = 
							"mceval (" + d1 + 
							", nruns " + runs1 + ") VS mceval (" + d2 + ", nruns " + runs2 + "):" 
							+ letPlay(getMCGameLogic(d1, 1, runs1), getMCGameLogic(d2, -1, runs2));
						System.out.println(s);
						sb.append(s + "\n");
					}
				}
			}
		}
write(sb.toString(), ++filecnt +".txt");
		/* mceval  VS piece counter */
		for(int nruns: runVal){
			for(int d1: mcSearchDepths){
				for(int d2: pcSearchDepths){
					String s = 
						"mceval (" + d1 +" nruns " + nruns + 
						") VS piece counter (" + d2 + "):" 
						+ letPlay(getMCGameLogic(d1, 1, nruns), getPCGameLogic(d2, -1));
					System.out.println(s);
					sb.append(s + "\n");
					s = 
						"piece counter (" + d2 + 
						") VS mc eval (" + d1 + " nruns " + nruns + "):"  
						+ letPlay(getPCGameLogic(d2, 1), getMCGameLogic(d1, -1, nruns));
					System.out.println(s);
					sb.append(s + "\n");
				}
			}
		}
		
write(sb.toString(), ++filecnt +".txt");		
		/* uct vs uct*/

		for(int time1: searchTime){
			for(int time2: searchTime){
				String s = 
						"uct (" +time1 + 
						") VS uct (" + time2 + "):" 
						+ letPlay(getUCTGameLogic(1, time1), getUCTGameLogic(-1, time2));
					System.out.println(s);
					sb.append(s + "\n");
			}
		}
write(sb.toString(), ++filecnt +".txt");
	/* uct vs mceval */
		for(int time: searchTime){
			for(int nruns: runVal){
				for(int d: mcSearchDepths){
					String s = 
						"uct (" +time + ") "+
						"VS" + 
						"MCEval (" + d + "runs " + nruns + "): " 
						+ letPlay(getUCTGameLogic(1, time),  getMCGameLogic(d, -1, nruns));
					System.out.println(s);
					sb.append(s + "\n");
					 s = 
						"MCEval (" + d + " runs " + nruns + "): " 						
						+ "VS" 
						+ "uct (" +time + ") "
						+ letPlay( getMCGameLogic(d, 1, nruns), getUCTGameLogic(-1, time));

					System.out.println(s);
					sb.append(s + "\n");
				}
			}
		}
write(sb.toString(), ++filecnt +".txt");
		/* uct vs piece counter  */
		for(int time: searchTime){
			for(int d: pcSearchDepths){
				String s = 
					"uct (" +time + ") "+
					"VS" + 
					"piece counter (" + d + "): " 			
					+ letPlay(getUCTGameLogic(1, time),  getPCGameLogic(d, -1));
				System.out.println(s);
				sb.append(s + "\n");
				 s = 
					"piece counter (" + d + "): " 						
					+ "VS" 
					+ "uct (" +time + ") "
					+ letPlay( getPCGameLogic(d, 1), getUCTGameLogic(-1, time));

				System.out.println(s);
				sb.append(s + "\n");
			}
		}

write(sb.toString(), ++filecnt +".txt");		
		System.out.println(sb.toString());

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
            }else{
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
