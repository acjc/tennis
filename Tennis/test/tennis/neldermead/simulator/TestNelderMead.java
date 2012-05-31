package tennis.neldermead.simulator;

import tennis.omalley.CurrentMatchScore;
import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalley;
import tennis.simulator.GameState;
import tennis.simulator.MatchState;
import tennis.simulator.SetState;

public class TestNelderMead
{
	protected double pa = 0.63;
	protected double pb = 0.61;
	protected final double mwp = OMalley.matchInProgress(pa, pb, new CurrentMatchScore(1, 0), new CurrentSetScore(1, 2), true, 3);
	protected MatchState initialState = new MatchState(1, 0, new SetState(1, 2), new GameState(true), 3);
}
