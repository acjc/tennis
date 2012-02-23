package tennis.simulator;

import java.io.IOException;

public class Simulator
{
	private double runs = 1;
	private SimulationOutcomes outcomes;

	public SimulationOutcomes simulate(final double onServe, final double returnServe, final double runs) throws IOException
	{
		return simulate(onServe, returnServe, new MatchState(), false, runs);
	}

	public SimulationOutcomes simulate(final double onServe, final double returnServe, final int numSetsToWin, final double runs) throws IOException
	{
		return simulate(onServe, returnServe, new MatchState(numSetsToWin), false, runs);
	}

	public SimulationOutcomes simulate(final double onServe, final double returnServe, final MatchState initialState, final boolean scenario, final double runs) throws IOException
	{
		this.runs = runs;
		this.outcomes = new SimulationOutcomes(runs);
		final long startTime = System.currentTimeMillis();
		for (int i = 0; i < runs; i++)
		{
			// When simulating a particular scenario, we want to replicate the starting conditions exactly
			// Otherwise, start a fresh match with a random first server
			final MatchState result = new MatchState(initialState);
			if (!scenario)
			{
				result.coinToss();
			}
			simulateMatch(onServe, returnServe, result);
			outcomes.update(result);
		}
		final long endTime = System.currentTimeMillis();
		outcomes.setSimulationTime((endTime - startTime) / 1000.0);
		return outcomes;
	}

	private MatchState simulateMatch(final double onServe, final double returnServe, final MatchState score) throws IOException
	{
		final double p = onServe;
		final double q = returnServe;

		while (!score.over())
		{
			while (!score.setOver())
			{
				while (!score.gameOver())
				{
					playPoint(p, q, score);
				}
				if (score.tiebreak()) // Assume tiebreaks are always used for now
				{
					playTiebreak(p, q, score);
				}
			}
		}
		return score;
	}

	private void playTiebreak(final double p, final double q, final MatchState score) throws IOException
	{
		// Whomever serves first is the server for this 'game'
		boolean servingNext = score.isServingNext();
		while (!score.tiebreakOver())
		{
			playPoint(p, q, score, servingNext);
			if (score.isOddPoint()) // Service changes every odd point
			{
				servingNext = !servingNext;
			}
		}
	}

	private void playPoint(final double p, final double q, final MatchState score) throws IOException
	{
		playPoint(p, q, score, score.isServingNext());
	}

	private void playPoint(final double p, final double q, final MatchState score, final boolean serving) throws IOException
	{
		final double playPoint = Math.random();
		if ((serving && playPoint < p) || (!serving && playPoint < q))
		{
			score.incrementTarget();
		}
		else
		{
			score.incrementOpponent();
		}
		if (runs == 1)
		{
			outcomes.addPrediction(p, q, serving, score);
		}
	}
}