package tennis.graphs.distributions;

import static java.lang.Math.pow;

public class BoundedParetoDistribution
{
	private final double alpha;
	private final double lowerBound;
	private final double upperBound;
	private final double decay;
	private final double lowerboundPowAlpha;

	private double currentRisk = 0;

	public BoundedParetoDistribution(final double alpha, final double lowerBound, final double upperBound, final double decay, final double lowerboundPowAlpha)
	{
		this.alpha = alpha;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.decay = decay;
		this.lowerboundPowAlpha = lowerboundPowAlpha;
	}

	public double getDecay()
	{
		return decay;
	}

	public double getCurrentRisk()
	{
		return currentRisk;
	}

	public void spike()
	{
		currentRisk += sample();
	}

	public void spikePercentage()
	{
		currentRisk += sample() / 100.0;
	}

	public void decay()
	{
		currentRisk *= decay;
	}

	public double f(final double x)
	{
		return (alpha * pow(lowerBound, alpha) * pow(x, -alpha - 1)) / (1 - pow(lowerBound / upperBound, alpha));
	}

	public double F(final double x)
	{
		return (1 - pow(lowerBound, alpha) * pow(x, -alpha)) / (1 - pow(lowerBound / upperBound, alpha));
	}

	public static double fastPow(final double a, final double b) {
	    final long temp = Double.doubleToLongBits(a);
	    final long result = (long)(b * (temp - 4606921280493453312L)) + 4606921280493453312L;
	    return Double.longBitsToDouble(result);
	}

	public double sample()
	{
		final double u = Math.random();
		final double upperboundPowAlpha = fastPow(upperBound, alpha);
		final double x = pow(-((u * upperboundPowAlpha - (u * lowerboundPowAlpha) - upperboundPowAlpha) / (upperboundPowAlpha * lowerboundPowAlpha)), (-1 / alpha));

		// Linearly shift and scale to be between 0 and the upper bound
		return (x - lowerBound) * (upperBound / (upperBound - lowerBound));
	}
}
