package tennis.simulator;

import tennis.graphs.distributions.pareto.BoundedParetoDistribution;

public class SimulatorWRPareto extends SimulatorWR
{
	private final double alphaA;
	private final double alphaB;
	private final double lowerbound = 0.01;
	private final double lowerboundPowAlphaA;
	private final double lowerboundPowAlphaB;
	private final double decay;

	public SimulatorWRPareto(final double alphaA, final double alphaB, final double decay, final boolean withRetirement)
	{
		super(withRetirement);
		this.alphaA = alphaA;
		this.alphaB = alphaB;
		this.decay = decay;
		this.lowerboundPowAlphaA = Math.pow(lowerbound, alphaA);
		this.lowerboundPowAlphaB = Math.pow(lowerbound, alphaB);
	}

	public SimulatorWRPareto(final double alpha, final double decay, final boolean withRetirement)
	{
		this(alpha, alpha, decay, withRetirement);
	}

	@Override
	protected void playPoint(final double pa, final double pb, final RetirementRisk risk, final MatchState score, final boolean serving)
	{
		if (withRetirement)
		{
			if (alphaA >= 500)
			{
				risk.ra = 0;
			}
			else
			{
				final BoundedParetoDistribution riskA = new BoundedParetoDistribution(alphaA, lowerbound, 1 - risk.ra, decay, lowerboundPowAlphaA);
				risk.ra *= decay;
				risk.ra += riskA.sample();
			}
			if (alphaB >= 500)
			{
				risk.rb = 0;
			}
			else
			{
				final BoundedParetoDistribution riskB = new BoundedParetoDistribution(alphaB, lowerbound, 1 - risk.rb, decay, lowerboundPowAlphaB);
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

		final double point = Math.random();
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
