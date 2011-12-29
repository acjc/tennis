package tennis.simulator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.text.DecimalFormat;

import org.junit.Ignore;
import org.junit.Test;

public class TestSimulator
{
	private final Simulator simulator = new Simulator();

	@Ignore
	@Test
	public void testSimulator()
	{
		assertThat(simulator.simulate(1, 0.3, 100), equalTo(1.0));
		assertThat(simulator.simulate(0, 0.3, 100), equalTo(0.0));

		assertThat(round(simulator.simulate(0.6, 0.4, 1000000)), equalTo(0.5));
	}

	@Ignore
	@Test
	public void testFiveSetMatch()
	{
		assertThat(round(simulator.simulate(0.55, 0.55, 1000000)), equalTo(0.953));
	}

	@Test
	public void testMatchesInProgress()
	{
		final Simulator simulator = new Simulator(new MatchState(0, 0, new SetState(2, 3), new GameState(false), 1), true);
		System.out.println(simulator.simulate(0.67, 0.38, 1000000));
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.###").format(value));
	}
}
