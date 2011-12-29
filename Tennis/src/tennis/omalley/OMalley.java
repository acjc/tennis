package tennis.omalley;

import static java.lang.Math.pow;

public final class OMalley
{
	public static double game(final double probabilityOfPointOnServe)
	{
		final double p = probabilityOfPointOnServe;
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

	public static double setInPlay(final double onServe, final double returnServe, final int targetScore, final int opponentScore, final boolean servingNext)
	{
		final double p = onServe;
		final double q = returnServe;
		final int a = targetScore;
		final int b = opponentScore;
		final double gp = game(p);
		final double gq = game(q);

		if (a == 7 || (a == 6 && b < 5)) // Target player has won
		{
			return 1.0;
		}
		if ((a < 5 && b == 6) || b == 7) // Opponent has won
		{
			return 0.0;
		}
		if (a == 6 && b == 6) // Either the target player wins 7-5 or he/she wins on a tiebreak
		{
			return tiebreak(p, q);
		}
		if (servingNext) // Otherwise, depends who is next to serve
		{
			return gp * setInPlay(p, q, a + 1, b, false) + (1 - gp) * setInPlay(p, q, a, b + 1, false);
		}
		else
		{
			return gq * setInPlay(p, q, a + 1, b, true) + (1 - gq) * setInPlay(p, q, a, b + 1, true);
		}
	}

	public static double bestOfThree(final double onServe, final double returnServe)
	{
		final double p = onServe;
		final double q = returnServe;

		return pow(set(p, q), 2) * (1 + 2 * (1 - set(p, q)));
	}

	public static double bestOfFive(final double onServe, final double returnServe)
	{
		final double p = onServe;
		final double q = returnServe;

		return pow(set(p, q), 3) * (1 + 3 * (1 - set(p, q)) + 6 * (pow(1 - set(p, q), 2)));
	}
}
