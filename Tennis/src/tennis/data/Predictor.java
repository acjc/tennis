package tennis.data;

import java.io.IOException;

import tennis.omalley.OMalley;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.Simulator;

public class Predictor
{
	private static final int RUNS = 500000;
	public static final int NUM_SETS_TO_WIN = 3;
	private static Surface SURFACE = Surface.ALL;

	@SuppressWarnings("unused")
	public static void main(final String[] args) throws IOException
	{
		System.out.println("SURFACE = " + SURFACE + ", " + ((NUM_SETS_TO_WIN == 2) ? "BEST OF THREE" : "BEST OF FIVE"));
		final Player p1 = new Player("Rafael Nadal", SURFACE);
		final Player p2 = new Player("Roger Federer", SURFACE);
		p1.adjustStatistics(p2);
		p2.adjustStatistics(p1);
		final double p = p1.servicePointsWonAgainst(p2);
		final double q = 1 - p2.servicePointsWonAgainst(p1);

		// O'Malley
		final double oMalley;
		if (NUM_SETS_TO_WIN == 3)
		{
			oMalley = OMalley.bestOfFive(p, q) * 100;
		}
		else
		{
			oMalley = OMalley.bestOfThree(p, q) * 100;
		}
		System.out.println("O'Malley says: " + p1.name() + " = " + oMalley + "%, " + p2.name() + " = " + (100 - oMalley) + "%");

		// Simulator
		final SimulationOutcomes outcomes = new Simulator().simulate(p, q, NUM_SETS_TO_WIN, RUNS);
		outcomes.printFinalScorePercentages(NUM_SETS_TO_WIN);
		System.out.println("Simulator says: " + p1.name() + " = " + outcomes.percentageMatchesWon() + "% (Odds = " + outcomes.oddsOfTargetWin() + "), "
						   + p2.name() + " = " + outcomes.percentageMatchesLost() + "% (Odds = " + outcomes.oddsOfOpponentWin() + ")");
	}
}
