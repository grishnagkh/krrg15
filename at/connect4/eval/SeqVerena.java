package at.connect4.eval;

public class SeqVerena {
  private int playerID;
  private int opponentID;
  private int currentCount;
  private int countingForPlayer;
  private int result;

  public SeqVerena(int playerID, int opponentID) {
    this.playerID = playerID;
    this.opponentID = opponentID;
    this.currentCount = 0;
  }

  public void evalPiece(int piece) {
    int val = 0;

    // no piece
    if (piece == 0) {

      // reset current count
      if (currentCount > 1) {
        val = currentCount;
      }

      currentCount = 0;
      countingForPlayer = 0;
    }

    // max piece
    if (piece == playerID) {
      if (countingForPlayer == playerID) {
        currentCount++;

        // for terminal test
        if (currentCount >= 4) {
          val = Integer.MAX_VALUE;
        }
      } else {

        // add current count to at.connect4.eval, set countingForPlayer == opponentID and reset currentCount
        if (currentCount > 1) {
          val = currentCount;
        }

        currentCount = 1;
        countingForPlayer = playerID;
      }
    }

    // min piece
    else if (piece == opponentID) {
      if (countingForPlayer == opponentID) {
        currentCount++;

        // for terminal test
        if (currentCount >= 4) {
          val = Integer.MIN_VALUE;
        }
      } else {

        // add current count to at.connect4.eval, set countingForPlayer == playerID and reset currentCount
        if (currentCount > 1) {
          val = -1 * currentCount;
        }

        currentCount = 1;
        countingForPlayer = opponentID;
      }
    }

    addToResult(val);
  }

  private void addToResult(int val) {
    // terminal test
    if (!isFinishedState()) {
      if (val != Integer.MAX_VALUE && val != Integer.MIN_VALUE) {
        result += val;
      } else {
        result = val;
      }
    }
  }

  private void evalLastPiece() {
    int val = 0;
    if (currentCount > 1) {
      if (countingForPlayer == playerID) {
        val = currentCount;
      } else {
        val =  -1 * currentCount;
      }
    }
    addToResult(val);
  }

  public void reset() {
    evalLastPiece();
    currentCount = 0;
  }

  public int getResult() {
    return result;
  }

  public boolean isFinishedState() {
    return (result == Integer.MAX_VALUE || result == Integer.MIN_VALUE);
  }
}
