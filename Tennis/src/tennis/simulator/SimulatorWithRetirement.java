package tennis.simulator;

import java.io.IOException;

import tennis.graphs.distributions.BoundedParetoDistribution;

public class SimulatorWithRetirement
{
	private double runs = 1;
	private final double alpha;
	private final double decay;
	private SimulationOutcomes outcomes;

	public SimulatorWithRetirement(final double alpha, final double decay)
	{
		this.alpha = alpha;
		this.decay = decay;
	}

	public SimulationOutcomes simulate(final double pa, final double pb, final double runs) throws IOException
	{
		return simulate(pa, pb, new MatchState(), false, runs);
	}

	public SimulationOutcomes simulate(final double pa, final double pb, final int numSetsToWin, final double runs) throws IOException
	{
		return simulate(pa, pb, new MatchState(numSetsToWin), false, runs);
	}

	public SimulationOutcomes simulate(final double pa, final double pb, final MatchState initialState, final boolean isScenario, final double runs) throws IOException
	{
		this.runs = runs;
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

	private MatchState simulateMatch(final double pa, final double pb, final MatchState score) throws IOException
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

	private void playTiebreak(final double pa, final double pb, final RetirementRisk risk, final MatchState score) throws IOException
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

	private void playPoint(final double pa, final double pb, final RetirementRisk risk, final MatchState score) throws IOException
	{
		playPoint(pa, pb, risk, score, score.isServingNext());
	}

	private void playPoint(final double pa, final double pb, final RetirementRisk risk, final MatchState score, final boolean serving) throws IOException
	{
		final BoundedParetoDistribution riskA = new BoundedParetoDistribution(alpha, 0.01, 1 - risk.ra, decay);
		final BoundedParetoDistribution riskB = new BoundedParetoDistribution(alpha, 0.01, 1 - risk.rb, decay);
		risk.ra *= decay;
		risk.ra += riskA.sample();
		risk.rb *= decay;
		risk.rb += riskB.sample();

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