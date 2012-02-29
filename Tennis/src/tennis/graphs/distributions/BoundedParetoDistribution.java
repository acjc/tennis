package tennis.graphs.distributions;

import static java.lang.Math.pow;

public class BoundedParetoDistribution
{
	private final double alpha;
	private final double lowerBound;
	private final double upperBound;
	private final double decay;
	private double currentRisk = 0;

	public BoundedParetoDistribution(final double alpha, final double lowerBound, final double upperBound, final double decay)
	{
		this.alpha = alpha;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.decay = decay;
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

	public double sample()
	{
		final double u = Math.random();
		final double x = pow(-((u * pow(upperBound, alpha) - u * pow(lowerBound, alpha) - pow(upperBound, alpha)) / (pow(upperBound, alpha) * pow(lowerBound, alpha))), (-1 / alpha));
		return x;
	}
}
