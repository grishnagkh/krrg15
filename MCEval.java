public class MCEval implements Evaluator{

	GameLogic gl;

	public MCEval(GameLogic gl){
		this.gl = gl;
	}

	@Override
	public int eval(State s){
		return -1;
	}
}
