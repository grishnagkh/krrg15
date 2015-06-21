package at.connect4.eval;

import at.connect4.game.GameLogic;
import at.connect4.game.State;

public class PieceCounter implements Evaluator{

	public static final int TWO_CHAIN = 2;
	public static final int THREE_CHAIN = 5;

	public static final int OFFSET = Integer.MAX_VALUE / 2; //to not have negative values^^

	GameLogic gl;

	int pid, oid;

	public PieceCounter(GameLogic gl){
		this.gl = gl;
		pid = gl.getPlayerID();
		oid = -pid; //NEGAMAX
	}

	@Override
	public int eval(State s){


		int tt = gl.TerminalTest(s);
		if(tt == pid){
			return Integer.MAX_VALUE;
		}
		if(tt == oid){
			return Integer.MIN_VALUE;
		}
		if(tt == 3){
			return 0; //DRAW
		}

		int colSum, rowSum, diagSum;

		colSum = evalCols(s.gameBoard);
		rowSum = evalRows(s.gameBoard);
		diagSum = evalDiag(s.gameBoard, false);
		diagSum += evalDiag(s.gameBoard, true);

		return colSum + rowSum + diagSum;
	}
	public int evalCols(int [][] gb){
		int[] res = new int[2];

		for(int col = 0; col<gb.length;col++){
			
			int row = gb[0].length;
			
			if(gb[col][row-1] != 0){
				/* the column is closed */
				continue;
			}


			while(--row > -1 && gb[col][row] == 0)
				; //go downwards until we hit a coin or the end of the board

			/* we are now on the uppermost coin */
			if(row < 2 ) 
				continue; // goto next col, we can not have chains here ;)

			int count = 1;
			int topPlayer = gb[col][row];
			while(--row > -1 && topPlayer == gb[col][row])
				count++; //go down until we hit the board's end or another color coin

			/* grant points */
		
			int p =	pid == topPlayer ? 0 : 1;
			res[p] += count == 2 ? TWO_CHAIN : 0;
			res[p] += count == 3 ? THREE_CHAIN : 0;
		}
		return res[1] - res[0];
	}
	public int evalRows(int[][] gb){
		int sum = 0;

		int[] res = new int[2];
		
		for(int[] row: gb){
			for(int i=-1; i<row.length-1; /* nop */){
				//search a zero....
				while(++i<row.length-2 && row[i] != 0)
					;

				if(i>=row.length-2)
					break; //we're at the right end

				/* we are on a zero now, check to the right */
				int cnt = 0; //the count (dracula)
				int p = row[i+1]; //player

				while(++i<row.length-1 && row[i] == p)
					cnt++;

				p = (p == pid) ? 0 : 1;
	
				res[p] += cnt == 2 ? TWO_CHAIN : 0;
				res[p] += cnt == 3 ? THREE_CHAIN : 0;
			}
			//and now to the left..
			for(int i=row.length; i > 1; /* nop */){
				//search a zero
				while(--i > 1 && row[i] != 0)
					; 

				// we have a zero, let's search to the left

				if(i <= 1)
					break; //at left end

				int cnt = 0;
				int p = row[i-1];
				while(--i > 0 && row[i] == p)
					cnt++;

				p = (p==pid) ? 0 : 1;

				res[p] += cnt == 2 ? TWO_CHAIN : 0;
				res[p] += cnt == 3 ? THREE_CHAIN : 0;

			
			}

			/* note: chains with 2 open ends are twice as valuable as such with 1 open end :)*/
		}

        return res[0] - res[1];
	}
	public int evalDiag(int[][] board, boolean mirrored){
		if(mirrored){
			board = mirror(board);
		}
		
		int dimA = board.length;
		int dimB = board[0].length;
		int[][] transformed = new int[dimA+dimB][dimB];

	// j: column^^
		for( int k = 0 ; k < dimA + dimB ; k++ ) {
        	for( int j = 0 ; j <= k ; j++ ) {
            	int i = k - j;
            	if( i < dimA && j < dimB ) {
                	transformed[k][j] =  board[i][j];
            	}
			}
        }	
		
		return evalRows(transformed);
	}
 	private int[][] mirror(int[][] gameBoard) {
    	int[][] ret = new int[gameBoard.length][gameBoard[0].length];

	    for (int i = 0; i < gameBoard.length; i++)
    		for (int j = 0; j < gameBoard[0].length; j++)
        		ret[i][j] = gameBoard[gameBoard.length - 1 - i][j];
    	return ret;
  	}
}
