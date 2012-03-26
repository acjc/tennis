package tennis.omalley;

import java.io.IOException;

import org.junit.Test;

import tennis.simulator.MatchState;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.Simulator;

public class TestRetirementParameters
{
	final double p = 0.55;
	final double q = 0.46;
	final int targetSets = 0;
	final int opponentSets = 0;
	final int targetGames = 3;
	final int opponentGames = 4;
	final double retirementRisk = 0.2;

	@Test
	public void pointsTest() throws IOException
	{
		final SimulationOutcomes outcome = new Simulator().simulate(p, q, new MatchState(), false, 100000);
		System.out.println("Average Tiebreak Points: " + outcome.avgTiebreakPoints());
		System.out.println("Average Points Post Deuce: " + outcome.avgPointsPostDeuce());

		System.out.println("Points in a game: " + OMalleyCount.gameInProgressCount(0.55, new CurrentGameScore(), 0, 0).points);
		System.out.println("Points in a set: " + OMalleyCount.setInProgressCount(0.55, 0.46, new CurrentSetScore(), new CurrentGameScore(), true, 0, 0).points);
	}

	@Test
	public void recursionTest()
	{
		System.out.println("Recursion levels in a game: " + OMalleyCount.gameInProgressCount(0.55, new CurrentGameScore(), 0, 0).levels);
		System.out.println("Recursion levels in a set: " + OMalleyCount.setInProgressCount(0.55, 0.46, new CurrentSetScore(), new CurrentGameScore(), true, 0, 0).levels);
	}

	@Test
	public void reverseEngineerPoints()
	{
		final double expectedPoints = OMalleyCount.matchInProgressCount(p, q, new CurrentMatchScore(),
																						new CurrentSetScore(),
																						new CurrentGameScore(),
																				  (Math.random() < 0.5) ? true : false, 3, 0, 0).points;
		System.out.println("Expected Points: " + expectedPoints);

		System.out.println("MWP (Start): " + OMalley.matchInProgress(p, q, new CurrentMatchScore(targetSets, opponentSets),
				 														   new CurrentSetScore(),
				 														   new CurrentGameScore(), true, 3));

		System.out.println("MWP (Mid): " + OMalley.matchInProgress(p, q, new CurrentMatchScore(targetSets, opponentSets),
																		 new CurrentSetScore(targetGames, opponentGames),
																		 new CurrentGameScore(), true, 3));


		final double pointsRemainingStart = OMalleyCount.matchInProgressCount(p, q, new CurrentMatchScore(targetSets, opponentSets),
																						 	  new CurrentSetScore(),
																						 	  new CurrentGameScore(), true, 3, 0, 0).points;
		System.out.println("Points Remaining (Start): " + pointsRemainingStart);

		System.out.println("Adjusted MWP (Start): " + OMalleyWithRetirement.matchInProgressWithRetirement(p, q, new CurrentMatchScore(targetSets, opponentSets),
			          																						    new CurrentSetScore(),
			          																						    new CurrentGameScore(),
			          																				      true, 3, retirementRisk, pointsRemainingStart, expectedPoints));

		System.out.println(OMalleyWithRetirement.levels);

		final double pointsRemainingMid = OMalleyCount.matchInProgressCount(p, q, new CurrentMatchScore(targetSets, opponentSets),
																						    new CurrentSetScore(targetGames, opponentGames),
																						    new CurrentGameScore(), true, 3, 0, 0).points;
		System.out.println("Points Remaining (Mid): " + pointsRemainingMid);

		System.out.println("Adjusted MWP (Mid): " + OMalleyWithRetirement.matchInProgressWithRetirement(p, q, new CurrentMatchScore(targetSets, opponentSets),
																						   			          new CurrentSetScore(targetGames, opponentGames),
																						   			          new CurrentGameScore(),
																						   				true, 3, retirementRisk, pointsRemainingMid, expectedPoints));
	}

	@Test
	public void reverseEngineerRecursionLevels()
	{
		final double expectedRecursionLevels = OMalleyCount.matchInProgressCount(p, q, new CurrentMatchScore(),
																						new CurrentSetScore(),
																						new CurrentGameScore(),
																				  (Math.random() < 0.5) ? true : false, 3, 0, 0).levels;
		System.out.println("Expected Recursion Levels: " + expectedRecursionLevels);

		System.out.println("MWP (Start): " + OMalley.matchInProgress(p, q, new CurrentMatchScore(targetSets, opponentSets),
				 														   new CurrentSetScore(),
				 														   new CurrentGameScore(), true, 3));

		System.out.println("MWP (Mid): " + OMalley.matchInProgress(p, q, new CurrentMatchScore(targetSets, opponentSets),
																		 new CurrentSetScore(targetGames, opponentGames),
																		 new CurrentGameScore(), true, 3));


		final double recursionLevelsRemainingStart = OMalleyCount.matchInProgressCount(p, q, new CurrentMatchScore(targetSets, opponentSets),
																						 	  new CurrentSetScore(),
																						 	  new CurrentGameScore(), true, 3, 0, 0).levels;
		System.out.println("Recursion Levels Remaining (Start): " + recursionLevelsRemainingStart);

		System.out.println("Adjusted MWP (Start): " + OMalleyWithRetirement.matchInProgressWithRetirement(p, q, new CurrentMatchScore(targetSets, opponentSets),
			          																						    new CurrentSetScore(),
			          																						    new CurrentGameScore(),
			          																				      true, 3, retirementRisk, recursionLevelsRemainingStart, expectedRecursionLevels));

		System.out.println(OMalleyWithRetirement.levels);

		final double recursionLevelsRemainingMid = OMalleyCount.matchInProgressCount(p, q, new CurrentMatchScore(targetSets, opponentSets),
																						    new CurrentSetScore(targetGames, opponentGames),
																						    new CurrentGameScore(), true, 3, 0, 0).levels;
		System.out.println("Recursion Levels Remaining (Mid): " + recursionLevelsRemainingMid);

		System.out.println("Adjusted MWP (Mid): " + OMalleyWithRetirement.matchInProgressWithRetirement(p, q, new CurrentMatchScore(targetSets, opponentSets),
																						   			          new CurrentSetScore(targetGames, opponentGames),
																						   			          new CurrentGameScore(),
																						   				true, 3, retirementRisk, recursionLevelsRemainingMid, expectedRecursionLevels));
	}
}
