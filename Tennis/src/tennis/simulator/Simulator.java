package tennis.simulator;

import java.io.IOException;

public class Simulator
{
	private double runs = 1;
	private SimulationOutcomes outcomes;

	public SimulationOutcomes simulate(final double pa, final double pb, final double runs) throws IOException
	{
		return simulate(pa, pb, new MatchState(), false, runs);
	}

	public SimulationOutcomes simulate(final double pa, final double pb, final int numSetsToWin, final double runs) throws IOException
	{
		return simulate(pa, pb, new MatchState(numSetsToWin), false, runs);
	}

	public SimulationOutcomes simulate(final double pa, final double pb, final MatchState initialState, final boolean isScenario, final double runs) throws IOException
	{
		this.runs = runs;
		this.outcomes = new SimulationOutcomes(runs);
		final long startTime = System.currentTimeMillis();
		for (int i = 0; i < runs; i++)
		{
			// When simulating a particular scenario, we want to replicate the starting conditions exactly
			// Otherwise, start a fresh match with a random first server
			final MatchState result = new MatchState(initialState);
			if (!isScenario)
			{
				result.coinToss();
			}
			simulateMatch(pa, pb, result);
			outcomes.update(result);
		}
		final long endTime = System.currentTimeMillis();
		outcomes.setSimulationTime((endTime - startTime) / 1000.0);
		return outcomes;
	}

	private MatchState simulateMatch(final double pa, final double pb, final MatchState score) throws IOException
	{
		while (!score.over())
		{
			while (!score.setOver())
			{
				while (!score.gameOver())
				{
					playPoint(pa, pb, score);
				}
				if (score.tiebreak()) // Assume tiebreaks are always used for now
				{
					playTiebreak(pa, pb, score);
				}
			}
		}
		return score;
	}

	private void playTiebreak(final double pa, final double pb, final MatchState score) throws IOException
	{
		// Whomever serves first is the server for this 'game'
		boolean servingNext = score.isServingNext();
		while (!score.tiebreakOver())
		{
			playPoint(pa, pb, score, servingNext);
			if (score.isOddPoint()) // Service changes every odd point
			{
				servingNext = !servingNext;
			}
		}
	}

	private void playPoint(final double pa, final double pb, final MatchState score) throws IOException
	{
		playPoint(pa, pb, score, score.isServingNext());
	}

	private void playPoint(final double pa, final double pb, final MatchState score, final boolean serving) throws IOException
	{
		final double playPoint = Math.random();
		if ((serving && playPoint < pa) || (!serving && playPoint >= pb))
		{
			score.incrementTarget();
		}
		else
		{
			score.incrementOpponent();
		}
		if (runs == 1)
		{
			outcomes.addPrediction(pa, pb, serving, score);
			outcomes.addInjuryPrediction(pa, pb, serving, score);
		}
	}
}