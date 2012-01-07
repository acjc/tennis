package tennis.simulator;

import java.text.DecimalFormat;

public class Outcomes
{
	private final double runs;
	private final double[][] scores = new double[4][4];
	private double targetMatchesWon = 0;

	public Outcomes(final double runs)
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

	public void incrementScore(final int i, final int j)
	{
		scores[i][j]++;
	}

	public void incrementTargetMatchesWon()
	{
		targetMatchesWon++;
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.#####").format(value));
	}
}
