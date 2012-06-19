package tennis.simulator;

import org.junit.Test;

public class TestSimulatorWRHyperExp
{
	@Test
	public void testFullMatch()
	{
		final double chance = 0.000115;
		final double lambda = 10.0;
		final double decay = 0.95;

//		final double chance = 0.0001908;
//		final double lambda = 2.195;
//		final double decay = 0.5026;

		final SimulatorWR simulator = new SimulatorWRHyperExp(chance, lambda, decay, true);
		final MatchState initialState = new MatchState(0, 0, new SetState(0, 0), new GameState(0, 0, true), 3);
		simulator.simulate(0.6, 0.6, new RetirementRisk(0.002, 0), initialState, true, 10000).minPrint("A", "B");
	}
}
