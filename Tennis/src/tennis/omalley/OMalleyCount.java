package tennis.omalley;

import static java.lang.Math.pow;

public class OMalleyCount
{
	public static MatchAnalysis matchInProgressCount(final double onServe, final double returnServe, final double r, final boolean servingNext, final int numSetsForWin)
	{
		return matchInProgressCount(onServe, returnServe, r, new CurrentMatchScore(), new CurrentSetScore(), new CurrentGameScore(), servingNext, numSetsForWin, 0, 0, 0);
	}

	public static MatchAnalysis matchInProgressCount(final double onServe, final double returnServe, final double r,
													 final CurrentMatchScore matchScore, final CurrentSetScore setScore, final CurrentGameScore gameScore,
													 final boolean servingNext, final int numSetsForWin, final double points, final double levels, final double risk)
	{
		final double p = onServe;
		final double q = returnServe;

		if (matchScore.getTargetSets() == numSetsForWin)
		{
			return new MatchAnalysis(1.0, 0.0, points, levels);
		}
		if (matchScore.getOpponentSets() == numSetsForWin)
		{
			return new MatchAnalysis(0.0, 0.0, points, levels);
		}
		else // Doesn't matter who serves first the next set because you don't know who served at the end of the previous set
		{
			final MatchAnalysis s = setInProgressCount(p, q, r, setScore, gameScore, servingNext, 0, 0, 0);
			final MatchAnalysis targetAnalysis = matchInProgressCount(p, q, r, matchScore.incTargetSets(), new CurrentSetScore(), new CurrentGameScore(), (Math.random() < 0.5) ? true : false, numSetsForWin, 0, 0, 0);
			final MatchAnalysis opponentAnalysis = matchInProgressCount(p, q, r, matchScore.incOpponentSets(), new CurrentSetScore(), new CurrentGameScore(), (Math.random() < 0.5) ? true : false, numSetsForWin, 0, 0, 0);
			return new MatchAnalysis(s.mwp * targetAnalysis.mwp + (1 - s.mwp) * opponentAnalysis.mwp,
								     risk + (s.mwp * targetAnalysis.risk + (1 - s.mwp) * opponentAnalysis.risk) + s.risk,
									 points + (s.mwp * targetAnalysis.points + (1 - s.mwp) * opponentAnalysis.points) + s.points,
									 levels + (s.mwp * targetAnalysis.levels + (1 - s.mwp) * opponentAnalysis.levels) + 1);
		}
	}

	public static MatchAnalysis setInProgressCount(final double onServe, final double returnServe, final double r, final boolean servingNext)
	{
		return setInProgressCount(onServe, returnServe, r, new CurrentSetScore(), new CurrentGameScore(), servingNext, 0, 0, 0);
	}

	public static MatchAnalysis setInProgressCount(final double onServe, final double returnServe, final double r,
												   final CurrentSetScore setScore, final CurrentGameScore gameScore,
												   final boolean servingNext, final double points, final double levels, final double risk)
	{
		final double p = onServe;
		final double q = returnServe;
		final int targetGames = setScore.getTargetGames();
		final int opponentGames = setScore.getOpponentGames();

		if (targetGames == 7 || (targetGames == 6 && opponentGames < 5))
		{
			return new MatchAnalysis(1.0, 0.0, points, levels);
		}
		if ((targetGames < 5 && opponentGames == 6) || opponentGames == 7)
		{
			return new MatchAnalysis(0.0, 0.0, points, levels);
		}
		if (targetGames == 6 && opponentGames == 6)
		{
			return new MatchAnalysis(tiebreak(p, q), 0.0, points + 12, levels + 1);
		}
		final MatchAnalysis g = (servingNext) ? gameInProgressCount(p, r, gameScore, 0, 0, 0) : gameInProgressCount(q, r, gameScore, 0, 0, 0);
		final MatchAnalysis targetAnalysis = setInProgressCount(p, q, r, setScore.incTargetGames(), new CurrentGameScore(), !servingNext, 0, 0, 0);
		final MatchAnalysis opponentAnalysis = setInProgressCount(p, q, r, setScore.incOpponentGames(), new CurrentGameScore(), !servingNext, 0, 0, 0);
		return new MatchAnalysis(g.mwp * targetAnalysis.mwp + (1 - g.mwp) * opponentAnalysis.mwp,
								 risk + (g.mwp * targetAnalysis.risk + (1 - g.mwp) * opponentAnalysis.risk) + g.risk,
								 points + (g.mwp * targetAnalysis.points + (1 - g.mwp) * opponentAnalysis.points) + g.points,
								 levels + (g.mwp * targetAnalysis.levels + (1 - g.mwp) * opponentAnalysis.levels) + 1);
	}

	private static double tiebreak(final double onServe, final double returnServe)
	{
		final double p = onServe;
		final double q = returnServe;

		double result = 0;
		for (final double[] a : Matrices.tbMatrix)
		{
			result += a[0] * pow(p, a[1]) * pow(1 - p, a[2]) * pow(q, a[3]) * pow(1 - q, a[4]) * pow(d(p, q), a[5]);
		}
		return result;
	}

	private static double d(final double p, final double q)
	{
		return p * q * pow(1 - (p * (1 - q) + (1 - p) * q), -1);
	}

	public static MatchAnalysis gameInProgressCount(final double p, final double r)
	{
		return gameInProgressCount(p, r, new CurrentGameScore(), 0, 0, 0);
	}

	public static MatchAnalysis gameInProgressCount(final double p, final double r, final CurrentGameScore gameScore, final double points, final double levels, final double risk)
	{
		final int a = gameScore.getTargetPoints();
		final int b = gameScore.getOpponentPoints();

		if (a > b && a >= 4 && Math.abs(a - b) >= 2)
		{
			return new MatchAnalysis(1.0, 0.0, points, levels);
		}
		if (b > a && b >= 4 && Math.abs(a - b) >= 2)
		{
			return new MatchAnalysis(0.0, 0.0, points, levels);
		}
		if (a == b && a >= 3) // Deuce (sum of an infinite geometric series)
		{
			return new MatchAnalysis(Math.pow(p, 2) / (1 - 2 * p * (1 - p)), 0.0, points + 4, levels);
		}
		else
		{
			final MatchAnalysis targetAnalysis = gameInProgressCount(p, r, gameScore.incTargetPoints(), 0, 0, 0);
			final MatchAnalysis opponentAnalysis = gameInProgressCount(p, r, gameScore.incOpponentPoints(), 0, 0, 0);
			return new MatchAnalysis(p * targetAnalysis.mwp + (1 - p) * opponentAnalysis.mwp,
									 risk + (p * targetAnalysis.risk + (1 - p) * opponentAnalysis.risk) + r,
									 points + (p * targetAnalysis.points + (1 - p) * opponentAnalysis.points) + 1,
									 levels + (p * targetAnalysis.levels + (1 - p) * opponentAnalysis.levels) + 1);
		}
	}

	public static class MatchAnalysis
	{
		public double mwp;
		public double risk;
		public double points;
		public double levels;

		MatchAnalysis(final double mwp, final double risk, final double points, final double levels)
		{
			this.mwp = mwp;
			this.risk = risk;
			this.points = points;
			this.levels = levels;
		}
	}
}
