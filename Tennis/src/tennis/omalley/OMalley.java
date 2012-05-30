package tennis.omalley;

import static java.lang.Math.pow;

public final class OMalley
{
	public static double game(final double p)
	{
		return pow(p, 4) * (15 - (4 * p) - ((10 * pow(p, 2)) / (1 - (2 * p) * (1 - p))));
	}

	public static double tiebreak(final double pa, final double pb)
	{
		final double p = pa;
		final double q = 1 - pb;

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

	public static double set(final double pa, final double pb)
	{
		final double gp = game(pa);
		final double gq = game(1 - pb);

		double result = 0;
		for (final double[] b : Matrices.setMatrix)
		{
			result += b[0] * pow(gp, b[1]) * pow(1 - gp, b[2]) * pow(gq, b[3]) * pow(1 - gq, b[4]) *
					  pow(gp * gq + (gp * (1 - gq) + (1 - gp) * gq) * tiebreak(pa, pb), b[5]);
		}
		return result;
	}

	public static double bestOfThree(final double pa, final double pb)
	{
		System.out.println("Three set match with pa = " + pa + ", pb = " + pb);
		return pow(set(pa, pb), 2) * (1 + 2 * (1 - set(pa, pb)));
	}

	public static double bestOfFive(final double pa, final double pb)
	{
		System.out.println("Five set match with pa = " + pa + ", pb = " + pb);
		return pow(set(pa, pb), 3) * (1 + 3 * (1 - set(pa, pb)) + 6 * (pow(1 - set(pa, pb), 2)));
	}

	public static double gameInProgress(final double p)
	{
		return gameInProgress(p, new CurrentGameScore());
	}

	public static double gameInProgress(final double p, final CurrentGameScore gameScore)
	{
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

	public static double setInProgress(final double pa, final double pb, final boolean servingNext)
	{
		return setInProgress(pa, pb, new CurrentSetScore(), new CurrentGameScore(), servingNext);
	}

	public static double setInProgress(final double pa, final double pb, final CurrentSetScore setScore, final boolean servingNext)
	{
		return setInProgress(pa, pb, setScore, new CurrentGameScore(), servingNext);
	}

	public static double setInProgress(final double pa, final double pb,
								   	   final CurrentSetScore setScore, final CurrentGameScore gameScore,
								   	   final boolean servingNext)
	{
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
			return tiebreak(pa, pb);
		}
		final double g = (servingNext) ? gameInProgress(pa, gameScore) : gameInProgress(1 - pb, gameScore);
		return g * setInProgress(pa, pb, setScore.incTargetGames(), new CurrentGameScore(), !servingNext) + (1 - g) * setInProgress(pa, pb, setScore.incOpponentGames(), new CurrentGameScore(), !servingNext);
	}

	public static double matchInProgress(final double pa, final double pb,
								     	 final CurrentMatchScore matchScore, final CurrentSetScore setScore, final CurrentGameScore gameScore,
								     	 final boolean servingNext, final int numSetsForWin)
	{
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
			final double s = setInProgress(pa, pb, setScore, gameScore, servingNext);
			return s * matchInProgress(pa, pb, matchScore.incTargetSets(), numSetsForWin) + (1 - s) * matchInProgress(pa, pb, matchScore.incOpponentSets(), numSetsForWin);
		}
	}

	public static double matchInProgress(final double pa, final double pb, final CurrentMatchScore matchScore, final int numSetsForWin)
	{
		return matchInProgress(pa, pb, matchScore, new CurrentSetScore(), (Math.random() < 0.5) ? true : false, numSetsForWin);
	}

	public static double matchInProgress(final double pa, final double pb, final CurrentMatchScore matchScore, final CurrentSetScore setScore, final boolean servingNext, final int numSetsForWin)
	{
		return matchInProgress(pa, pb, matchScore, setScore, new CurrentGameScore(), servingNext, numSetsForWin);
	}

	public static double matchInProgress(final double pa, final double pb, final int numSetsForWin)
	{
		return matchInProgress(pa, pb, new CurrentMatchScore(), numSetsForWin);
	}
}
