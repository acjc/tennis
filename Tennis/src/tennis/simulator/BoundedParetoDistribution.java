package tennis.simulator;

import static java.lang.Math.pow;

public class BoundedParetoDistribution
{
	private final double alpha;
	private final double lowerBound;
	private final double upperBound;

	public BoundedParetoDistribution(final double alpha, final double lowerBound, final double upperBound)
	{
		this.alpha = alpha;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public double f(final double x)
	{
		return (alpha * pow(lowerBound, alpha) * pow(x, -alpha - 1)) / (1 - pow(lowerBound / upperBound, alpha));
	}

	public double F(final double x)
	{
		return (1 - pow(lowerBound, alpha) * pow(x, -alpha)) / (1 - pow(lowerBound / upperBound, alpha));
	}

	public double sample()
	{
		final double u = Math.random();
		final double x = pow(-((u * pow(upperBound, alpha) - u * pow(lowerBound, alpha) - pow(upperBound, alpha)) / (pow(upperBound, alpha) * pow(lowerBound, alpha))), (-1 / alpha));
		return x;
	}
}
