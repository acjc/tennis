package tennis.omalley;

import static java.lang.Math.pow;

public class OMalleyCount
{
	public static int levels = 0;

	public static MatchAnalysis matchInProgressCount(final double onServe, final double returnServe,
													 final CurrentMatchScore matchScore, final CurrentSetScore setScore, final CurrentGameScore gameScore,
													 final boolean servingNext, final int numSetsForWin, final double points, final double levels)
	{
		final double p = onServe;
		final double q = returnServe;

		if (matchScore.getTargetSets() == numSetsForWin)
		{
			return new MatchAnalysis(1.0, points, levels);
		}
		if (matchScore.getOpponentSets() == numSetsForWin)
		{
			return new MatchAnalysis(0.0, points, levels);
		}
		else // Doesn't matter who serves first the next set because you don't know who served at the end of the previous set
		{
			final MatchAnalysis s = setInProgressCount(p, q, setScore, gameScore, servingNext, 0, 0);
			final MatchAnalysis targetAnalysis = matchInProgressCount(p, q, matchScore.incTargetSets(), new CurrentSetScore(), new CurrentGameScore(), (Math.random() < 0.5) ? true : false, numSetsForWin, 0, levels + s.levels);
			final MatchAnalysis opponentAnalysis = matchInProgressCount(p, q, matchScore.incOpponentSets(), new CurrentSetScore(), new CurrentGameScore(), (Math.random() < 0.5) ? true : false, numSetsForWin, 0, levels + s.levels);
			return new MatchAnalysis(s.mwp * targetAnalysis.mwp + (1 - s.mwp) * opponentAnalysis.mwp,
									 points + (s.mwp * targetAnalysis.points + (1 - s.mwp) * opponentAnalysis.points) + s.points,
									 s.mwp * targetAnalysis.levels + (1 - s.mwp) * opponentAnalysis.levels);
		}
	}

