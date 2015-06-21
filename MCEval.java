import java.util.Random;

public class MCEval implements Evaluator{

	Random random;
	GameLogic gl;
	int runs = 50;

	int pid, oid; //player id, opponent id
	int turn; // indicating which player's turn it is

	public MCEval(GameLogic gl){
		random = new Random();
		this.gl = gl;
		pid = gl.getPlayerID();
		oid = -pid; //Negamax only ;)
        turn = pid;
    }

    @Override
    public int eval(State s) {
        int randomCol;
        int wins = 0;

        for (int i = 0; i < runs; i++) {

            State clone = new State(s);

            while (gl.TerminalTest(clone) == 0) {
                randomCol = random.nextInt(clone.openCols.size());
                clone.insertCoin(clone.openCols.get(randomCol), turn);
                turn = (turn == pid) ? oid : pid;
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
