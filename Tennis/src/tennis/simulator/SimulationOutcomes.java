package tennis.simulator;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tennis.charts.PredictionChart;
import tennis.distributions.BoundedParetoDistribution;

public class SimulationOutcomes
{
	private final double runs;
	private final double[][] matchScores = new double[4][4];
	private final double[][] setScores = new double[8][8];
	private double targetMatchesWon = 0;
	private double tiebreaks = 0;
	private double setsPlayed = 0;
	private double simulationTime;

	private final List<Double> targetPrediction = new ArrayList<Double>();
	private final List<Double> targetInjuryPrediction = new ArrayList<Double>();
	final BoundedParetoDistribution pareto = new BoundedParetoDistribution(0.85, 0.01, 100, 0.95);

	public SimulationOutcomes(final double runs)
	{
		this.runs = runs;
	}

	public double oddsOfMatchScore(final int i, final int j)
	{
		return matchScores[i][j] == 0 ? 0 : round(runs / matchScores[i][j]);
	}

	public double percentageWithMatchScore(final int i, final int j)
	{
		return round((matchScores[i][j] / runs) * 100);
	}

	public double oddsOfSetScore(final int i, final int j)
	{
		return setScores[i][j] == 0 ? 0 : round(setsPlayed / setScores[i][j]);
	}

	public double percentageWithSetScore(final int i, final int j)
	{
		return round((setScores[i][j] / setsPlayed) * 100);
	}

	public double proportionMatchesWon()
	{
		return round(targetMatchesWon / runs);
	}

	public double percentageMatchesWon()
	{
		return round(proportionMatchesWon() * 100);
	}

	public double percentageMatchesLost()
	{
		return round((1 - proportionMatchesWon()) * 100);
	}

	public double percentageTiebreaksPlayed()
	{
		return round((tiebreaks / setsPlayed) * 100);
	}

	public double oddsOfTiebreak()
	{
		return tiebreaks == 0 ? 0 : round(setsPlayed / tiebreaks);
	}

	public double oddsOfTargetWin()
	{
		return targetMatchesWon == 0 ? 0 : round(runs / targetMatchesWon);
	}

	public double oddsOfOpponentWin()
	{
		final double opponentMatchesWon = runs - targetMatchesWon;
		return opponentMatchesWon == 0 ? 0 : round(runs / opponentMatchesWon);
	}

	public void update(final MatchState matchState)
	{
		final int targetSets = matchState.getTargetSets();
		final int opponentSets = matchState.getOpponentSets();

		setsPlayed += targetSets + opponentSets;
		tiebreaks += matchState.tiebreaks();

		matchScores[targetSets][opponentSets]++;
		for (int i = 0; i <= 7; i++)
		{
			for (int j = 0; j <= 7; j++)
			{
				setScores[i][j] += matchState.getSetScores(i, j);
			}
		}

		if (matchState.targetWon())
		{
			targetMatchesWon++;
		}
	}

	public void addPrediction(final double p, final double q, final boolean serving, final MatchState matchState) throws IOException
	{
		targetPrediction.add(matchState.getTargetPrediction(p, q, serving));
	}

	public void addInjuryPrediction(final double p, final double q, final boolean serving, final MatchState matchState)
	{
		targetInjuryPrediction.add(matchState.getTargetInjuryPrediction(p, q, pareto.getCurrentRisk(), serving));
		pareto.spike();
		pareto.decay();
	}

	public PredictionChart targetPredictionChart() throws IOException
	{
		return new PredictionChart(targetPrediction, targetInjuryPrediction);
	}

	public void setSimulationTime(final double time)
	{
		simulationTime = time;
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.#####").format(value));
	}

	public void print(final String target, final String opponent, final int numSetsToWin)
	{
		System.out.println("Simulator says: " + target + " = " + percentageMatchesWon() + "% (Odds = " + oddsOfTargetWin() + "), "
						   + opponent + " = " + percentageMatchesLost() + "% (Odds = " + oddsOfOpponentWin() + ")");
		System.out.println("Time to simulate " + runs + " runs = " + simulationTime + " seconds");
		System.out.println("POSSIBLE MATCH SCORES:");
		for (int i = 0; i <= numSetsToWin; i++)
		{
			for (int j = 0; j <= numSetsToWin; j++)
			{
				if ((i == numSetsToWin ^ j == numSetsToWin))
				{
					System.out.println("Match Score (" + i + "-" + j + "): " + percentageWithMatchScore(i, j) + "%, Odds = " + oddsOfMatchScore(i, j));
				}
			}
		}
		System.out.println("POSSIBLE SET SCORES:");
		for (int i = 0; i <= 7; i++)
		{
			for (int j = 0; j <= 7; j++)
			{
				if ((Math.abs(i - j) >= 2 && (i == 6 ^ j == 6))
				    || (Math.abs(i - j) <= 2 && (i == 7 ^ j == 7)))
				{
					System.out.println("Set Score (" + i + "-" + j + "): " + percentageWithSetScore(i, j) + "%, Odds = " + oddsOfSetScore(i, j));
				}
			}
		}
		System.out.println("Total tiebreaks played = " + tiebreaks + ", Total sets played = " + setsPlayed);
		System.out.println("Chance of tiebreak: " + percentageTiebreaksPlayed() + "%, Odds = " + oddsOfTiebreak());
	}
}
