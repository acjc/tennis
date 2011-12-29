package tennis.simulator;

public class SetState
{
	private int targetGames;
	private int opponentGames;

	public SetState()
	{
		this(0, 0);
	}

	public SetState(final int targetGames, final int opponentGames)
	{
		this.targetGames = targetGames;
		this.opponentGames = opponentGames;
	}

	public SetState(final SetState s)
	{
		this(s.getTargetGames(), s.getOpponentGames());
	}

	public boolean setOver()
	{
		return (Math.abs(targetGames - opponentGames) >= 2 && (targetGames == 6 || opponentGames == 6))
				|| (targetGames == 7 || opponentGames == 7);
	}

	public boolean targetWon()
	{
		return targetGames > opponentGames;
	}

	public void targetGame()
	{
		targetGames++;
	}

	public void opponentGame()
	{
		opponentGames++;
	}

	public boolean tiebreak()
	{
		return targetGames == 6 && opponentGames == 6;
	}

	public void reset()
	{
		targetGames = 0;
		opponentGames = 0;
	}

	private int getTargetGames()
	{
		return targetGames;
	}

	private int getOpponentGames()
	{
		return opponentGames;
	}
}
