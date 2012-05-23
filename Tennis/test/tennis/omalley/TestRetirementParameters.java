package tennis.omalley;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.text.DecimalFormat;

import org.junit.Test;

import tennis.omalley.OMalleyCount.MatchAnalysis;
import tennis.simulator.MatchState;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.Simulator;

public class TestRetirementParameters
{
	final double p = 0.55;
	final double q = 0.46;
	final int targetSets = 1;
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
		System.out.println();
	}

	@Test
	public void recursionLevelsTest()
	{
		System.out.println("Recursion levels in a game: " + OMalleyCount.gameInProgressCount(p, new CurrentGameScore(), 0, 0).levels);
		System.out.println("Recursion levels in a set: " + OMalleyCount.setInProgressCount(p, q, new CurrentSetScore(), new CurrentGameScore(), true, 0, 0).levels);
		System.out.println("Recursion levels in a match: " + OMalleyCount.matchInProgressCount(p, q, new CurrentMatchScore(),
																									 new CurrentSetScore(),
																									 new CurrentGameScore(),
																							   (Math.random() < 0.5) ? true : false, 3, 0, 0).levels);
		System.out.println();
	}

	@Test
	public void endOfsingleGameRecursionLevelsTest()
	{
		final MatchAnalysis analysis = OMalleyCount.gameInProgressCount(p, new CurrentGameScore(3, 2), 0, 0);
		System.out.println("Recursion levels at 40-30: " + analysis.levels);
		System.out.println("MWP at 40-30: " + analysis.mwp);
		final double modifiedMwp = OMalleyWithRetirement.gameInProgressWithRetirement(p, new CurrentGameScore(3, 2), retirementRisk, analysis.levels, analysis.mwp);
		System.out.println("Modified MWP at 40-30: " + modifiedMwp);
		assertThat(round(analysis.mwp - modifiedMwp), equalTo(retirementRisk));
		System.out.println();
	}

	@Test
	public void midSingleGameRecursionLevelsTest()
	{
		final MatchAnalysis analysis = OMalleyCount.gameInProgressCount(p, new CurrentGameScore(1, 2), 0, 0);
		System.out.println("Recursion levels at 15-30: " + analysis.levels);
		System.out.println("MWP at 15-30: " + analysis.mwp);
		final double modifiedMwp = OMalleyWithRetirement.gameInProgressWithRetirement(p, new CurrentGameScore(1, 2), retirementRisk, analysis.levels, analysis.mwp);
		System.out.println("Modified MWP at 15-30: " + modifiedMwp);
		assertThat(round(analysis.mwp - modifiedMwp), equalTo(retirementRisk));
		System.out.println();
	}

	@Test
	public void singleGameRecursionLevelsTest()
	{
		final MatchAnalysis analysis = OMalleyCount.gameInProgressCount(p, new CurrentGameScore(), 0, 0);
		System.out.println("Recursion levels in game: " + analysis.levels);
		System.out.println("MWP for game: " + analysis.mwp);
		final double modifiedMwp = OMalleyWithRetirement.gameInProgressWithRetirement(p, new CurrentGameScore(), retirementRisk, analysis.levels, analysis.mwp);
		System.out.println("Modified MWP for game: " + modifiedMwp);
		assertThat(round(analysis.mwp - modifiedMwp), equalTo(retirementRisk));
		System.out.println();
	}

	@Test
	public void singleSetRecursionLevelsTest()
	{
		final double setTreeDepth = OMalleyCount.setTreeDepth(p, q, new CurrentSetScore(), new CurrentGameScore(), true, 0) + OMalleyCount.setTreeDepth(p, q, new CurrentSetScore(), new CurrentGameScore(), false, 0);
		System.out.println("Set Tree Depth: " + setTreeDepth);
		final MatchAnalysis analysis = OMalleyCount.setInProgressCount(p, q, new CurrentSetScore(), new CurrentGameScore(), true, 0, 0);
		System.out.println("Recursion levels in a set: " + analysis.levels);
		System.out.println("MWP for set: " + analysis.mwp);
		final double modifiedMwp = OMalleyWithRetirement.setInProgressWithRetirement(p, q, new CurrentSetScore(), new CurrentGameScore(), true, retirementRisk, analysis.levels, analysis.mwp);
		System.out.println("Modified MWP for set: " + modifiedMwp);
		System.out.println("MWP Difference: " + (analysis.mwp - modifiedMwp));
		System.out.println();
	}

	@Test
	public void midSingleSetRecursionLevelsTest()
	{
		final MatchAnalysis analysis = OMalleyCount.setInProgressCount(p, q, new CurrentSetScore(2, 3), new CurrentGameScore(), true, 0, 0);
		System.out.println("Recursion levels at 2-3: " + analysis.levels);
		System.out.println("MWP at 2-3: " + analysis.mwp);
		final double modifiedMwp = OMalleyWithRetirement.setInProgressWithRetirement(p, q, new CurrentSetScore(2, 3), new CurrentGameScore(), true, retirementRisk, analysis.levels, analysis.mwp);
		System.out.println("Modified MWP at 2-3: " + modifiedMwp);
		System.out.println(analysis.mwp - modifiedMwp);
		System.out.println();
	}

	@Test
	public void singleThreeSetMatchRecursionLevelsTest()
	{
		final double matchTreeDepth = OMalleyCount.matchTreeDepth(p, q, new CurrentMatchScore(), new CurrentSetScore(), new CurrentGameScore(), true, 3, 0);
		System.out.println(matchTreeDepth);
		final MatchAnalysis analysis = OMalleyCount.matchInProgressCount(p, q, new CurrentMatchScore(), new CurrentSetScore(), new CurrentGameScore(), true, 3, 0, 0);
		System.out.println("Recursion levels in a match: " + analysis.levels);
		System.out.println("MWP for match: " + analysis.mwp);
		final double modifiedMwp = OMalleyWithRetirement.matchInProgressWithRetirement(p, q, new CurrentMatchScore(), new CurrentSetScore(), new CurrentGameScore(), true, 3, retirementRisk, analysis.levels, analysis.mwp);
		System.out.println("Modified MWP for match: " + modifiedMwp);
		System.out.println(analysis.mwp - modifiedMwp);
		System.out.println();
	}

	@Test
	public void reverseEngineerRecursionLevels()
	{
		final double expectedRecursionLevels = OMalleyCount.matchInProgressCount(p, q, new CurrentMatchScore(),
																					   new CurrentSetScore(),
																					   new CurrentGameScore(),
																			   (Math.random() < 0.5) ? true : false, 3, 0, 0).levels;
		System.out.println("Expected Recursion Levels: " + expectedRecursionLevels);

		final double expectedMwpStart = OMalley.matchInProgress(p, q, new CurrentMatchScore(targetSets, opponentSets),
				 														   new CurrentSetScore(),
				 														   new CurrentGameScore(), true, 3);
		System.out.println("MWP (Start): " + expectedMwpStart);

		final double expectedMwpMid = OMalley.matchInProgress(p, q, new CurrentMatchScore(targetSets, opponentSets),
																		 new CurrentSetScore(targetGames, opponentGames),
																		 new CurrentGameScore(), true, 3);
		System.out.println("MWP (Mid): " + expectedMwpMid);


		final double recursionLevelsRemainingStart = OMalleyCount.matchInProgressCount(p, q, new CurrentMatchScore(targetSets, opponentSets),
																						 	  new CurrentSetScore(),
																						 	  new CurrentGameScore(), true, 3, 0, 0).levels;
		System.out.println("Recursion Levels Remaining (Start): " + recursionLevelsRemainingStart);

		System.out.println("Adjusted MWP (Start): " + OMalleyWithRetirement.matchInProgressWithRetirement(p, q, new CurrentMatchScore(targetSets, opponentSets),
			          																						    new CurrentSetScore(),
			          																						    new CurrentGameScore(),
			          																				      true, 3, retirementRisk, recursionLevelsRemainingStart, expectedMwpStart));

		final double recursionLevelsRemainingMid = OMalleyCount.matchInProgressCount(p, q, new CurrentMatchScore(targetSets, opponentSets),
																						 new CurrentSetScore(targetGames, opponentGames),
																						 new CurrentGameScore(), true, 3, 0, 0).levels;
		System.out.println("Recursion Levels Remaining (Mid): " + recursionLevelsRemainingMid);

		System.out.println("Adjusted MWP (Mid): " + OMalleyWithRetirement.matchInProgressWithRetirement(p, q, new CurrentMatchScore(targetSets, opponentSets),
																						   			          new CurrentSetScore(targetGames, opponentGames),
																						   			          new CurrentGameScore(),
																						   				true, 3, retirementRisk, recursionLevelsRemainingMid, expectedMwpMid));
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.#").format(value));
	}
}
