package tennis.simulator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

import org.junit.Test;

public class TestSimulator
{
	@Test
	public void testSimulator()
	{
		final Simulator simulator = new Simulator();

		assertThat(simulator.simulate(1, 0.3, 1000), equalTo(1.0));
		assertThat(simulator.simulate(0, 0.3, 1000), equalTo(0.0));

		assertThat(simulator.simulate(0.6, 0.4, 100000), lessThan(0.51));
		assertThat(simulator.simulate(0.6, 0.4, 100000), greaterThan(0.49));
	}

	@Test
	public void testFiveSetMatch()
	{
		System.out.println(new Simulator().simulate(0.55, 0.55, 1000000));
	}

	@Test
	public void testMatchesInProgress()
	{
		final Simulator simulator = new Simulator(new MatchState(0, 0, 2, 3, 0, 0, false, 1), true);
		System.out.println(simulator.simulate(0.67, 0.38, 1000000));
	}
}
