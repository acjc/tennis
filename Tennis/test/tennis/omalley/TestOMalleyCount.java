package tennis.omalley;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.text.DecimalFormat;

import org.junit.Test;

import tennis.simulator.MatchState;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.Simulator;

public class TestOMalleyCount
{
	@Test
	public void testGameMwp()
	{
		assertThat(OMalleyCount.gameInProgressCount(0.55, 0.01).mwp, equalTo(OMalley.gameInProgress(0.55)));
	}

	@Test
	public void testSetMwp()
	{
		assertThat(OMalleyCount.setInProgressCount(0.55, 0.46, 0.01, true).mwp, equalTo(OMalley.setInProgress(0.55, 0.46, true)));
	}

	@Test
	public void testMatchMwp()
	{
		assertThat(round3(OMalleyCount.matchInProgressCount(0.55, 0.46, 0.01, Math.random() < 0.5 ? true : false, 3).mwp),
				   equalTo(round3(OMalley.matchInProgress(0.55, 0.46, 3))));
	}

	final double p = 0.55;
	final double q = 0.46;
	double r = 0.001;

	@Test
	public void testPoints() throws IOException
	{
		final SimulationOutcomes outcome = new Simulator().simulate(p, q, new MatchState(), false, 100000);
		System.out.println("Average Tiebreak Points: " + outcome.avgTiebreakPoints());
		System.out.println("Average Points Post Deuce: " + outcome.avgPointsPostDeuce());

		System.out.println("Points in a game: " + OMalleyCount.gameInProgressCount(p, r).points);
		System.out.println("Points in a set: " + OMalleyCount.setInProgressCount(p, q, r, true).points);

		System.out.println();
	}

	@Test
	public void testRecursionLevels()
	{
		final double gameLevels = OMalleyCount.gameInProgressCount(p, r).levels;
		System.out.println("Recursion levels in a game: " + gameLevels);
		assertThat(round3(gameLevels), equalTo(5.480));

		final double setLevels = OMalleyCount.setInProgressCount(p, q, r, true).levels;
		System.out.println("Recursion levels in a set: " + setLevels);
		assertThat(round3(setLevels), equalTo(9.715));

		final double matchLevels = OMalleyCount.matchInProgressCount(p, q, r, (Math.random() < 0.5) ? true : false, 3).levels;
		System.out.println("Recursion levels in a match: " + matchLevels);
		assertThat(round1(matchLevels), equalTo(40.0));

		System.out.println();
	}

	@Test
	public void testRetirementRisk()
	{
		final double gameRisk = OMalleyCount.gameInProgressCount(p, r).risk;
		System.out.println("Retirement risk in a game: " + gameRisk);
		assertThat(round3(gameRisk), equalTo(0.005));

		final double setLevels = OMalleyCount.setInProgressCount(p, q, r, true).risk;
		System.out.println("Retirement risk in a set: " + setLevels);
		assertThat(round3(setLevels), equalTo(0.053));

		final double matchLevels = OMalleyCount.matchInProgressCount(p, q, r, (Math.random() < 0.5) ? true : false, 3).risk;
		System.out.println("Retirement risk in a match: " + matchLevels);
		assertThat(round3(matchLevels), equalTo(0.216));

		System.out.println();
	}

	private double round1(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.#").format(value));
	}

	private double round3(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.###").format(value));
	}
}
