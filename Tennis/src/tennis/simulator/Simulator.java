package tennis.simulator;

public class Simulator
{
	private final MatchState initialState;
	private final boolean scenario;

	public Simulator()
	{
		this(new MatchState(), false);
	}

	public Simulator(final MatchState initialState, final boolean scenario)
	{
		this.initialState = initialState;
		this.scenario = scenario;
	}

	public double simulate(final double onServe, final double returnServe, final double runs)
	{
		final double p = onServe;
		final double q = returnServe;

		double matchesWon = 0.0;
		for (int i = 0; i < runs; i++)
		{
			// If simulating a particular scenario, we want to replicate the starting conditions exactly
			// Otherwise, start a fresh match with a random first server
			final MatchState score = scenario ? new MatchState(initialState) : new MatchState();
			while (!score.finished())
			{
				while (!score.setFinished())
				{
					while (!score.gameFinished())
					{
						playPoint(p, q, score, score.isServingNext());
					}
					if (score.tiebreak()) // Assume tiebreaks are always used for now
					{
						playTiebreak(p, q, score);
					}
				}
			}
			if (score.targetWon())
			{
				matchesWon++;
			}
		}

		return matchesWon / runs;
	}

	private void playTiebreak(final double p, final double q, final MatchState score)
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

	private void playPoint(final double p, final double q, final Score score, final boolean servingNext)
	{
		final double playPoint = Math.random();
		if ((servingNext && playPoint < p) || (!servingNext && playPoint < q))
		{
			score.incrementTarget();
		}
		else
		{
			score.incrementOpponent();
		}
	}
}