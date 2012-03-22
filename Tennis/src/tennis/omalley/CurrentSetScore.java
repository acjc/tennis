package tennis.omalley;

public class CurrentSetScore
{
	private final int targetGames;
	private final int opponentGames;

	public CurrentSetScore()
	{
		targetGames = 0;
		opponentGames = 0;
	}

	public CurrentSetScore(final int targetGames, final int opponentGames)
	{
		this.targetGames = targetGames;
		this.opponentGames = opponentGames;
	}

	public int getTargetGames()
	{
		return targetGames;
	}

	public int getOpponentGames()
	{
		return opponentGames;
	}

	public CurrentSetScore incTargetGames()
	{
		return new CurrentSetScore(targetGames + 1, opponentGames);
	}

	public CurrentSetScore incOpponentGames()
	{
		return new CurrentSetScore(targetGames, opponentGames + 1);
	}
}
