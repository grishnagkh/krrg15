public class PieceCounter implements Evaluator{

	GameLogic gl;

	public PieceCounter(GameLogic gl){
		this.gl = gl;
	}

	@Override
	public int eval(State s){
		return -1;
	}
}
