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
	public void testSimulatorSpeed() throws IOException
	{
		System.out.println(new Simulator().simulate(0.5, 0.5, 1000000).getSimulationTime());
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
	public void validateHuang() throws IOException
	{
		// Huang results
		double mwp = round(simulator.simulate(0.58, 0.38, new MatchState(1, 0, new SetState(5, 1), new GameState(true), 2), true, 500000).proportionMatchesWon());
		assertThat(mwp, equalTo(0.979)); // 0.441

		mwp = round(simulator.simulate(0.56, 0.44, new MatchState(1, 1, new SetState(4, 3), new GameState(true), 2), true, 500000).proportionMatchesWon());
		assertThat(mwp, equalTo(0.750)); // 0.841
	}


	@Test
	public void validateLiu() throws IOException
	{
		// Liu results
		double mwp = round(simulator.simulate(0.55, 0.55, 500000).proportionMatchesWon());
		assertThat(mwp, equalTo(0.953));

		mwp = round(simulator.simulate(0.51, 0.51, 500000).proportionMatchesWon());
		assertThat(mwp, equalTo(0.632));
	}

	@Test
	public void validateOMalley() throws IOException
	{
		// Results agree with me
		SimulationOutcomes outcomes = new Simulator().simulate(0.67, 0.38, new MatchState(0, 0, new SetState(2, 3), new GameState(false), 1), true, 500000);
		assertThat(round(outcomes.proportionMatchesWon()), equalTo(0.295)); // 0.295

		outcomes = new Simulator().simulate(0.62, 0.33, new MatchState(0, 0, new SetState(1, 3), new GameState(true), 1), true, 500000);
		assertThat(round(outcomes.proportionMatchesWon()), equalTo(0.092)); // 0.092

		outcomes = new Simulator().simulate(0.62, 0.33, new MatchState(0, 0, new SetState(0, 3), new GameState(true), 1), true, 500000);
		assertThat(round(outcomes.proportionMatchesWon()), equalTo(0.074)); // 0.074
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

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.###").format(value));
	}
}
