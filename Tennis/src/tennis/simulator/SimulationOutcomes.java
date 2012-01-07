package tennis.simulator;

import java.text.DecimalFormat;

public class SimulationOutcomes
{
	private final double runs;
	private final double[][] scores = new double[4][4];
	private double targetMatchesWon = 0;

	public SimulationOutcomes(final double runs)
	{
		this.runs = runs;
	}

	public double oddsOfScore(final int i, final int j)
	{
		return round(runs / scores[i][j]);
	}

	public double percentageWithScore(final int i, final int j)
	{
		return round((scores[i][j] / runs) * 100);
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

	public double oddsOfTargetWin()
	{
		return round(runs / targetMatchesWon);
	}

	public double oddsOfOpponentWin()
	{
		return round(runs / (runs - targetMatchesWon));
	}

	public void incrementFinalScore(final MatchState matchState)
	{
		scores[matchState.getTargetSets()][matchState.getOpponentSets()]++;
	}

	public void incrementTargetMatchesWon()
	{
		targetMatchesWon++;
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.#####").format(value));
	}

	public void printFinalScorePercentages(final int numSetsToWin)
	{
		for (int i = 0; i <= numSetsToWin; i++)
		{
			for (int j = 0; j <= numSetsToWin; j++)
			{
				if ((i == numSetsToWin ^ j == numSetsToWin))
				{
					System.out.println("Score (" + i + "-" + j + "): " + percentageWithScore(i, j) + "%, Odds = " + oddsOfScore(i, j));
				}
			}
		}
	}
}
