import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Minimax implements ISolver{
        private int cutoffdepth, transTblCutoffDepth;
        public int playerID, opponentID;
        private boolean returnTwice = false;
        private HashMap<State, Integer> transTbl = new HashMap<State, Integer>(100000);
        private static int hitCounter = 0, count = 0;
        private GameLogic gameLogic;
        private enum CutoffActions {finalState, cutoff, playOn}
        private final int winSymbol = Integer.MAX_VALUE - 1;
        private final int lossSymbol = Integer.MIN_VALUE + 1;
        private boolean firstMove = true;

        private Random random = new Random();
        private SeqFabian seq;

        private int quadLength;
        private int excessRows;
        private int excessCols;
      
        public int runs = 100;
        
        public Minimax(int playerID, int opponentID, GameLogic gameLogic, int d){
                this.playerID = playerID;
                this.opponentID = opponentID;
                this.gameLogic = gameLogic;
//                seq = new Seq(playerID, opponentID);
                quadLength = Math.min(gameLogic.board.gameBoard.length, gameLogic.board.gameBoard[0].length);
                excessRows = Math.max(0, gameLogic.board.gameBoard[0].length - gameLogic.board.gameBoard.length);
                excessCols = Math.max(0, gameLogic.board.gameBoard.length - gameLogic.board.gameBoard[0].length);      
                this.cutoffdepth = d;
                this.transTblCutoffDepth = d;
        }
        
        public int getDecision(State s){
                
                if (firstMove) {
                        firstMove = false;
                        return s.gameBoard.length / 2 ;
                }
                
                long startTime = System.currentTimeMillis();
                int pick = Integer.MIN_VALUE; 
                int v = Integer.MIN_VALUE;
                for (Integer column : s.openCols){
                        State sPrime = new State(s);
                        sPrime.insertCoin(column, playerID);
                        int val;
//                      val = MinValue(sPrime, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
//                      System.out.println("col: " + column + " val: "+val);
                        if (transTbl.containsKey(sPrime)) {
                                val = transTbl.get(sPrime);
                                hitCounter++;
                        } else {
                                val = MinValue(sPrime, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
                                if (val == winSymbol || val == lossSymbol)transTbl.put(sPrime, val);
                        }
                        
                        if (val > v){
                                pick = column;
                                if (val == winSymbol) break;
                                v = val;
                        }
                }
                long endTime = System.currentTimeMillis();
                long timeTaken = (endTime - startTime) / 1000;
                if (timeTaken < 6) {
                        cutoffdepth++;
                } 
                System.out.println("time: " + timeTaken);
                System.out.println("cutoff: " + cutoffdepth);
                System.out.println("hits: " + hitCounter);
                System.out.println("count: " + count);
                System.out.println("tvlSize: " + transTbl.size() + "\n");
                count = hitCounter = 0;
                return pick;
        }
        
        public int MaxValue(State s, int alpha, int beta, int depth){
                count++;

                CutoffActions action = cutoffTest(s, depth);
                if (action != CutoffActions.playOn){
                        if (action == CutoffActions.finalState){
                                int utility = gameLogic.TerminalTest(s);
                                if (utility == playerID) return winSymbol;
                                else if (utility == opponentID) return lossSymbol;
                                else return -1000;
                        }
                        else if (action == CutoffActions.cutoff) return evalVerena(s);
                }
                
                int v = Integer.MIN_VALUE;
                for (Integer column : s.openCols){
                        State sPrime = new State(s);
                        sPrime.insertCoin(column, playerID);
                        int val;
//                      val = MinValue(sPrime, alpha, beta, depth+1);
                        if (transTbl.containsKey(sPrime)) {
                                val = transTbl.get(sPrime);
                                hitCounter++;
                        } else {
                                val = MinValue(sPrime, alpha, beta, depth+1);
                                if (depth < transTblCutoffDepth && (val == winSymbol || val == lossSymbol) ) transTbl.put(sPrime, val);
                                }
                        
                        if (val > v){
                                if (val == winSymbol) return val;
                                v = val;
                        }
                        if (v >= beta) return v;
                        if (v > alpha) alpha = v;
                }
                return v;
        }
        
        public int MinValue(State s, int alpha, int beta, int depth){
                count++;
                CutoffActions action = cutoffTest(s, depth);
                if (action != CutoffActions.playOn){
                        if (action == CutoffActions.finalState){
                                int utility = gameLogic.TerminalTest(s);
                                if (utility == playerID) return winSymbol;
                                else if (utility == opponentID) return lossSymbol;
                                else return -1000  ;
                        }
                        else if (action == CutoffActions.cutoff) return evalVerena(s);
                }

                int v = Integer.MAX_VALUE;
                for (Integer column : s.openCols){
                        State sPrime = new State(s);
                        sPrime.insertCoin(column, opponentID);
                        int val;
//                      val = MaxValue(sPrime, alpha, beta, depth+1);
                        if (transTbl.containsKey(sPrime)) {
                                val = transTbl.get(sPrime);
                                hitCounter++;
                        } else {
                                val = MaxValue(sPrime, alpha, beta, depth + 1);
                                if (depth < transTblCutoffDepth && (val == winSymbol || val == lossSymbol) ) transTbl.put(sPrime, val);
                        }
                        
                        if (returnTwice){
                                returnTwice = false;
                                return val;
                        }
                        if (val < v){
                                v = val;
                        }
                        if (v <= alpha) return v;
                        if (v < beta) beta = v;
                }
                return v;
        }
        
        private CutoffActions cutoffTest(State state, int depth){
                if (gameLogic.TerminalTest(state) != 0){
                        return CutoffActions.finalState;
                }
                if (depth >= cutoffdepth){
                        return CutoffActions.cutoff;
                }
                return CutoffActions.playOn;
        }

  private int evalVerena(State s){

    int[][] gameBoard = s.gameBoard;
    int[][] gameBoardMirrored = mirror(s.gameBoard);

    SeqVerena seqVerena = new SeqVerena(playerID, opponentID);

    // vertical (columns)
    for (int i = 0; i < gameBoard.length; i++) {
      for (int j = 0; j < gameBoard[0].length; j++) {
        seqVerena.evalPiece(gameBoard[i][j]);
      }
      seqVerena.reset();
    }
    seqVerena.reset();

    // horizontal (rows)
    if (!seqVerena.isFinishedState()) {
      for (int i = 0; i < gameBoard[0].length; i++) {
        for (int j = 0; j < gameBoard.length; j++) {
          seqVerena.evalPiece(gameBoard[j][i]);
        }
        seqVerena.reset();
      }
      seqVerena.reset();
    }

    // diagonal (top left to bottom right)
    diagonalEvalPiece(seqVerena, gameBoard);

    // diagonal (bottom left to top right) - use mirrored gameboard
    diagonalEvalPiece(seqVerena, gameBoardMirrored);

    return seqVerena.getResult();
  }

  private void diagonalEvalPiece(SeqVerena seqVerena, int[][] gameBoard) {
    int diagLength = 1;

    if (!seqVerena.isFinishedState()) {

      // iterate vertically / handle excessRows
      for (int i = 0; i < quadLength + excessRows; i++) {
        for (int j = 0; j < diagLength; j++) {
          seqVerena.evalPiece(gameBoard[j][i - j]);
        }
        seqVerena.reset();

        if (diagLength < quadLength) {
          diagLength++;
        }
      }
      seqVerena.reset();

      // iterate horizontally / handle excessCols
      diagLength = quadLength;
      for (int i = 1; i < quadLength + excessCols; i++) {
        for (int j = 0; j < diagLength; j++) {
          seqVerena.evalPiece(gameBoard[j + i][quadLength - 1 - j]);
        }
        seqVerena.reset();

        // last triangle where diagLength is reducing to 1
        if (i >= gameBoard[0].length - quadLength) {
          diagLength--;
        }
      }
      seqVerena.reset();
    }
  }

  private int[][] mirror(int[][] gameBoard) {
    int[][] ret = new int[gameBoard.length][gameBoard[0].length];

    for (int i = 0; i < gameBoard.length; i++) {
      for (int j = 0; j < gameBoard[0].length; j++) {
        ret[i][j] = gameBoard[gameBoard.length - 1 - i][j];
      }
    }

    return ret;
  }

  private int evalMonteCarloVerena(State s) {
    int randomCol;
    int wins = 0;
    int currentPlayer = opponentID;

    for (int i = 0; i < runs; i++) {
      currentPlayer = opponentID;
      State clone = new State(s);

      while (gameLogic.TerminalTest(clone) == 0) {
        randomCol = random.nextInt(clone.openCols.size());
        clone.insertCoin(clone.openCols.get(randomCol), currentPlayer);
        currentPlayer = (currentPlayer == opponentID) ?  playerID : opponentID ;
      }

      if (gameLogic.TerminalTest(clone) == playerID) {
        wins++;
      } else if (gameLogic.TerminalTest(clone) == opponentID) {
        wins--;
      }
    }

    return wins;
  }


    private int evalFabian(State s) {
        int sumOwn = 0, sumOpponent = 0;

        //Rows
        for (int[] rows : s.gameBoard) {
            seq.push(rows);
            sumOwn += seq.eval(playerID);
            if (sumOwn == Integer.MAX_VALUE) {
                return sumOwn;
            }

            sumOpponent += seq.eval(opponentID);
            seq.flush();
        }

        //Columns
        for (int col = 0; col < s.gameBoard[0].length; col++) {
            for (int[] row : s.gameBoard) {
                seq.push(row[col]);
            }
            sumOwn += seq.eval(playerID);
            if (sumOwn == Integer.MAX_VALUE) {
                return sumOwn;
            }
            sumOpponent += seq.eval(opponentID);
            seq.flush();
        }

        //Diagonals right/up
        for (int k = 3; k < s.gameBoard.length + s.gameBoard[0].length - 4; k++) {
            for (int j = 0; j <= k; j++) {
                int i = k - j;
                if (i < s.gameBoard.length && j < s.gameBoard[i].length) {
                    seq.push(s.gameBoard[i][j]);
                }
            }

            sumOwn += seq.eval(playerID);
            if (sumOwn == Integer.MAX_VALUE) {
                return sumOwn;
            }
            sumOpponent += seq.eval(opponentID);
            seq.flush();
        }

        //Diagonals right/down
        for (int k = 3; k < s.gameBoard.length + s.gameBoard[0].length - 4; k++) {
            for (int j = 0; j <= k; j++) {
                int i = k - j;
                if (i < s.gameBoard.length && j < s.gameBoard[i].length) {
                    seq.push(s.gameBoard[i][s.gameBoard[i].length - j - 1]);
                }
            }

            sumOwn += seq.eval(playerID);
            if (sumOwn == Integer.MAX_VALUE) {
                return sumOwn;
            }
            sumOpponent += seq.eval(opponentID);
            seq.flush();
        }

        return sumOwn - sumOpponent;
    }

    private int evalMonteCarloFabian(State s) {
        int wins = 0;

        for (int run = 0; run < runs; run++) {
            State simState = new State(s);

            int result;
            int player = playerID;
            while ((result = gameLogic.TerminalTest(simState)) == 0) {
                int colNumber = random.nextInt(simState.openCols.size());
                simState.insertCoin(simState.openCols.get(colNumber), player);
                player = player == playerID ? opponentID : playerID;
            }
            if (result == playerID) {
                wins++;
            } else if (result == opponentID) {
                wins--;
            }

        }

        return wins;
    }

    private class SeqFabian {

        List<Integer> sequence = new ArrayList<Integer>(7);

        public void push(int field) {
            sequence.add(field);
        }

        public void push(int... fields) {
            for (int i : fields) {
                sequence.add(i);
            }
        }

        public void flush() {
            sequence.clear();
        }

        public int eval(int playerId) {
            int sum = 0;
            boolean open = false;
            int length = 0;

            for (Integer field : sequence) {
                if (field == 0) {
                    sum = weigth(length, sum);
                    open = true;
                    length = 0;
                } else if (field == playerId) {
                    length++;
                } else {
                    length = 0;
                    open = false;
                }
            }
            if (open) {
                sum = weigth(length, sum);
            }

            return sum;
        }


        public int weigth(int length, int sum) {
            switch (length) {
                case 4:
                    //Should not be accessed
                    sum = Integer.MAX_VALUE;
                    break;
                case 3:
                    sum += 3;
                    break;
                case 2:
                    sum += 1;
                    break;
                default:
                    break;
            }
            return sum;
        }
    }
}
