package tennis.omalley;

import org.junit.Ignore;
import org.junit.Test;

public class TestOMalleyWR
{
	@Ignore
	@Test
	public void testOneMatchBothPlayersWin()
	{
		final double alpha = 154.62;
		final OMalleyWRWin oMalley = new OMalleyWRWin(alpha, 0.85, true);
		System.out.println("Alpha = " + alpha + ": " + oMalley.matchInProgress(0.6, 0.6, 3));
	}

	@Test
	public void testScenarioOnePlayerWin()
	{
		final double alpha = 53.0;
		final OMalleyWRWin oMalley = new OMalleyWRWin(alpha, 1000.0, 0.85, true);
		System.out.println("Alpha = " + alpha + ": " + oMalley.matchInProgress(0.6, 0.6, new RetirementRisk(), new CurrentMatchScore(1, 0), new CurrentSetScore(1, 2), true, 3));
	}

	@Ignore
	@Test
	public void testWholeMatchWin()
	{
		for (int i = 0; i <= 50; i++)
		{
			final OMalleyWRWin oMalley = new OMalleyWRWin(i, 0.85, true);
			System.out.println(i + ": " + oMalley.matchInProgress(0.6, 0.6, 3));
		}
	}

	@Ignore
	@Test
	public void testWholeMatchRetire()
	{
		for (int i = 0; i <= 50; i++)
		{
			final OMalleyWRRetire oMalley = new OMalleyWRRetire(i, 0.85, true);
			System.out.println(i + " = " + oMalley.matchInProgress(0.6, 0.6, 3));
		}
	}
}
