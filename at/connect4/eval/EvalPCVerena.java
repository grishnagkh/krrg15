package at.connect4.eval;

import at.connect4.game.GameLogic;
import at.connect4.game.State;

public class EvalPCVerena extends PieceCounter {

    private int quadLength;
    private int excessRows;
    private int excessCols;

    public EvalPCVerena(GameLogic gl) {
        super(gl);
        quadLength = Math.min(gl.board.gameBoard.length, gl.board.gameBoard[0].length);
        excessRows = Math.max(0, gl.board.gameBoard[0].length - gl.board.gameBoard.length);
        excessCols = Math.max(0, gl.board.gameBoard.length - gl.board.gameBoard[0].length);

    }

    @Override
    public int eval(State s) {

        int[][] gameBoard = s.gameBoard;
        int[][] gameBoardMirrored = mirror(s.gameBoard);

        SeqVerena seqVerena = new SeqVerena(pid, oid);

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
}
