package tennis.simulator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.text.DecimalFormat;

import org.junit.Test;

public class TestSimulator
{
	private final Simulator simulator = new Simulator();

	@Test
	public void testPercentageWon()
	{
		assertThat(simulator.simulate(1, 0.3, 100).proportionMatchesWon(), equalTo(1.0));
		assertThat(simulator.simulate(0, 0.3, 100).proportionMatchesWon(), equalTo(0.0));

		final double percentageWon = simulator.simulate(0.6, 0.4, 1000000).proportionMatchesWon();
		assertThat(percentageWon, lessThan(0.51));
		assertThat(percentageWon, greaterThan(0.49));
	}

	@Test
	public void testFiveSetMatch()
	{
		assertThat(round(simulator.simulate(0.55, 0.55, 1000000).proportionMatchesWon()), equalTo(0.953));
	}

	@Test
	public void testOutcomes()
	{
		final int runs = 1000;
		final Outcomes outcomes = simulator.simulate(0.67, 0.38, runs);
		double total = 0;
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				total += outcomes.percentageWithScore(i, j);
			}
		}
		assertThat(round(total), equalTo(100.0));
	}

	@Test
	public void testMatchesInProgress()
	{
		final Simulator simulator = new Simulator(new MatchState(0, 0, new SetState(2, 3), new GameState(false), 1), true);
		System.out.println(simulator.simulate(0.67, 0.38, 1000000).proportionMatchesWon());
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.###").format(value));
	}
}