	public static MatchAnalysis setInProgressCount(final double onServe, final double returnServe,
												   final CurrentSetScore setScore, final CurrentGameScore gameScore,
												   final boolean servingNext, final double points, final double levels)
	{
		final double p = onServe;
		final double q = returnServe;
		final int targetGames = setScore.getTargetGames();
		final int opponentGames = setScore.getOpponentGames();

		if (targetGames == 7 || (targetGames == 6 && opponentGames < 5))
		{
			return new MatchAnalysis(1.0, points, levels);
		}
		if ((targetGames < 5 && opponentGames == 6) || opponentGames == 7)
		{
			return new MatchAnalysis(0.0, points, levels);
		}
		if (targetGames == 6 && opponentGames == 6)
		{
			return new MatchAnalysis(tiebreak(p, q), points + 12, levels + 1);
		}
		final MatchAnalysis g = (servingNext) ? gameInProgressCount(p, gameScore, points, levels) : gameInProgressCount(q, gameScore, 0, 0);
		final MatchAnalysis targetAnalysis = setInProgressCount(p, q, setScore.incTargetGames(), new CurrentGameScore(), !servingNext, 0, levels + 1);
		final MatchAnalysis opponentAnalysis = setInProgressCount(p, q, setScore.incOpponentGames(), new CurrentGameScore(), !servingNext, 0, levels + 1);
		return new MatchAnalysis(g.mwp * targetAnalysis.mwp + (1 - g.mwp) * opponentAnalysis.mwp,
								 points + (g.mwp * targetAnalysis.points + (1 - g.mwp) * opponentAnalysis.points) + g.points,
								 g.mwp * targetAnalysis.levels + (1 - g.mwp) * opponentAnalysis.levels);
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

	public static MatchAnalysis gameInProgressCount(final double probabilityOfWinningPoint, final CurrentGameScore gameScore, final double points, final double levels)
	{
		final double p = probabilityOfWinningPoint;
		final int a = gameScore.getTargetPoints();
		final int b = gameScore.getOpponentPoints();

		if (a > b && a >= 4 && Math.abs(a - b) >= 2)
		{
			return new MatchAnalysis(1.0, points, levels);
		}
		if (b > a && b >= 4 && Math.abs(a - b) >= 2)
		{
			return new MatchAnalysis(0.0, points, levels);
		}
		if (a == b && a >= 3) // Deuce (sum of an infinite geometric series)
		{
			return new MatchAnalysis(Math.pow(p, 2) / (1 - 2 * p * (1 - p)), points + 4, levels);
		}
		else
		{
			final MatchAnalysis targetAnalysis = gameInProgressCount(p, gameScore.incTargetPoints(), 0, levels + 1);
			final MatchAnalysis opponentAnalysis = gameInProgressCount(p, gameScore.incOpponentPoints(), 0, levels + 1);
			return new MatchAnalysis(p * targetAnalysis.mwp + (1 - p) * opponentAnalysis.mwp,
									 points + (p * targetAnalysis.points + (1 - p) * opponentAnalysis.points) + 1,
									 p * targetAnalysis.levels + (1 - p) * opponentAnalysis.levels);
		}
	}

	public static double gameTreeDepth(final double probabilityOfWinningPoint, final CurrentGameScore gameScore, final double depth)
	{
		final double p = probabilityOfWinningPoint;
		final int a = gameScore.getTargetPoints();
		final int b = gameScore.getOpponentPoints();

		if (a > b && a >= 4 && Math.abs(a - b) >= 2)
		{
			return depth;
		}
		if (b > a && b >= 4 && Math.abs(a - b) >= 2)
		{
			return depth;
		}
		if (a == b && a >= 3) // Deuce (sum of an infinite geometric series)
		{
			return depth;
		}
		else
		{
			return p * gameTreeDepth(p, gameScore.incTargetPoints(), depth + 1) + (1 - p) * gameTreeDepth(p, gameScore.incOpponentPoints(), depth + 1);
		}
	}

	public static double setTreeDepth(final double p, final double q, final CurrentSetScore setScore, final CurrentGameScore gameScore, final boolean servingNext, final double depth)
	{
		final int targetGames = setScore.getTargetGames();
		final int opponentGames = setScore.getOpponentGames();

		if (targetGames == 7 || (targetGames == 6 && opponentGames < 5))
		{
			return depth;
		}
		if ((targetGames < 5 && opponentGames == 6) || opponentGames == 7)
		{
			return depth;
		}
		if (targetGames == 6 && opponentGames == 6)
		{
			return depth;
		}
		else
		{
			final double g = (servingNext) ? gameInProgressCount(p, gameScore, 0, 0).mwp : gameInProgressCount(q, gameScore, 0, 0).mwp;
			return g * setTreeDepth(p, q, setScore.incTargetGames(), new CurrentGameScore(), !servingNext, depth + 1) + (1 - g) * setTreeDepth(p, q, setScore.incOpponentGames(), new CurrentGameScore(), !servingNext, depth + 1);
		}
	}

	public static double matchTreeDepth(final double p, final double q,
										final CurrentMatchScore matchScore, final CurrentSetScore setScore, final CurrentGameScore gameScore,
										final boolean servingNext, final int numSetsForWin, final double depth)
	{
		if (matchScore.getTargetSets() == numSetsForWin)
		{
			return depth;
		}
		if (matchScore.getOpponentSets() == numSetsForWin)
		{
			return depth;
		}
		else
		{
			final double s = setInProgressCount(p, q, setScore, gameScore, servingNext, 0, 0).mwp;
			return s * matchTreeDepth(p, q, matchScore.incTargetSets(), new CurrentSetScore(), new CurrentGameScore(), Math.random() < 0.5 ? true : false, 3, depth + 1)
				+ (1 - s) * matchTreeDepth(p, q, matchScore.incOpponentSets(), new CurrentSetScore(), new CurrentGameScore(), Math.random() < 0.5 ? true : false, 3, depth + 1);
		}
	}

	public static class MatchAnalysis
	{
		public double mwp;
		public double points;
		public double levels;

		MatchAnalysis(final double mwp, final double points, final double levels)
		{
			this.mwp = mwp;
			this.points = points;
			this.levels = levels;
		}
	}
}
