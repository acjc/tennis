package tennis.simulator;

public class MatchState
{
	private boolean servingNext;

	private int targetPoints;
	private int opponentPoints;
	private int targetGames;
	private int opponentGames;
	private int targetSets;
	private int opponentSets;

	private final int numSetsForWin;

	public MatchState()
	{
		this(0, 0, 0, 0, 0, 0, (Math.random() < 0.5) ? true : false, 3);
	}

	public MatchState(final MatchState s)
	{
		this(s.getTargetSets(), s.getOpponentSets(), s.getTargetGames(), s.getOpponentGames(),
			 s.getTargetPoints(), s.getOpponentPoints(), s.isServingNext(), s.getNumSetsForWin());
	}

	public MatchState(final int targetSets, final int opponentSets,
					  final int targetGames, final int opponentGames,
					  final int targetPoints, final int opponentPoints,
					  final boolean servingNext, final int numSetsForWin)
	{
		this.targetSets = targetSets;
		this.opponentSets = opponentSets;
		this.targetGames = targetGames;
		this.opponentGames = opponentGames;
		this.targetPoints = targetPoints;
		this.opponentPoints = opponentPoints;

		this.servingNext = servingNext;
		this.numSetsForWin = numSetsForWin;
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
		if ((Math.abs(targetGames - opponentGames) >= 2 && (targetGames >= 6 || opponentGames >= 6))
			|| (targetGames == 7 || opponentGames == 7))
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
		if (Math.abs(targetPoints - opponentPoints) >= 2 && (targetPoints >= 4 || opponentPoints >= 4))
		{
			resetGame();
			return true;
		}
		return false;
	}

	private void resetGame()
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
		return targetSets == numSetsForWin || opponentSets == numSetsForWin;
	}

	public boolean targetWon()
	{
		return targetSets == numSetsForWin;
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

	public int getTargetPoints()
	{
		return targetPoints;
	}

	public int getOpponentPoints()
	{
		return opponentPoints;
	}

	public int getTargetGames()
	{
		return targetGames;
	}

	public int getOpponentGames()
	{
		return opponentGames;
	}

	public int getTargetSets()
	{
		return targetSets;
	}

	public int getOpponentSets()
	{
		return opponentSets;
	}

	public int getNumSetsForWin()
	{
		return numSetsForWin;
	}
}
