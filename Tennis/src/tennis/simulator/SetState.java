package tennis.simulator;

public class SetState implements Score
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

	@Override
	public boolean over()
	{
		return (Math.abs(targetGames - opponentGames) >= 2 && (targetGames == 6 || opponentGames == 6))
				|| (targetGames == 7 || opponentGames == 7);
	}

	@Override
	public boolean targetWon()
	{
		return targetGames > opponentGames;
	}

	@Override
	public void incrementTarget()
	{
		targetGames++;
	}

	@Override
	public void incrementOpponent()
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
