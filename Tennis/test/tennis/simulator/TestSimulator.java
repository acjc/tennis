package tennis.simulator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.text.DecimalFormat;

import org.junit.Test;

public class TestSimulator
{
	private final Simulator simulator = new Simulator();

	@Test
	public void testPrediction() throws IOException
	{
		// One run triggers injury model
		new Simulator().simulate(0.6, 0.4, 1).targetOneBallPredictionChart();
	}

	@Test
	public void testPercentageWon() throws IOException
	{
		// Basic match assertions
		assertThat(simulator.simulate(1, 0.3, 100).proportionMatchesWon(), equalTo(1.0));
		assertThat(simulator.simulate(0, 0.3, 100).proportionMatchesWon(), equalTo(0.0));

		final double percentageWon = simulator.simulate(0.6, 0.4, 200000).proportionMatchesWon();
		assertThat(percentageWon, lessThan(0.51));
		assertThat(percentageWon, greaterThan(0.49));
	}

	@Test
	public void testThreeSetMatch() throws IOException
	{
		// Huang results
		double mwp = round(simulator.simulate(0.60, 0.43, new MatchState(1, 1, new SetState(4, 3), new GameState(true), 2), true, 400000).proportionMatchesWon());
		assertThat(mwp, equalTo(0.891));
		System.out.println(mwp);

		mwp = round(simulator.simulate(0.65, 0.46, new MatchState(1, 1, new SetState(4, 3), new GameState(true), 2), true, 400000).proportionMatchesWon());
		assertThat(mwp, equalTo(0.968));
		System.out.println(mwp);
	}


	@Test
	public void testFiveSetMatch() throws IOException
	{
		// Liu results
		double mwp = round(simulator.simulate(0.55, 0.55, 400000).proportionMatchesWon());
		assertThat(mwp, equalTo(0.953));
		System.out.println(mwp);

		mwp = round(simulator.simulate(0.51, 0.51, 400000).proportionMatchesWon());
		assertThat(mwp, equalTo(0.632));
		System.out.println(mwp);
	}

	@Test
	public void testOutcomes() throws IOException
	{
		// Final score probabilities sum to 1
		final int runs = 1000;
		final SimulationOutcomes outcomes = simulator.simulate(0.67, 0.38, runs);
		double total = 0;
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				total += outcomes.percentageWithMatchScore(i, j);
			}
		}
		assertThat(round(total), equalTo(100.0));
	}

	@Test
	public void validateOMalley() throws IOException
	{
		// Results agree with me
		MatchState initialState = new MatchState(0, 0, new SetState(2, 3), new GameState(false), 1);
		SimulationOutcomes outcomes = new Simulator().simulate(0.67, 0.38, initialState, true, 200000);
		System.out.println(outcomes.proportionMatchesWon());

		initialState = new MatchState(0, 0, new SetState(1, 3), new GameState(true), 1);
		outcomes = new Simulator().simulate(0.62, 0.33, initialState, true, 200000);
		System.out.println(outcomes.proportionMatchesWon());

		initialState = new MatchState(0, 0, new SetState(0, 3), new GameState(true), 1);
		outcomes = new Simulator().simulate(0.62, 0.33, initialState, true, 200000);
		System.out.println(outcomes.proportionMatchesWon());
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.###").format(value));
	}
}
