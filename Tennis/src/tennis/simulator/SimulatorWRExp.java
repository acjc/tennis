package tennis.simulator;

import tennis.distributions.ProbabilityDistribution;
import tennis.distributions.exp.TruncatedExponentialDistribution;

public class SimulatorWRExp extends SimulatorWR
{
	private final double lambdaA;
	private final double lambdaB;
	private final TruncatedExponentialDistribution riskA;
	private final TruncatedExponentialDistribution riskB;

	public SimulatorWRExp(final double lambdaA, final double lambdaB, final double decay, final boolean withRetirement)
	{
		super(decay, withRetirement);
		this.lambdaA = lambdaA;
		this.lambdaB = lambdaB;
		this.riskA = new TruncatedExponentialDistribution(lambdaA);
		this.riskB = new TruncatedExponentialDistribution(lambdaB);
	}

	public SimulatorWRExp(final double lambda, final double decay, final boolean withRetirement)
	{
		this(lambda, lambda, decay, withRetirement);
	}

	@Override
	protected void playPoint(final double pa, final double pb, final RetirementRisk risk, final MatchState score, final boolean serving)
	{
		if (withRetirement)
		{
			if (lambdaA < 0)
			{
				risk.ra = 0;
			}
			else
			{
				risk.ra *= decay;
				risk.ra += riskA.sample();
			}
			if (lambdaB < 0)
			{
				risk.rb = 0;
			}
			else
			{
				risk.rb *= decay;
				risk.rb += riskB.sample();
			}
		}

		// p = probability target player wins this point, q = probability target player loses this point
		double p, q;
		if(serving)
		{
			p = pa / (1 + risk.ra + risk.rb);
			q = (1 - pa) / (1 + risk.ra + risk.rb);
		}
		else
		{
			p = (1 - pb) / (1 + risk.ra + risk.rb);
			q = pb / (1 + risk.ra + risk.rb);
		}

		final double point = ProbabilityDistribution.twister.nextDouble();
		if (point < p)
		{
			score.incrementTarget();
		}
		else if (point >= p && point < p + q)
		{
			score.incrementOpponent();
		}
		else if (point >= p + q && point < p + q + risk.ra)
		{
			score.targetRetires();
		}
		else if (point >= p + q + risk.ra)
		{
			score.opponentRetires();
		}
	}
}
