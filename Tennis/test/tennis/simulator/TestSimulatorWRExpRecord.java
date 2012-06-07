package tennis.simulator;

import java.io.IOException;

import org.junit.Test;

public class TestSimulatorWRExpRecord
{
	@Test
	public void generateFullMatch() throws IOException
	{
		final double lambda = 4150;
//		final double lambda = 55000;
		final double decay = 0.75;
		final SimulatorWR simulator = new SimulatorWRExpRecord(lambda, decay, false);
		simulator.simulate(0.60, 0.60, 1).minPrint("A", "B");
	}
}
