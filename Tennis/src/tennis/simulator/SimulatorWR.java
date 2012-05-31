package tennis.simulator;


public abstract class SimulatorWR
{
	protected final double decay;
	protected final boolean withRetirement;
	private SimulationOutcomes outcomes;

	public SimulatorWR(final double decay, final boolean withRetirement)
	{
		this.decay = decay;
		this.withRetirement = withRetirement;
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

	protected abstract void playPoint(final double pa, final double pb, final RetirementRisk risk, final MatchState score, final boolean serving);
}