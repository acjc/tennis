package tennis.simulator;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

public class TestSimulatorWRExpRecord
{
	@Test
	public void generateFullMatch() throws IOException
	{
		final SimulatorWR simulator = new SimulatorWRExpRecord(-1, 0.85, true);
		simulator.simulate(0.60, 0.60, 1).minPrint("A", "B");
	}

	@Ignore
	@Test
	public void testScenarioOnePlayer() throws IOException
	{
		final SimulatorWR simulatorWR = new SimulatorWRExpRecord(53.0, 1000.0, 0.85, true);
		simulatorWR.simulate(0.63, 0.61, new MatchState(1, 0, new SetState(1, 2), new GameState(true), 3), true, 200000).minPrint("A", "B");
	}
}
