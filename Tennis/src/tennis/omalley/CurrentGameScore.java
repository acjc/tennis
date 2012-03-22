package tennis.omalley;

public class CurrentGameScore
{
	private final int targetPoints;
	private final int opponentPoints;

	public CurrentGameScore()
	{
		targetPoints = 0;
		opponentPoints = 0;
	}

	public CurrentGameScore(final int targetPoints, final int opponentPoints)
	{
		this.targetPoints = targetPoints;
		this.opponentPoints = opponentPoints;
	}

	public int getTargetPoints()
	{
		return targetPoints;
	}

	public int getOpponentPoints()
	{
		return opponentPoints;
	}

	public CurrentGameScore incTargetPoints()
	{
		return new CurrentGameScore(targetPoints + 1, opponentPoints);
	}

	public CurrentGameScore incOpponentPoints()
	{
		return new CurrentGameScore(targetPoints, opponentPoints + 1);
	}
}
