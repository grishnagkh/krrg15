public class SeqVerena {
  private int playerID;
  private int opponentID;
  private int currentCount;
  private int countingForPlayer;

  public SeqVerena(int playerID, int opponentID) {
    this.playerID = playerID;
    this.opponentID = opponentID;
    this.currentCount = 0;
  }

  public int evalPiece(int piece) {
    int ret = 0;

    // no piece
    if (piece == 0) {

      // reset current count
      if (currentCount > 1) {
        ret = currentCount;
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
          ret = Integer.MAX_VALUE;
        }
      } else {

        // add current count to eval, set countingForPlayer == opponentID and reset currentCount
        if (currentCount > 1) {
          ret = currentCount;
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
          ret = Integer.MIN_VALUE;
        }
      } else {

        // add current count to eval, set countingForPlayer == playerID and reset currentCount
        if (currentCount > 1) {
          ret = -1 * currentCount;
        }

        currentCount = 1;
        countingForPlayer = opponentID;
      }
    }

    return ret;
  }

  private int evalLastPiece() {
    if (currentCount > 1) {
      if (countingForPlayer == playerID) {
        return currentCount;
      } else {
        return -1 * currentCount;
      }
    }
    return 0;
  }

  public int reset() {
    int ret = evalLastPiece();
    currentCount = 0;
    return ret;
  }
}
