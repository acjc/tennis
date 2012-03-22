package tennis.omalley;

public class CurrentMatchScore
{
	private final int targetSets;
	private final int opponentSets;

	public CurrentMatchScore()
	{
		targetSets = 0;
		opponentSets = 0;
	}

	public CurrentMatchScore(final int targetSets, final int opponentSets)
	{
		this.targetSets = targetSets;
		this.opponentSets = opponentSets;
	}

	public int getTargetSets()
	{
		return targetSets;
	}

	public int getOpponentSets()
	{
		return opponentSets;
	}

	public CurrentMatchScore incTargetSets()
	{
		return new CurrentMatchScore(targetSets + 1, opponentSets);
	}

	public CurrentMatchScore incOpponentSets()
	{
		return new CurrentMatchScore(targetSets, opponentSets + 1);
	}
}
