package tennis.simulator;

import org.junit.Ignore;
import org.junit.Test;

public class TestSimulatorWRPareto
{
	@Ignore
	@Test
	public void testFindAlpha()
	{
//		final double baseAlpha = 154.62;
		final double baseAlpha = 200000.0;
		for(double i = 0; i <= 10000; i += 500)
		{
			System.out.println(baseAlpha + i);
			final SimulatorWR simulator = new SimulatorWRPareto(baseAlpha + i, 0.85, true);
			simulator.simulate(0.6, 0.6, 10000).minPrint("A", "B");
			System.out.println();
		}
	}

	@Test
	public void testFullMatchBothPlayers()
	{
		final double baseAlpha = 53000;
		final SimulatorWR simulator = new SimulatorWRPareto(baseAlpha, 0.75, true);
		simulator.simulate(0.60, 0.60, 20000).minPrint("A", "B");
	}

	@Ignore
	@Test
	public void testScenarioOnePlayer()
	{
		final SimulatorWR simulatorWR = new SimulatorWRPareto(53.0, -1, 0.85, true);
		simulatorWR.simulate(0.63, 0.61, new MatchState(1, 0, new SetState(1, 2), new GameState(true), 3), true, 100000).minPrint("A", "B");
	}
}
