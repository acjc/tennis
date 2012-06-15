package tennis.read;

import tennis.omalley.CurrentGameScore;
import tennis.omalley.CurrentMatchScore;
import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalley;

public class PbGapSearch
{
	private final double oddsMwp;
	private final int targetSets;
	private final int opponentSets;
	private final int targetGames;
	private final int opponentGames;
	private final int targetPoints;
	private final int opponentPoints;
	private final boolean servingNext;
	private final int numSetsToWin;

	public PbGapSearch(final double oddsMwp,
					   final int targetSets, final int opponentSets, final int targetGames, final int opponentGames, final int targetPoints, final int opponentPoints,
					   final boolean servingNext, final int numSetsToWin)
	{
		this.oddsMwp = oddsMwp;
		this.targetSets = targetSets;
		this.opponentSets = opponentSets;
		this.targetGames = targetGames;
		this.opponentGames = opponentGames;
		this.targetPoints = targetPoints;
		this.opponentPoints = opponentPoints;
		this.servingNext = servingNext;
		this.numSetsToWin = numSetsToWin;
	}

	public static void main(final String[] args)
	{
		System.out.println(new PbGapSearch(0.375, 0, 0, 0, 0, 0, 0, true, 3).search());
	}

	public double search()
	{
		double upperGap = 0.4;
		double gap = 0.0;
		double lowerGap = -0.4;
		final double pa = 0.645;
		final double pb = 0.645;
		double mwp = 0.5;
		int iterations = 0;
		while(Math.abs(mwp - oddsMwp) > 0.0001 && iterations < 50)
		{
			gap = (upperGap + lowerGap) / 2.0;
			mwp = OMalley.matchInProgress(pa + (gap / 2.0), pb - (gap / 2.0), new CurrentMatchScore(targetSets, opponentSets), new CurrentSetScore(targetGames, opponentGames), new CurrentGameScore(targetPoints, opponentPoints), servingNext, numSetsToWin);
			if(mwp > oddsMwp)
			{
				upperGap = gap;
			}
			else
			{
				lowerGap = gap;
			}
			iterations++;
//			System.out.println("U = " + upperGap + ", L = " + lowerGap + ", Gap = " + gap + ", MWP = " + mwp);
		}

//		System.out.println((pa + (gap / 2.0)) + ", " + (pb - (gap / 2.0)));
		return gap;
	}
}
