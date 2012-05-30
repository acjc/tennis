package tennis.simulator;

import tennis.graphs.distributions.BoundedParetoDistribution;

public class SimulatorWR
{
	private final double alphaA;
	private final double alphaB;
	private final double lowerbound = 0.01;
	private final double lowerboundPowAlphaA;
	private final double lowerboundPowAlphaB;
	private final double decay;
	private final boolean withRetirement;
	private SimulationOutcomes outcomes;

	public SimulatorWR(final double alphaA, final double alphaB, final double decay, final boolean withRetirement)
	{
		this.alphaA = alphaA;
		this.alphaB = alphaB;
		this.decay = decay;
		this.withRetirement = withRetirement;
		this.lowerboundPowAlphaA = Math.pow(lowerbound, alphaA);
		this.lowerboundPowAlphaB = Math.pow(lowerbound, alphaB);
	}

	public SimulatorWR(final double alpha, final double decay, final boolean withRetirement)
	{
		this(alpha, alpha, decay, withRetirement);
	}

	public SimulationOutcomes simulate(final double pa, final double pb, final double runs)
	{
		return simulate(pa, pb, new MatchState(), false, runs);
	}

	public SimulationOutcomes simulate(final double pa, final double pb, final int numSetsToWin, final double runs)
	{
		return simulate(pa, pb, new MatchState(numSetsToWin), false, runs);
	}

	public SimulationOutcomes simulate(final double pa, final double pb, final MatchState initialState, final boolean isScenario, final double runs)
	{
		this.outcomes = new SimulationOutcomes(runs);
		final long startTime = System.currentTimeMillis();
		for (int i = 0; i < runs; i++)
		{
			// When simulating a particular scenario, we want to replicate the starting conditions exactly
			// Otherwise, start a fresh match with a random first server
			final MatchState result = new MatchState(initialState);
			if (!isScenario)
			{
				result.coinToss();
			}
			simulateMatch(pa, pb, result);
			outcomes.update(result);
		}
		final long endTime = System.currentTimeMillis();
		outcomes.setSimulationTime((endTime - startTime) / 1000.0);
		return outcomes;
	}

	private MatchState simulateMatch(final double pa, final double pb, final MatchState score)
	{
		final RetirementRisk risk = new RetirementRisk();
		while (!score.over())
		{
			while (!score.setOver())
			{
				while (!score.gameOver())
				{
					playPoint(pa, pb, risk, score);
				}
				if (score.tiebreak()) // Assume tiebreaks are always used for now
				{
					playTiebreak(pa, pb, risk, score);
				}
			}
		}
		return score;
	}

	private void playTiebreak(final double pa, final double pb, final RetirementRisk risk, final MatchState score)
	{
		// Whomever serves first is the server for this 'game'
		boolean servingNext = score.isServingNext();
		while (!score.tiebreakOver())
		{
			playPoint(pa, pb, risk, score, servingNext);
			if (score.isOddPoint()) // Service changes every odd point
			{
				servingNext = !servingNext;
			}
		}
	}

	private void playPoint(final double pa, final double pb, final RetirementRisk risk, final MatchState score)
	{
		playPoint(pa, pb, risk, score, score.isServingNext());
	}

	private void playPoint(final double pa, final double pb, final RetirementRisk risk, final MatchState score, final boolean serving)
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

	private class RetirementRisk
	{
		public double ra = 0;
		public double rb = 0;
	}
}