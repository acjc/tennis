package tennis.distributions;

import org.apache.commons.math.random.MersenneTwister;

public abstract class ProbabilityDistribution
{
	public static MersenneTwister twister = new MersenneTwister();

	protected abstract double F(double x);

	protected abstract double sample();
}
