package tennis.simulator;

public class MatchState
{
	private boolean servingNext;
	private int targetPoints = 0;
	private int opponentPoints = 0;
	private int targetGames = 0;
	private int opponentGames = 0;
	private int targetSets = 0;
	private int opponentSets = 0;

	public MatchState()
	{
		servingNext = (Math.random() < 0.5) ? true : false;
	}

	public boolean isServingNext()
	{
		return servingNext;
	}

	public void changeServer()
	{
		servingNext = !servingNext;
	}

	public boolean setOver()
	{
		if ((targetGames == 7 || opponentGames == 7)
			|| (targetGames == 6 && opponentGames < 5)
			|| (targetGames < 5 && opponentGames == 6))
		{
			resetSet();
			return true;
		}
		return false;
	}

	private void resetSet()
	{
		if (targetGames > opponentGames)
		{
			targetSets++;
		}
		else
		{
			opponentSets++;
		}

		targetGames = 0;
		opponentGames = 0;
	}

	public boolean gameOver()
	{
		if ((Math.abs(targetPoints - opponentPoints) >= 2 && (targetPoints >= 5 || opponentPoints >= 5))
			|| (targetPoints == 4 && opponentPoints < 3)
			|| (targetPoints < 3 && opponentPoints == 4))
		{
			resetGame();
			return true;
		}
		return false;
	}

	public void resetGame()
	{
		if (targetPoints > opponentPoints)
		{
			targetGames++;
		}
		else
		{
			opponentGames++;
		}

		targetPoints = 0;
		opponentPoints = 0;
		changeServer();
	}

	public void targetPoint()
	{
		targetPoints++;
	}

	public void opponentPoint()
	{
		opponentPoints++;
	}

	public boolean matchOver()
	{
		return targetSets == 3 || opponentSets == 3;
	}

	public boolean targetWon()
	{
		return targetSets == 3;
	}

	public boolean tiebreak()
	{
		return targetGames == 6 && opponentGames == 6;
	}

	public boolean tiebreakOver()
	{
		if (Math.abs(targetPoints - opponentPoints) >= 2 && (targetPoints >= 7 || opponentPoints >= 7))
		{
			resetGame();
			return true;
		}
		return false;
	}
}
