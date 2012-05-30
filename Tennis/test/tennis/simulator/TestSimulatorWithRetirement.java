package tennis.simulator;

import java.io.IOException;
import java.text.DecimalFormat;

import org.junit.Ignore;
import org.junit.Test;

public class TestSimulatorWithRetirement
{
	@Ignore
	@Test
	public void testFindAlpha() throws IOException
	{
		final double baseAlpha = 154.13;
		for(double i = 0; i <= 0.01; i += 0.001)
		{
			System.out.println(baseAlpha + i);
			final SimulatorWithRetirement simulator = new SimulatorWithRetirement(baseAlpha + i, 0.85);
			simulator.simulate(0.6, 0.6, 10000).minPrint("A", "B");
			System.out.println();
		}
	}

	@Test
	public void testSimulatorWithRetirement() throws IOException
	{
		final double baseAlpha = 154.133;
		final SimulatorWithRetirement simulator = new SimulatorWithRetirement(baseAlpha, 0.85);
		simulator.simulate(0.61, 0.64, 200000).minPrint("A", "B");
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.###").format(value));
	}
}
