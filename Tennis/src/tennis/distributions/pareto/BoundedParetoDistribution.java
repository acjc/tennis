package tennis.distributions.pareto;

import static java.lang.Math.pow;
import tennis.distributions.ProbabilityDistribution;

public class BoundedParetoDistribution extends ProbabilityDistribution
{
	private final double alpha;
	private final double lowerBound = 1.0;
	private final double upperBound;
	private final double upperboundPowAlpha;
	private final double decay;

	private double currentRisk = 0;

	public BoundedParetoDistribution(final double alpha, final double upperBound, final double decay)
	{
		this.alpha = alpha;
		this.upperBound = upperBound;
		this.decay = decay;
		upperboundPowAlpha = Math.pow(upperBound, alpha);
	}

	public BoundedParetoDistribution(final double alpha, final double decay)
	{
		this(alpha, 2.0, decay);
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
		return (alpha * pow(x, -alpha - 1)) / (1 - pow(lowerBound / upperBound, alpha));
	}

	@Override
	public double F(final double x)
	{
		return (1 - pow(x, -alpha)) / (1 - pow(lowerBound / upperBound, alpha));
	}

	@Override
	public double sample()
	{
		final double u = twister.nextDouble();
//		final double upperboundPowAlpha = Math.exp(alpha * Math.log(upperBound));
		double sample = pow(-(u - (u / upperboundPowAlpha) - 1), (-1 / alpha));
//		double sample = Math.exp((-1 / alpha) * Math.log(-(u - (u / upperboundPowAlpha) - 1)));

		// Linearly shift to be between 0 and the upper bound
		sample -= lowerBound;
		return sample < 0 ? 0 : sample;
	}
}
