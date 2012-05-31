package tennis.distributions.exp;

import static java.lang.Math.*;
import tennis.distributions.ProbabilityDistribution;

public class TruncatedExponentialDistribution extends ProbabilityDistribution
{
	private final double lambda;

	public TruncatedExponentialDistribution(final double lambda)
	{
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
		final double u = twister.nextDouble();
		double sample = -log(u) / lambda;
		sample -= floor(sample);
		return sample;
	}
}
