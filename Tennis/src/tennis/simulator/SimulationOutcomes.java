package tennis.simulator;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import tennis.graphs.PredictionChart;
import tennis.graphs.distributions.BoundedParetoDistribution;

public class SimulationOutcomes
{
	private final double runs;

	private final double[][] matchScores = new double[4][4];
	private final double[][] setScores = new double[8][8];

	private double targetMatchesWon = 0;
	private double targetRetirements = 0;
	private double opponentRetirements = 0;
	private double targetRetirementsFirstSet = 0;
	private double opponentRetirementsFirstSet = 0;

	private double numDeuces = 0;
	private double totalPointsPostDeuce = 0;
	private double numTiebreaks = 0;
	private double totalTiebreakPoints = 0;
	private double setsPlayed = 0;
	private double simulationTime;

	private final List<Double> targetPrediction = new ArrayList<Double>();
	private final List<Double> oneBallTargetInjuryPrediction = new ArrayList<Double>();
	private final List<Double> oneSetTargetInjuryPrediction = new ArrayList<Double>();
	private final List<Double> twoSetsTargetInjuryPrediction = new ArrayList<Double>();
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
		return setsPlayed == 0 ? 0 : round((setScores[i][j] / setsPlayed) * 100);
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
		return round((1 - proportionMatchesWon() - proportionTargetRetirements() - proportionOpponentRetirements()) * 100);
	}

	public double proportionTargetRetirements()
	{
		return round(targetRetirements / runs);
	}

	public double percentageTargetRetirements()
	{
		return round(proportionTargetRetirements() * 100);
	}

	public double proportionOpponentRetirements()
	{
		return round(opponentRetirements / runs);
	}

	public double percentageOpponentRetirements()
	{
		return round(proportionOpponentRetirements() * 100);
	}

	public double proportionTargetRetirementsFirstSet()
	{
		return round(targetRetirementsFirstSet / runs);
	}

	public double percentageTargetRetirementsFirstSet()
	{
		return round(proportionTargetRetirementsFirstSet() * 100);
	}

	public double proportionOpponentRetirementsFirstSet()
	{
		return round(opponentRetirementsFirstSet / runs);
	}

	public double percentageOpponentRetirementsFirstSet()
	{
		return round(proportionOpponentRetirementsFirstSet() * 100);
	}

	public double percentageTiebreaksPlayed()
	{
		return setsPlayed == 0 ? 0 : round((numTiebreaks / setsPlayed) * 100);
	}

	public double oddsOfTiebreak()
	{
		return numTiebreaks == 0 ? 0 : round(setsPlayed / numTiebreaks);
	}

	public double oddsOfTargetWin()
	{
		return targetMatchesWon == 0 ? 0 : round(runs / targetMatchesWon);
	}

	public double oddsOfOpponentWin()
	{
		final double opponentMatchesWon = runs - targetMatchesWon - targetRetirements - opponentRetirements;
		return opponentMatchesWon == 0 ? 0 : round(runs / opponentMatchesWon);
	}

	public void update(final MatchState matchState)
	{
		if (matchState.targetRetired())
		{
			targetRetirements++;
			if (matchState.inFirstSet())
			{
				targetRetirementsFirstSet++;
			}
		}
		else if (matchState.opponentRetired())
		{
			opponentRetirements++;
			if (matchState.inFirstSet())
			{
				opponentRetirementsFirstSet++;
			}
		}
		else
		{
			if (matchState.targetWon())
			{
				targetMatchesWon++;
			}

			final int targetSets = matchState.getTargetSets();
			final int opponentSets = matchState.getOpponentSets();

			setsPlayed += targetSets + opponentSets;
			numDeuces += matchState.getNumDeuces();
			totalPointsPostDeuce += matchState.getTotalPointsPostDeuce();
			numTiebreaks += matchState.getNumTiebreaks();
			totalTiebreakPoints += matchState.getTotalTiebreakPoints();

			matchScores[targetSets][opponentSets]++;
			for (int i = 0; i <= 7; i++)
			{
				for (int j = 0; j <= 7; j++)
				{
					setScores[i][j] += matchState.getSetScores(i, j);
				}
			}
		}
	}

	public double avgTiebreakPoints()
	{
		return numTiebreaks == 0 ? 0 : totalTiebreakPoints / numTiebreaks;
	}

	public double avgPointsPostDeuce()
	{
		return numDeuces == 0 ? 0 : totalPointsPostDeuce / numDeuces;
	}

	public void addPrediction(final double p, final double q, final boolean serving, final MatchState matchState) throws IOException
	{
		targetPrediction.add(matchState.getTargetPrediction(p, q, serving));
	}

	public void addInjuryPrediction(final double p, final double q, final boolean serving, final MatchState matchState)
	{
		oneBallTargetInjuryPrediction.add(matchState.getTargetInjuryPrediction(p, q, pareto.getCurrentRisk(), serving));
		if (matchState.setsPlayed() >= 1)
		{
			oneSetTargetInjuryPrediction.add(matchState.getTargetInjuryPrediction(p, q, pareto.getCurrentRisk(), serving));
		}
		else
		{
			oneSetTargetInjuryPrediction.add(matchState.getTargetPrediction(p, q, serving));
		}
		if (matchState.setsPlayed() >= 2)
		{
			twoSetsTargetInjuryPrediction.add(matchState.getTargetInjuryPrediction(p, q, pareto.getCurrentRisk(), serving));
		}
		else
		{
			twoSetsTargetInjuryPrediction.add(matchState.getTargetPrediction(p, q, serving));
		}
		pareto.spikePercentage();
		pareto.decay();
	}

	public PredictionChart targetOneBallPredictionChart() throws IOException
	{
		return new PredictionChart("One Ball Match Model", targetPrediction, oneBallTargetInjuryPrediction);
	}

	public PredictionChart targetOneSetPredictionChart() throws IOException
	{
		return new PredictionChart("One Set Match Model", targetPrediction, oneSetTargetInjuryPrediction);
	}

	public PredictionChart targetTwoSetsPredictionChart() throws IOException
	{
		return new PredictionChart("Two Set Match Model", targetPrediction, twoSetsTargetInjuryPrediction);
	}

	public void setSimulationTime(final double time)
	{
		simulationTime = time;
	}

	public double getSimulationTime()
	{
		return simulationTime;
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.#####").format(value));
	}

	public void print(final String target, final String opponent, final int numSetsToWin)
	{
		System.out.println("Simulator says: " + target + " = " + percentageMatchesWon() + "% (Odds = " + oddsOfTargetWin() + "), "
						   + opponent + " = " + percentageMatchesLost() + "% (Odds = " + oddsOfOpponentWin() + "), "
						   + target + " retirements = " + percentageTargetRetirements() + "%, "+ opponent + " retirements = " + percentageOpponentRetirements() + "%, "
						   + target + " retirements (1st set) = " + percentageTargetRetirementsFirstSet() + "%, "+ opponent + " retirements (1st set) = " + percentageOpponentRetirementsFirstSet() + "%");
		System.out.println("Time to simulate " + runs + " runs = " + simulationTime + " seconds");
		System.out.println("\nPOSSIBLE MATCH SCORES:");
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
		System.out.println("\nPOSSIBLE SET SCORES:");
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
		System.out.println("Total tiebreaks played = " + numTiebreaks + ", Total sets played = " + setsPlayed);
		System.out.println("Chance of tiebreak: " + percentageTiebreaksPlayed() + "%, Odds = " + oddsOfTiebreak());
	}
}
