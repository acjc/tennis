package tennis.omalley.probabilities;

import static java.lang.Math.pow;
import tennis.omalley.matrices.Matrices;

public class OMalley {

	public double game(final double point)
	{
		final double p = point;
		return pow(p, 4) * (15 - (4 * p) - ((10 * pow(p, 2)) / (1 - (2 * p) * (1 - p))));
	}

	public double tiebreak(final double onServe, final double returnServe)
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

	private double d(final double p, final double q) {
		return p * q * pow(1 - (p * (1 - q) + (1 - p) * q), -1);
	}

	public double set(final double onServe, final double returnServe)
	{
		final double p = onServe;
		final double q = returnServe;
		final double gp = game(p);
		final double gq = game(q);
		double result = 0;
		for (final double[] b : Matrices.setMatrix)
		{
			result += b[0] * pow(gp, b[1]) * pow(1 - gp, b[2]) * pow(gq, b[3]) * pow(1 - gq, b[4]) *
					  pow(gp * gq + (gq * (1 - gq) + (1 - gp) * gq) * tiebreak(p, q), b[5]);
		}
		return result;
	}

	public double bestOfThree(final double onServe, final double returnServe)
	{
		final double p = onServe;
		final double q = returnServe;
		return pow(set(p, q), 2) * (1 + 2 * (1 - set(p, q)));
	}

	public double bestOfFive(final double onServe, final double returnServe)
	{
		final double p = onServe;
		final double q = returnServe;
		return pow(set(p, q), 3) * (1 + 3 * (1 - set(p, q)) + 6 * (1 - pow(set(p, q), 2)));
	}
}
