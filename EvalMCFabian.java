public class EvalMCFabian extends MCEval {

    public EvalMCFabian(GameLogic gl) {
        super(gl);
    }

    @Override
    public int eval(State s) {
        int wins = 0;

        for (int run = 0; run < runs; run++) {
            State simState = new State(s);

            int result;
            int player = oid;
            while ((result = gl.TerminalTest(simState)) == 0) {
                int colNumber = random.nextInt(simState.openCols.size());
                simState.insertCoin(simState.openCols.get(colNumber), player);
                player = player == oid ? oid : pid;
            }
            if (result == pid) {
                wins++;
            } else if (result == oid) {
                wins--;
            }

        }

        return wins;
    }

}