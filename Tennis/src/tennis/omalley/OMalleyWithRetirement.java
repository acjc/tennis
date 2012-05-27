package tennis.omalley;

import static java.lang.Math.pow;
import tennis.omalley.OMalleyCount.MatchAnalysis;

public final class OMalleyWithRetirement
{
	public static double tiebreakWithRetirement(final double onServe, final double returnServe,
												final double retirementRisk, final double levelsRemaining, final double expectedMwp)
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

	public static double gameInProgressWithRetirement(final double probabilityOfWinningPoint, final CurrentGameScore gameScore,
													  final double retirementRisk, final double levelsRemaining, final double expectedMwp)
	{
		final double p = probabilityOfWinningPoint;
		final int a = gameScore.getTargetPoints();
		final int b = gameScore.getOpponentPoints();

		final double r = 1 - Math.pow(1 - (retirementRisk / expectedMwp), 1 / levelsRemaining);
//		final double r = retirementRisk;
		if (a > b && a >= 4 && Math.abs(a - b) >= 2)
		{
			return 1.0;
		}
		if (b > a && b >= 4 && Math.abs(a - b) >= 2)
		{
			return 0.0;
		}
		if (a == b && a >= 3) // Deuce (sum of an infinite geometric series)
		{
			return (Math.pow(p, 2) / (1 - 2 * p * (1 - p)));
		}
		else
		{
			// Probability of winning is probability you don't retire and then you win
			return (1 - r) * (p * gameInProgressWithRetirement(p, gameScore.incTargetPoints(), retirementRisk, levelsRemaining, expectedMwp)
					          + (1 - p) * gameInProgressWithRetirement(p, gameScore.incOpponentPoints(), retirementRisk, levelsRemaining, expectedMwp));
		}
	}

	public static double setInProgressWithRetirement(final double onServe, final double returnServe,
													 final CurrentSetScore setScore, final CurrentGameScore gameScore,
													 final boolean servingNext, final double retirementRisk, final double levelsRemaining, final double expectedMwp)
	{
		final double p = onServe;
		final double q = returnServe;
		final int targetGames = setScore.getTargetGames();
		final int opponentGames = setScore.getOpponentGames();

		if (targetGames == 7 || (targetGames == 6 && opponentGames < 5))
		{
			return 1.0;
		}
		if ((targetGames < 5 && opponentGames == 6) || opponentGames == 7)
		{
			return 0.0;
		}
		if (targetGames == 6 && opponentGames == 6)
		{
			return tiebreakWithRetirement(p, q, retirementRisk, levelsRemaining, expectedMwp);
		}

		final double sPlus = setInProgressWithRetirement(p, q, setScore.incTargetGames(), new CurrentGameScore(), !servingNext, retirementRisk, levelsRemaining, expectedMwp);
		final double sMinus = setInProgressWithRetirement(p, q, setScore.incOpponentGames(), new CurrentGameScore(), !servingNext, retirementRisk, levelsRemaining, expectedMwp);

		final MatchAnalysis gameAnalysis = servingNext ? OMalleyCount.gameInProgressCount(p, gameScore) : OMalleyCount.gameInProgressCount(q, gameScore);
		final double gameRisk = retirementRisk * (1 / levelsRemaining) + (retirementRisk / 2);
		final double g = servingNext ? gameInProgressWithRetirement(p, gameScore, gameRisk, gameAnalysis.levels, gameAnalysis.mwp) : gameInProgressWithRetirement(q, gameScore, gameRisk, gameAnalysis.levels, gameAnalysis.mwp);

		return g * sPlus + (1 - g) * sMinus;
	}

	public static double matchInProgressWithRetirement(final double onServe, final double returnServe,
								     	 			   final CurrentMatchScore matchScore, final CurrentSetScore setScore, final CurrentGameScore gameScore,
								     	 			   final boolean servingNext, final int numSetsForWin, final double retirementRisk, final double levelsRemaining, final double expectedMwp)
	{
		final double p = onServe;
		final double q = returnServe;

		if (matchScore.getTargetSets() == numSetsForWin)
		{
			return 1.0;
		}
		if (matchScore.getOpponentSets() == numSetsForWin)
		{
			return 0.0;
		}
		else // Doesn't matter who serves first the next set because you don't know who served at the end of the previous set
		{
			final double s = setInProgressWithRetirement(p, q, setScore, gameScore, servingNext, retirementRisk, levelsRemaining, expectedMwp);
			return s * matchInProgressWithRetirement(p, q, matchScore.incTargetSets(), new CurrentSetScore(), new CurrentGameScore(), (Math.random() < 0.5) ? true : false, numSetsForWin, retirementRisk, levelsRemaining, expectedMwp)
				   + (1 - s) * matchInProgressWithRetirement(p, q, matchScore.incOpponentSets(), new CurrentSetScore(), new CurrentGameScore(), (Math.random() < 0.5) ? true : false, numSetsForWin, retirementRisk, levelsRemaining, expectedMwp);
		}
	}
}
