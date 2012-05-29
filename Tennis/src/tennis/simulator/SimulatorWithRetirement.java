package tennis.simulator;

import java.io.IOException;

import tennis.graphs.distributions.BoundedParetoDistribution;

public class SimulatorWithRetirement
{
	private double runs = 1;
	private final double decay = 0.95;
	private SimulationOutcomes outcomes;

	public SimulationOutcomes simulate(final double p, final double q, final double runs) throws IOException
	{
		return simulate(p, q, new MatchState(), false, runs);
	}

	public SimulationOutcomes simulate(final double p, final double q, final int numSetsToWin, final double runs) throws IOException
	{
		return simulate(p, q, new MatchState(numSetsToWin), false, runs);
	}

	public SimulationOutcomes simulate(final double p, final double q, final MatchState initialState, final boolean isScenario, final double runs) throws IOException
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
			simulateMatch(p, q, result);
			outcomes.update(result);
		}
		final long endTime = System.currentTimeMillis();
		outcomes.setSimulationTime((endTime - startTime) / 1000.0);
		return outcomes;
	}

	private MatchState simulateMatch(final double p, final double q, final MatchState score) throws IOException
	{
		final RetirementRisk risk = new RetirementRisk();
		while (!score.matchOver())
		{
			while (!score.setOver())
			{
				while (!score.gameOver())
				{
					playPoint(p, q, risk, score);
				}
				if (score.tiebreak()) // Assume tiebreaks are always used for now
				{
					playTiebreak(p, q, risk, score);
				}
			}
		}
		return score;
	}

	private void playTiebreak(final double p, final double q, final RetirementRisk risk, final MatchState score) throws IOException
	{
		// Whomever serves first is the server for this 'game'
		boolean servingNext = score.isServingNext();
		while (!score.tiebreakOver())
		{
			playPoint(p, q, risk, score, servingNext);
			if (score.isOddPoint()) // Service changes every odd point
			{
				servingNext = !servingNext;
			}
		}
	}

	private void playPoint(final double p, final double q, final RetirementRisk risk, final MatchState score) throws IOException
	{
		playPoint(p, q, risk, score, score.isServingNext());
	}

	private void playPoint(double p, double q, final RetirementRisk risk, final MatchState score, final boolean serving) throws IOException
	{
		final BoundedParetoDistribution riskA = new BoundedParetoDistribution(1.0, 0.01, 1 - risk.ra, decay);
		final BoundedParetoDistribution riskB = new BoundedParetoDistribution(1.0, 0.01, 1 - risk.rb, decay);
		risk.ra *= decay;
		risk.ra += riskA.sample();
		risk.rb *= decay;
		risk.rb += riskB.sample();

		p = p / (1 + risk.ra + risk.rb);
		q = q / (1 + risk.ra + risk.rb);

		final double point = Math.random();
		if ((serving && point < p) || (!serving && point < p && point < p + q))
		{
			score.incrementTarget();
		}
		else if(((serving && point > p && point < p + q) || (!serving && point < p && point < p + q)))
		{
			score.incrementOpponent();
		}
		if (runs == 1)
		{
			outcomes.addPrediction(p, q, serving, score);
			outcomes.addInjuryPrediction(p, q, serving, score);
		}
	}

	private class RetirementRisk
	{
		public double ra = 0;
		public double rb = 0;
	}
}