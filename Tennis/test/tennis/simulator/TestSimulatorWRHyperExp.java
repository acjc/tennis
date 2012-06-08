package tennis.simulator;

import org.junit.Test;

public class TestSimulatorWRHyperExp
{
	@Test
	public void testFullMatch()
	{
		final double chance = 0.00019;
		final double lambda = 10.0;
		final double decay = 0.85;

		final SimulatorWR simulator = new SimulatorWRHyperExp(chance, lambda, decay, true);
		simulator.simulate(0.60, 0.60, 20000).minPrint("A", "B");
	}
}
