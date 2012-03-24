package tennis.omalley;

import static java.lang.Math.pow;

public final class OMalley
{
	public static double game(final double probabilityOfWinningPoint)
	{
		final double p = probabilityOfWinningPoint;
		return pow(p, 4) * (15 - (4 * p) - ((10 * pow(p, 2)) / (1 - (2 * p) * (1 - p))));
	}

	public static double tiebreak(final double onServe, final double returnServe)
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

	public static double set(final double onServe, final double returnServe)
	{
		final double p = onServe;
		final double q = returnServe;
		final double gp = game(p);
		final double gq = game(q);

		double result = 0;
		for (final double[] b : Matrices.setMatrix)
		{
			result += b[0] * pow(gp, b[1]) * pow(1 - gp, b[2]) * pow(gq, b[3]) * pow(1 - gq, b[4]) *
					  pow(gp * gq + (gp * (1 - gq) + (1 - gp) * gq) * tiebreak(p, q), b[5]);
		}
		return result;
	}

	public static double bestOfThree(final double onServe, final double returnServe)
	{
		final double p = onServe;
		final double q = returnServe;
		System.out.println("Three set match with p = " + p + " q = " + q);

		return pow(set(p, q), 2) * (1 + 2 * (1 - set(p, q)));
	}

	public static double bestOfFive(final double onServe, final double returnServe)
	{
		final double p = onServe;
		final double q = returnServe;
		System.out.println("Five set match with p = " + p + " q = " + q);

		return pow(set(p, q), 3) * (1 + 3 * (1 - set(p, q)) + 6 * (pow(1 - set(p, q), 2)));
	}

	public static double gameInProgress(final double probabilityOfWinningPoint)
	{
		return gameInProgress(probabilityOfWinningPoint, new CurrentGameScore());
	}

	public static double gameInProgress(final double probabilityOfWinningPoint, final CurrentGameScore gameScore)
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
			return p * gameInProgress(p, gameScore.incTargetPoints()) + (1 - p) * gameInProgress(p, gameScore.incOpponentPoints());
		}
	}

	public static double setInProgress(final double onServe, final double returnServe, final boolean servingNext)
	{
		return setInProgress(onServe, returnServe, new CurrentSetScore(), new CurrentGameScore(), servingNext);
	}

	public static double setInProgress(final double onServe, final double returnServe, final CurrentSetScore setScore, final boolean servingNext)
	{
		return setInProgress(onServe, returnServe, setScore, new CurrentGameScore(), servingNext);
	}

	public static double setInProgress(final double onServe, final double returnServe,
								   	   final CurrentSetScore setScore, final CurrentGameScore gameScore,
								   	   final boolean servingNext)
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
			return tiebreak(p, q);
		}
		final double g = (servingNext) ? gameInProgress(p, gameScore) : gameInProgress(q, gameScore);
		return g * setInProgress(p, q, setScore.incTargetGames(), new CurrentGameScore(), !servingNext) + (1 - g) * setInProgress(p, q, setScore.incOpponentGames(), new CurrentGameScore(), !servingNext);
	}

	public static double matchInProgress(final double onServe, final double returnServe,
								     	 final CurrentMatchScore matchScore, final CurrentSetScore setScore, final CurrentGameScore gameScore,
								     	 final boolean servingNext, final int numSetsForWin)
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
			final double s = setInProgress(p, q, setScore, gameScore, servingNext);
			return s * matchInProgress(p, q, matchScore.incTargetSets(), numSetsForWin) + (1 - s) * matchInProgress(p, q, matchScore.incOpponentSets(), numSetsForWin);
		}
	}

	public static double matchInProgress(final double onServe, final double returnServe,
									     final CurrentMatchScore matchScore,
									     final int numSetsForWin)
	{
		return matchInProgress(onServe, returnServe, matchScore, new CurrentSetScore(), new CurrentGameScore(), (Math.random() < 0.5) ? true : false, numSetsForWin);
	}

	public static double matchInProgress(final double onServe, final double returnServe, final int numSetsForWin)
	{
		return matchInProgress(onServe, returnServe, new CurrentMatchScore(), numSetsForWin);
	}
}
