import java.util.HashMap;

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
  private Seq seq;
  private int quadLength;
  private int excessRows;
  private int excessCols;

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
      else if (action == CutoffActions.cutoff) return eval(s);
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
      else if (action == CutoffActions.cutoff) return eval(s);
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
        val = MaxValue(sPrime, alpha, beta, depth+1);
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

  private int eval(State s){
    int eval = 0;

    int[][] gameBoard = s.gameBoard;
    int[][] gameBoardMirrored = mirror(s.gameBoard);

    seq = new Seq(playerID, opponentID);

    // vertical (columns)
    for (int i = 0; i < gameBoard.length; i++) {
      for (int j = 0; j < gameBoard[0].length; j++) {
        int seqResult = seq.evalPiece(gameBoard[i][j]);

        // terminal test
        if (seqResult == Integer.MAX_VALUE || seqResult == Integer.MIN_VALUE) {
          return seqResult;
        }
        eval += seqResult;
      }
      eval += seq.reset();
    }
    eval += seq.reset();

    // horizontal (rows)
    for (int i = 0; i < gameBoard[0].length; i++) {
      for (int j = 0; j < gameBoard.length; j++) {
        int seqResult = seq.evalPiece(gameBoard[j][i]);

        // terminal test
        if (seqResult == Integer.MAX_VALUE || seqResult == Integer.MIN_VALUE) {
          return seqResult;
        }
        eval += seqResult;
      }
      eval += seq.reset();
    }
    eval += seq.reset();

    // diagonal (top left to bottom right)

    // iterate vertically / handle excessRows
    int diagLength = 1;
    for (int i = 0; i < quadLength + excessRows; i++) {
      for (int j = 0; j < diagLength; j++) {
        int seqResult = seq.evalPiece(gameBoard[j][i-j]);

        // terminal test
        if (seqResult == Integer.MAX_VALUE || seqResult == Integer.MIN_VALUE) {
          return seqResult;
        }
        eval += seqResult;
      }
      eval += seq.reset();

      if (diagLength < quadLength) {
        diagLength++;
      }
    }
    eval += seq.reset();

    // iterate horizontally / handle excessCols
    diagLength = quadLength;
    for (int i = 1; i < quadLength + excessCols; i++) {
      for (int j = 0; j < diagLength; j++) {
        int seqResult = seq.evalPiece(gameBoard[j + i][quadLength - 1 - j]);

        // terminal test
        if (seqResult == Integer.MAX_VALUE || seqResult == Integer.MIN_VALUE) {
          return seqResult;
        }
        eval += seqResult;
      }
      eval += seq.reset();

      // last triangle where diagLength is reducing to 1
      if (i >= gameBoard[0].length - quadLength) {
        diagLength--;
      }
    }
    eval += seq.reset();

    // diagonal (bottom left to top right) - use mirrored gameboard

    // iterate vertically  / handle excessRows
    diagLength = 1;
    for (int i = 0; i < quadLength + excessRows; i++) {
      for (int j = 0; j < diagLength; j++) {
        int seqResult = seq.evalPiece(gameBoardMirrored[j][i-j]);

        // terminal test
        if (seqResult == Integer.MAX_VALUE || seqResult == Integer.MIN_VALUE) {
          return seqResult;
        }
        eval += seqResult;
      }
      eval += seq.reset();

      if (diagLength < quadLength) {
        diagLength++;
      }
    }
    eval += seq.reset();

    // iterate horizontally / handle excessCols
    diagLength = quadLength;
    for (int i = 1; i < quadLength + excessCols; i++) {
      for (int j = 0; j < diagLength; j++) {
        int seqResult = seq.evalPiece(gameBoardMirrored[j + i][quadLength - 1 - j]);

        // terminal test
        if (seqResult == Integer.MAX_VALUE || seqResult == Integer.MIN_VALUE) {
          return seqResult;
        }
        eval += seqResult;
      }
      eval += seq.reset();

      // last triangle where diagLength is reducing to 1
      if (i >= gameBoardMirrored[0].length - quadLength) {
        diagLength--;
      }
    }
    eval += seq.reset();

    return eval;
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
}
