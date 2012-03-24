package tennis.omalley;

import static java.lang.Math.pow;

public class OMalleyAvgPointsToBePlayed
{
	public static MatchAnalysis matchInProgressAvgPointsToBePlayed(final double onServe, final double returnServe,
													   		   final CurrentMatchScore matchScore, final CurrentSetScore setScore, final CurrentGameScore gameScore,
													   		   final boolean servingNext, final int numSetsForWin, final double points)
	{
		final double p = onServe;
		final double q = returnServe;

		if (matchScore.getTargetSets() == numSetsForWin)
		{
			return new MatchAnalysis(1.0, points);
		}
		if (matchScore.getOpponentSets() == numSetsForWin)
		{
			return new MatchAnalysis(0.0, points);
		}
		else // Doesn't matter who serves first the next set because you don't know who served at the end of the previous set
		{
			final MatchAnalysis s = setInProgressAvgPointsToBePlayed(p, q, setScore, gameScore, servingNext, 0);
			final MatchAnalysis targetAnalysis = matchInProgressAvgPointsToBePlayed(p, q, matchScore.incTargetSets(), new CurrentSetScore(), new CurrentGameScore(), (Math.random() < 0.5) ? true : false, numSetsForWin, points + s.points);
			final MatchAnalysis opponentAnalysis = matchInProgressAvgPointsToBePlayed(p, q, matchScore.incOpponentSets(), new CurrentSetScore(), new CurrentGameScore(), (Math.random() < 0.5) ? true : false, numSetsForWin, points + s.points);
			return new MatchAnalysis(s.mwp * targetAnalysis.mwp + (1 - s.mwp) * opponentAnalysis.mwp, s.mwp * targetAnalysis.points + (1 - s.mwp) * opponentAnalysis.points);
		}
	}

	private static MatchAnalysis setInProgressAvgPointsToBePlayed(final double onServe, final double returnServe,
												       final CurrentSetScore setScore, final CurrentGameScore gameScore,
												       final boolean servingNext, final double points)
	{
		final double p = onServe;
		final double q = returnServe;
		final int targetGames = setScore.getTargetGames();
		final int opponentGames = setScore.getOpponentGames();

		if (targetGames == 7 || (targetGames == 6 && opponentGames < 5))
		{
			return new MatchAnalysis(1.0, points);
		}
		if ((targetGames < 5 && opponentGames == 6) || opponentGames == 7)
		{
			return new MatchAnalysis(0.0, points);
		}
		if (targetGames == 6 && opponentGames == 6)
		{
			return new MatchAnalysis(tiebreak(p, q), points + Math.random() * 12);
		}
		final MatchAnalysis g = (servingNext) ? gameInProgressAvgPointsToBePlayed(p, gameScore, points) : gameInProgressAvgPointsToBePlayed(q, gameScore, 0);
		final MatchAnalysis targetAnalysis = setInProgressAvgPointsToBePlayed(p, q, setScore.incTargetGames(), new CurrentGameScore(), !servingNext, points + g.points);
		final MatchAnalysis opponentAnalysis = setInProgressAvgPointsToBePlayed(p, q, setScore.incOpponentGames(), new CurrentGameScore(), !servingNext, points + g.points);
		return new MatchAnalysis(g.mwp * targetAnalysis.mwp + (1 - g.mwp) * opponentAnalysis.mwp, g.mwp * targetAnalysis.points + (1 - g.mwp) * opponentAnalysis.points);
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

	private static MatchAnalysis gameInProgressAvgPointsToBePlayed(final double probabilityOfWinningPoint, final CurrentGameScore gameScore, final double points)
	{
		final double p = probabilityOfWinningPoint;
		final int a = gameScore.getTargetPoints();
		final int b = gameScore.getOpponentPoints();

		if (a > b && a >= 4 && Math.abs(a - b) >= 2)
		{
			return new MatchAnalysis(1.0, points);
		}
		if (b > a && b >= 4 && Math.abs(a - b) >= 2)
		{
			return new MatchAnalysis(0.0, points);
		}
		if (a == b && a >= 3) // Deuce (sum of an infinite geometric series)
		{
			return new MatchAnalysis(Math.pow(p, 2) / (1 - 2 * p * (1 - p)), points + Math.random() * 5);
		}
		else
		{
			final MatchAnalysis targetAnalysis = gameInProgressAvgPointsToBePlayed(p, gameScore.incTargetPoints(), points);
			final MatchAnalysis opponentAnalysis = gameInProgressAvgPointsToBePlayed(p, gameScore.incOpponentPoints(), points);
			return new MatchAnalysis(p * targetAnalysis.mwp + (1 - p) * opponentAnalysis.mwp, p * targetAnalysis.points + (1 - p) * opponentAnalysis.points);
		}
	}

	static class MatchAnalysis
	{
		double mwp;
		double points;

		MatchAnalysis(final double mwp, final double points)
		{
			this.mwp = mwp;
			this.points = points;
		}
	}
}
