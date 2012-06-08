package tennis.simulator;

import java.io.IOException;

import org.junit.Test;

public class TestSimulatorWRHypExpRecord
{
	@Test
	public void generateFullMatch() throws IOException
	{
		final double chance = 0.00019;
		final double lambda = 10.0;
		final double decay = 0.85;
		final SimulatorWR simulator = new SimulatorWRHypExpRecord(chance, lambda, decay, true);
		simulator.simulate(0.60, 0.60, 1).minPrint("A", "B");
	}
}
