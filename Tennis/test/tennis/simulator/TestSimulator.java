package tennis.simulator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.text.DecimalFormat;

import org.junit.Test;

public class TestSimulator
{
	private final Simulator simulator = new Simulator();

	@Test
	public void testSimulator()
	{
		assertThat(simulator.simulate(1, 0.3, 100), equalTo(1.0));
		assertThat(simulator.simulate(0, 0.3, 100), equalTo(0.0));

		assertThat(round(simulator.simulate(0.6, 0.4, 1000000)), equalTo(0.5));
	}

	@Test
	public void testFiveSetMatch()
	{
		assertThat(round(simulator.simulate(0.55, 0.55, 1000000)), equalTo(0.953));
	}

	@Test
	public void testMatchesInProgress()
	{
		final Simulator simulator = new Simulator(new MatchState(0, 0, 2, 3, 0, 0, false, 1), true);
		System.out.println(simulator.simulate(0.67, 0.38, 1000000));
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.###").format(value));
	}
}
