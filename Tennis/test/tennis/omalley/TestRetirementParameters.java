package tennis.omalley;

import org.junit.Test;

public class TestRetirementParameters
{
	@Test
	public void reverseEngineer()
	{
		System.out.println(OMalley.matchInProgress(0.55, 0.46, new CurrentMatchScore(1, 0),
															   new CurrentSetScore(3, 4),
															   new CurrentGameScore(), true, 3));
		System.out.println(OMalleyWithRetirement.matchInProgressWithRetirement(0.55, 0.46, new CurrentMatchScore(1, 0),
																						   new CurrentSetScore(3, 4),
																						   new CurrentGameScore(), true, 3, 0.2));
		System.out.println(OMalleyAvgPointsToBePlayed.matchInProgressAvgPointsToBePlayed(0.55, 0.46, new CurrentMatchScore(1, 0),
																							 new CurrentSetScore(3, 4),
																							 new CurrentGameScore(), true, 3, 0).points);
	}
}
