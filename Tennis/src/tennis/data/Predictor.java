package tennis.data;

import java.io.IOException;

import tennis.omalley.OMalley;
import tennis.simulator.Outcomes;
import tennis.simulator.Simulator;

public class Predictor
{
	private static final int RUNS = 200000;

	public static void main(final String[] args) throws IOException
	{
		final Player p1 = new Player("Roger Federer");
		final Player p2 = new Player("Jo-Wilfried Tsonga");
		p1.adjustStatistics(p2);
		p2.adjustStatistics(p1);
		final double p = p1.servicePointsWonAgainst(p2);
		final double q = 1 - p2.servicePointsWonAgainst(p1);

		// O'Malley
		final double bestOfFive = OMalley.bestOfFive(p, q) * 100;
		System.out.println("O'Malley says: " + p1.name() + " = " + bestOfFive + "%, " + p2.name() + " = " + (100 - bestOfFive) + "%");

		// Simulator
		final Outcomes outcomes = new Simulator().simulate(p, q, RUNS);
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				if ((i == 3 ^ j == 3))
				{
					System.out.println("Score (" + i + "-" + j + "): Proportion = " + outcomes.percentageWithScore(i, j) + "%, Odds = " + outcomes.oddsOfScore(i, j));
				}
			}
		}
		System.out.println("Simulator says: " + p1.name() + " = " + outcomes.percentageMatchesWon() + "% (Odds = " + outcomes.oddsOfTargetWin() + "), "
						   + p2.name() + " = " + outcomes.percentageMatchesLost() + "% (Odds = " + outcomes.oddsOfOpponentWin() + ")");
	}
}
