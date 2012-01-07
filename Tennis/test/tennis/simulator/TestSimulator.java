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

		final double percentageWon = simulator.simulate(0.6, 0.4, 200000).proportionMatchesWon();
		assertThat(percentageWon, lessThan(0.51));
		assertThat(percentageWon, greaterThan(0.49));
	}

	@Test
	public void testFiveSetMatch()
	{
		final double mwp = round(simulator.simulate(0.55, 0.55, 200000).proportionMatchesWon());
		assertThat(mwp, equalTo(0.953));
		System.out.println(mwp);
	}

	@Test
	public void testOutcomes()
	{
		final int runs = 1000;
		final SimulationOutcomes outcomes = simulator.simulate(0.67, 0.38, runs);
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
		final MatchState initialState = new MatchState(0, 0, new SetState(2, 3), new GameState(false), 1);
		final SimulationOutcomes outcomes = new Simulator().simulate(0.67, 0.38, initialState, true, 200000);
		System.out.println(outcomes.proportionMatchesWon());
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.###").format(value));
	}
}
