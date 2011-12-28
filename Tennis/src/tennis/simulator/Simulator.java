package tennis.simulator;

public class Simulator
{
	public double simulateFiveSetMatch(final double onServe, final double returnServe, final double runs)
	{
		final double p = onServe;
		final double q = returnServe;

		double matchesWon = 0.0;
		for (int i = 0; i < runs; i++)
		{
			final MatchState score = new MatchState();
			while (!score.matchOver())
			{
				while (!score.setOver())
				{
					while (!score.gameOver())
					{
						playPoint(p, q, score, score.isServingNext());
					}
					if (score.tiebreak()) // Assume tiebreakers always used
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
		boolean servingNext = score.isServingNext();

		// Service changes after first point
		// Whoever serves first is the server for this 'game'
		playPoint(p, q, score, servingNext);
		servingNext = !servingNext;

		int i = 0;
		while (!score.tiebreakOver())
		{
			playPoint(p, q, score, servingNext);
			i++;
			if (i == 2) // Service then changes after every two points
			{
				servingNext = !servingNext;
				i = 0;
			}
		}
	}

	private void playPoint(final double p, final double q, final MatchState score, final boolean servingNext)
	{
		final double playPoint = Math.random();
		if ((servingNext && playPoint < p) || (!servingNext && playPoint < q))
		{
			score.targetPoint();
		}
		else
		{
			score.opponentPoint();
		}
	}
}