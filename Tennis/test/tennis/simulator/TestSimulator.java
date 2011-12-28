package tennis.simulator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

import org.hamcrest.Matchers;
import org.junit.Test;

public class TestSimulator
{
	private final Simulator simulator = new Simulator();

	@Test
	public void testSimulator()
	{
		assertThat(simulator.simulateFiveSetMatch(0.6, 0.4, 100000), lessThan(0.51));
		assertThat(simulator.simulateFiveSetMatch(0.6, 0.4, 100000), Matchers.greaterThan(0.49));
	}
}
