package tennis.simulator;

import java.io.FileWriter;
import java.io.IOException;

import tennis.distributions.ProbabilityDistribution;
import tennis.distributions.exp.TruncatedHyperExponentialDistribution;
import au.com.bytecode.opencsv.CSVWriter;

public class SimulatorWRHyperExpRecord extends SimulatorWR
{
	private final double chance;
	private final double lambda;
	private final double decay;
	private final TruncatedHyperExponentialDistribution riskA;
	private final TruncatedHyperExponentialDistribution riskB;
	private final CSVWriter writer;

	public SimulatorWRHyperExpRecord(final double chance, final double lambda, final double decay, final boolean withRetirement) throws IOException
	{
		super(decay, withRetirement);
		this.chance = chance;
		this.lambda = lambda;
		this.decay = decay;
		this.riskA = new TruncatedHyperExponentialDistribution(chance, lambda);
		this.riskB = new TruncatedHyperExponentialDistribution(chance, lambda);
		this.writer = new CSVWriter(new FileWriter("doc\\match.csv"), ',');
	}

	@Override
	public SimulationOutcomes simulate(final double pa, final double pb, final RetirementRisk initialRisk, final MatchState initialState, final boolean isScenario, final double runs)
	{
		this.outcomes = new SimulationOutcomes(runs);
		final long startTime = System.currentTimeMillis();
		for (int i = 0; i < runs; i++)
		{
			// When simulating a particular scenario, we want to replicate the starting conditions exactly
			// Otherwise, start a fresh match with a random first server
			final MatchState result = new MatchState(initialState);
			final RetirementRisk risk = new RetirementRisk(initialRisk.ra, initialRisk.rb);
			if (!isScenario)
			{
				result.coinToss();
			}
			simulateMatch(pa, pb, risk, result);
			outcomes.update(result);
		}
		final long endTime = System.currentTimeMillis();
		outcomes.setSimulationTime((endTime - startTime) / 1000.0);
		try
		{
			writer.close();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		return outcomes;
	}

	@Override
	protected void playPoint(final double pa, final double pb, final RetirementRisk risk, final MatchState score, final boolean serving)
	{
		if (withRetirement)
		{
			if (chance < 0)
			{
				risk.ra = 0;
			}
			else
			{
				risk.ra *= decay;
				risk.ra += riskA.sample();
				risk.ra = risk.ra > 1.0 ? 1.0 : risk.ra;
			}
			if (lambda < 0)
			{
				risk.rb = 0;
			}
			else
			{
				risk.rb *= decay;
				risk.rb += riskB.sample();
				risk.rb = risk.rb > 1.0 ? 1.0 : risk.rb;
			}
		}

		if(risk.ra > 0.03)
		{
			System.out.println(risk.ra);
			if(score.inFirstSet())
			{
				TestSimulatorWRHypExpRecord.GO = false;
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
			TestSimulatorWRHypExpRecord.GO = true;
		}
		else if (point >= p + q + risk.ra)
		{
			score.opponentRetires();
			TestSimulatorWRHypExpRecord.GO = true;
		}

		final String[] entries = {Integer.toString(score.getTargetSets()), Integer.toString(score.getOpponentSets()),
								  Integer.toString(score.getTargetGames()), Integer.toString(score.getOpponentGames()),
								  Integer.toString(score.getTargetPoints()), Integer.toString(score.getOpponentPoints()), serving ? "1" : "0", "0",
							      Double.toString(risk.ra), Double.toString(risk.rb)};
		writer.writeNext(entries);

	}
}
