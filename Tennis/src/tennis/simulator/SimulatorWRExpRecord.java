package tennis.simulator;

import java.io.FileWriter;
import java.io.IOException;

import tennis.distributions.ProbabilityDistribution;
import tennis.distributions.exp.TruncatedExponentialDistribution;
import au.com.bytecode.opencsv.CSVWriter;

public class SimulatorWRExpRecord extends SimulatorWR
{
	private final double lambdaA;
	private final double lambdaB;
	private final TruncatedExponentialDistribution riskA;
	private final TruncatedExponentialDistribution riskB;
	private final CSVWriter writer;

	public SimulatorWRExpRecord(final double lambdaA, final double lambdaB, final double decay, final boolean withRetirement, final CSVWriter writer)
	{
		super(decay, withRetirement);
		this.lambdaA = lambdaA;
		this.lambdaB = lambdaB;
		this.riskA = new TruncatedExponentialDistribution(lambdaA);
		this.riskB = new TruncatedExponentialDistribution(lambdaB);
		this.writer = writer;
	}

	public SimulatorWRExpRecord(final double lambdaA, final double lambdaB, final double decay, final boolean withRetirement) throws IOException
	{
		this(lambdaA, lambdaB, decay, withRetirement, new CSVWriter(new FileWriter("doc\\match.csv"), ','));
	}

	public SimulatorWRExpRecord(final double lambda, final double decay, final boolean withRetirement) throws IOException
	{
		this(lambda, lambda, decay, withRetirement, new CSVWriter(new FileWriter("doc\\match.csv"), ','));
	}

	@Override
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

		final String[] entries = {Integer.toString(score.getTargetSets()), Integer.toString(score.getOpponentSets()),
								  Integer.toString(score.getTargetGames()), Integer.toString(score.getOpponentGames()),
								  Integer.toString(score.getTargetPoints()), Integer.toString(score.getOpponentPoints()),
							      Double.toString(point), Double.toString(risk.ra), Double.toString(risk.rb)};
		writer.writeNext(entries);
	}
}
