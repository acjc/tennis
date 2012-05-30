package tennis.simulator;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

public class TestSimulatorWR
{
	@Ignore
	@Test
	public void testFindAlpha() throws IOException
	{
		final double baseAlpha = 154.62;
		for(double i = 0; i <= 0.1; i += 0.01)
		{
			System.out.println(baseAlpha + i);
			final SimulatorWR simulator = new SimulatorWR(baseAlpha + i, 0.85, true);
			simulator.simulate(0.6, 0.6, 25000).minPrint("A", "B");
			System.out.println();
		}
	}

	@Test
	public void testFullMatchBothPlayers() throws IOException
	{
		final double baseAlpha = 154.62;
		final SimulatorWR simulator = new SimulatorWR(baseAlpha, 0.85, true);
		simulator.simulate(0.60, 0.60, 100000).minPrint("A", "B");
	}

	@Test
	public void testScenarioOnePlayer() throws IOException
	{
		final SimulatorWR simulatorWR = new SimulatorWR(50.0, 1000.0, 0.85, true);
		simulatorWR.simulate(0.63, 0.61, new MatchState(1, 0, new SetState(1, 2), new GameState(true), 3), true, 100000).minPrint("A", "B");
	}
}
