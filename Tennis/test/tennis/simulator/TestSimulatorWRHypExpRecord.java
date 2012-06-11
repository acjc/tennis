package tennis.simulator;

import java.io.IOException;

import org.junit.Test;

public class TestSimulatorWRHypExpRecord
{
	public static boolean GO = true;

	@Test
	public void generateFullMatch() throws IOException
	{
		final double chance = 0.000115;
		final double lambda = 10.0;
		final double decay = 0.95;
		while(GO)
		{
			final SimulatorWR simulator = new SimulatorWRHyperExpRecord(chance, lambda, decay, true);
			simulator.simulate(0.60, 0.60, 1).minPrint("A", "B");
		}
	}
}
