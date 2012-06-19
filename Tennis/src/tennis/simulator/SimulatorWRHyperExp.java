package tennis.simulator;

import tennis.distributions.ProbabilityDistribution;
import tennis.distributions.exp.TruncatedHyperExponentialDistribution;

public class SimulatorWRHyperExp extends SimulatorWR
{
	private final double lambda;
	private final TruncatedHyperExponentialDistribution riskA;
	private final TruncatedHyperExponentialDistribution riskB;

	public SimulatorWRHyperExp(final double chance, final double lambda, final double decay, final boolean withRetirement)
	{
		super(decay, withRetirement);
		this.lambda = lambda;
		this.riskA = new TruncatedHyperExponentialDistribution(chance, lambda);
		this.riskB = new TruncatedHyperExponentialDistribution(chance, lambda);
	}

	@Override
	protected void playPoint(final double pa, final double pb, final RetirementRisk risk, final MatchState score, final boolean serving)
	{
		if (withRetirement)
		{
			if (lambda < 0)
			{
				risk.ra = 0;
			}
			else
			{
				risk.ra *= decay;
				risk.ra += riskA.sample();
			}
			if (lambda < 0)
			{
				risk.rb = 0;
			}
			else
			{
				risk.rb *= decay;
				risk.rb += riskB.sample();
			}
		}

//		if(risk.ra > 0.03 && score.inFirstSet())
//		{
//			System.out.println(risk.ra);
//		}

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
