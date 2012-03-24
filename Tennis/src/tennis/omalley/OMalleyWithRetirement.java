package tennis.omalley;

import static java.lang.Math.pow;

public class OMalleyWithRetirement
{
	public static double matchInProgressWithRetirement(final double onServe, final double returnServe,
													   final CurrentMatchScore matchScore, final CurrentSetScore setScore, final CurrentGameScore gameScore,
													   final boolean servingNext, final int numSetsForWin, final double retirementRisk)
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
			final double s = setInProgressWithRetirement(p, q, setScore, gameScore, servingNext, retirementRisk);
			return s * matchInProgressWithRetirement(p, q, matchScore.incTargetSets(), new CurrentSetScore(), new CurrentGameScore(), (Math.random() < 0.5) ? true : false, numSetsForWin, retirementRisk)
				 + (1 - s) * matchInProgressWithRetirement(p, q, matchScore.incOpponentSets(), new CurrentSetScore(), new CurrentGameScore(), (Math.random() < 0.5) ? true : false, numSetsForWin, retirementRisk);
		}
	}

	private static double setInProgressWithRetirement(final double onServe, final double returnServe,
												      final CurrentSetScore setScore, final CurrentGameScore gameScore,
												      final boolean servingNext, final double retirementRisk)
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
			return tiebreak(p, q, retirementRisk);
		}
		final double g = (servingNext) ? gameInProgressWithRetirement(p, gameScore, retirementRisk) : gameInProgressWithRetirement(q, gameScore, retirementRisk);
		return g * setInProgressWithRetirement(p, q, setScore.incTargetGames(), new CurrentGameScore(), !servingNext, retirementRisk)
		     + (1 - g) * setInProgressWithRetirement(p, q, setScore.incOpponentGames(), new CurrentGameScore(), !servingNext, retirementRisk);
	}

	private static double tiebreak(final double onServe, final double returnServe, final double retirementRisk)
	{
		final double p = onServe;
		final double q = returnServe;

		double result = 0;
		for (final double[] a : Matrices.tbMatrix)
		{
			result += a[0] * pow(p, a[1]) * pow(1 - p, a[2]) * pow(q, a[3]) * pow(1 - q, a[4]) * pow(d(p, q), a[5]);
		}
		return result * (1 - retirementRisk);
	}

	private static double d(final double p, final double q)
	{
		return p * q * pow(1 - (p * (1 - q) + (1 - p) * q), -1);
	}

	private static double gameInProgressWithRetirement(final double probabilityOfWinningPoint, final CurrentGameScore gameScore, final double retirementRisk)
	{
		final double p = probabilityOfWinningPoint;
		final int a = gameScore.getTargetPoints();
		final int b = gameScore.getOpponentPoints();

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
			return Math.pow(p, 2) / (1 - 2 * p * (1 - p));
		}
		else
		{
			final double r = 1 - retirementRisk;
			return (p * gameInProgressWithRetirement(p, gameScore.incTargetPoints(), retirementRisk) + (1 - p) * gameInProgressWithRetirement(p, gameScore.incOpponentPoints(), retirementRisk)) * r;
		}
	}
}
