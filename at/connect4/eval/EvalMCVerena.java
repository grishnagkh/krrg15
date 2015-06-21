package at.connect4.eval;

import at.connect4.game.GameLogic;
import at.connect4.game.State;

public class EvalMCVerena extends MCEval {

    public EvalMCVerena(GameLogic gl) {
        super(gl);
    }

    @Override
    public int eval(State s) {
        int randomCol;
        int wins = 0;
        int currentPlayer = oid;

        for (int i = 0; i < runs; i++) {
            currentPlayer = oid;
            State clone = new State(s);

            while (gl.TerminalTest(clone) == 0) {
                randomCol = random.nextInt(clone.openCols.size());
                clone.insertCoin(clone.openCols.get(randomCol), currentPlayer);
                currentPlayer = (currentPlayer == oid) ? pid : oid;
            }

            if (gl.TerminalTest(clone) == pid) {
                wins++;
            } else if (gl.TerminalTest(clone) == oid) {
                wins--;
            }
        }

        return wins;
    }

}