public class StefsMCEvaluator implements Evaluator {
	public int nSims = 100;
	int pid, oid, turn;
	GameLogic gl;

	public static final int WIN = 1;
	public static final int DRAW = 0;
	public static final int LOSS = -1;

	public StefsMCEvaluator(GameLogic gl) {
		this.gl = gl;
		this.pid = gl.getPlayerID();
		this.oid = -gl.getPlayerID(); // NEGAMAX!
		turn = pid;
	}

	@Override
	public int eval(State s) {

		System.out
				.println("I'm the mighty evaluator and evaluate forever! Muhahahaa");

		int[] winCounter = new int[s.gameBoard.length];
		int randomMove;
		boolean ended;
		State tmp;
		for (int run = 0; run < nSims; run++) {
			tmp = new State(s);
			ended = false;

			do {
				randomMove = (int) (Math.random() * s.gameBoard.length);
				tmp.insertCoin(randomMove, turn);

				turn = turn == pid ? oid : pid;
				int tt = gl.TerminalTest(tmp);

				if (tt == 0) {
				}
				if (tt == 3) {
					winCounter[randomMove] += DRAW;
					ended = true;
				}
				if (tt == oid) {
					winCounter[randomMove] += LOSS;
					ended = true;
				}
				if (tt == pid) {
					winCounter[randomMove] += WIN;
					ended = true;
				}

			} while (!ended);
		}
		int ret = maxIndex(winCounter);
		System.out.println("evaluation: " + ret);
		return ret;
	}

	public int maxIndex(int[] arr) {

		int maxi = 0;
		if (arr.length < 1)
			return -1;
		if (arr.length < 2)
			return maxi;
		for (int i = 1; i < arr.length; i++) {
			if (arr[maxi] < arr[i]) {
				maxi = i;
			}
		}
		return maxi;

	}
}
