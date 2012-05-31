package tennis.graphs.distributions.exp;

import static java.lang.Math.*;

import org.apache.commons.math.random.MersenneTwister;

public class TruncatedExponentialDistribution
{
	private final double lambda;

	public TruncatedExponentialDistribution(final double lambda)
	{
		this.lambda = lambda;
	}

	public double f(final double x)
	{
		if (x >= 0)
		{
			return lambda * exp(-lambda * x);
		}
		else
		{
			return 0;
		}
	}

	public double F(final double x)
	{
		return 1 - exp(-lambda * x);
	}

	public double sample()
	{
		final double u = new MersenneTwister().nextDouble();
		double sample = -log(u) / lambda;
		sample = sample - (floor(sample));
		return sample;
	}
}
