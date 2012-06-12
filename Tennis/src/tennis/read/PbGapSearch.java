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

	public PbGapSearch(final double oddsMwp,
					final int targetSets, final int opponentSets, final int targetGames, final int opponentGames, final int targetPoints, final int opponentPoints,
					final boolean servingNext)
	{
		this.oddsMwp = oddsMwp;
		this.targetSets = targetSets;
		this.opponentSets = opponentSets;
		this.targetGames = targetGames;
		this.opponentGames = opponentGames;
		this.targetPoints = targetPoints;
		this.opponentPoints = opponentPoints;
		this.servingNext = servingNext;
	}

	public static void main(final String[] args)
	{
		System.out.println(new PbGapSearch(0.787, 0, 0, 0, 0, 0, 0, true).search());
	}

	public double search()
	{
		double gap = 0.0;
		double pa = 0.645;
		double pb = 0.645;
		double mwp = 0.5;
		if (oddsMwp > 0.5)
		{
			while(mwp < oddsMwp)
			{
				pa += 0.0005;
				pb -= 0.0005;
				gap += 0.001;
				mwp = OMalley.matchInProgress(pa, pb, new CurrentMatchScore(targetSets, opponentSets), new CurrentSetScore(targetGames, opponentGames), new CurrentGameScore(targetPoints, opponentPoints), servingNext, 3);
//				System.out.println(mwp);
			}
		}
		else if (oddsMwp < 0.5)
		{
			while(mwp > oddsMwp)
			{
				pa -= 0.0005;
				pb += 0.0005;
				gap += 0.001;
				mwp = OMalley.matchInProgress(pa, pb, new CurrentMatchScore(targetSets, opponentSets), new CurrentSetScore(targetGames, opponentGames), new CurrentGameScore(targetPoints, opponentPoints), servingNext, 3);
			}
		}
		else
		{
			return 0;
		}

		return gap;
	}
}
