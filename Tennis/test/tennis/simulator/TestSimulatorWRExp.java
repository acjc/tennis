package tennis.simulator;

import org.junit.Ignore;
import org.junit.Test;

public class TestSimulatorWRExp
{
	@Ignore
	@Test
	public void testFindLambda()
	{
		final double lambda = 0.1;
		for(double i = 0; i <= 5; i += 0.1)
		{
			System.out.println(lambda + i);
			final SimulatorWR simulator = new SimulatorWRExp(lambda + i, 0.85, true);
			simulator.simulate(0.6, 0.6, 10000).minPrint("A", "B");
			System.out.println();
		}
	}

	@Ignore
	@Test
	public void testFullMatchBothPlayersSmallRisk()
	{
		final double lambda = 55000;
		final double decay = 0.75;
		final SimulatorWR simulator = new SimulatorWRExp(lambda, decay, true);
		simulator.simulate(0.60, 0.60, 100000).minPrint("A", "B");
	}

	@Test
	public void testFullMatchBothPlayersLargeRisk()
	{
		final double lambda = 4150;
		final double decay = 0.75;
		final SimulatorWR simulator = new SimulatorWRExp(lambda, decay, true);
		simulator.simulate(0.60, 0.60, 20000).minPrint("A", "B");
	}

	@Ignore
	@Test
	public void testScenarioOnePlayer()
	{
		final SimulatorWR simulatorWR = new SimulatorWRExp(53.0, 1000.0, 0.85, true);
		simulatorWR.simulate(0.63, 0.61, new MatchState(1, 0, new SetState(1, 2), new GameState(true), 3), true, 200000).minPrint("A", "B");
	}
}
