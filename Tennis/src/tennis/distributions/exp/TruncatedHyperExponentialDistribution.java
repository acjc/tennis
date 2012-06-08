package tennis.distributions.exp;

import static java.lang.Math.exp;
import static java.lang.Math.log;
import tennis.distributions.ProbabilityDistribution;

public class TruncatedHyperExponentialDistribution extends ProbabilityDistribution
{
	private final double chance;
	private final double lambda;

	public TruncatedHyperExponentialDistribution(final double chance, final double lambda)
	{
		this.chance = chance;
		this.lambda = lambda;
	}

	@Override
	public double F(final double x)
	{
		return (1 - exp(-lambda * x)) / (1 - exp(-lambda));
	}

	@Override
	public double sample()
	{
		final double u1 = twister.nextDouble();
		final double u2 = twister.nextDouble();
		double sample = 0;
		if(u1 < chance)
		{
			sample = (-log(u2) / lambda);
		}
		sample -= Math.floor(sample);
		return sample;
	}
}
