package tennis.simulator;

public class GameState implements Score
{
	private boolean serving;

	private int targetPoints;
	private int opponentPoints;

	public GameState()
	{
		this((Math.random() < 0.5) ? true : false);
	}

	public GameState(final boolean serving)
	{
		this(0, 0, serving);
	}

	public GameState(final int targetPoints, final int opponentPoints, final boolean serving)
	{
		this.targetPoints = targetPoints;
		this.opponentPoints = opponentPoints;
		this.serving = serving;
	}

	public GameState(final GameState s)
	{
		this(s.getTargetPoints(), s.getOpponentPoints(), s.isServing());
	}

	@Override
	public boolean over()
	{
		return Math.abs(targetPoints - opponentPoints) >= 2 && (targetPoints >= 4 || opponentPoints >= 4);
	}

	public boolean tiebreakOver()
	{
		return Math.abs(targetPoints - opponentPoints) >= 2 && (targetPoints >= 7 || opponentPoints >= 7);
	}

	@Override
	public void incrementTarget()
	{
		targetPoints++;
	}

	@Override
	public void incrementOpponent()
	{
		opponentPoints++;
	}

	@Override
	public boolean targetWon()
	{
		return targetPoints > opponentPoints;
	}

	public boolean isOddPoint()
	{
		return (targetPoints + opponentPoints) % 2 == 1;
	}

	public boolean isServing()
	{
		return serving;
	}

	private void changeServer()
	{
		serving = !serving;
	}

	public void reset()
	{
		targetPoints = 0;
		opponentPoints = 0;
		changeServer();
	}

	private int getTargetPoints()
	{
		return targetPoints;
	}

	private int getOpponentPoints()
	{
		return opponentPoints;
	}

	public void coinToss()
	{
		serving = (Math.random() < 0.5) ? true : false;
	}
}
