package tennis.simulator;

public class MatchState
{
	private final GameState game;
	private final SetState set;
	private int targetSets;
	private int opponentSets;

	private final int numSetsForWin;

	public MatchState()
	{
		this(0, 0, new SetState(), new GameState(), 3);
	}

	// Deep copy
	public MatchState(final MatchState s)
	{
		this(s.getTargetSets(), s.getOpponentSets(), s.getSetState(), s.getGameState(), s.getNumSetsForWin());
	}

	public MatchState(final int targetSets, final int opponentSets, final SetState set, final GameState game, final int numSetsForWin)
	{
		this.targetSets = targetSets;
		this.opponentSets = opponentSets;
		this.set = set;
		this.game = game;
		this.numSetsForWin = numSetsForWin;
	}

	public boolean isServingNext()
	{
		return game.isServing();
	}

	public void targetPoint()
	{
		game.targetPoint();
	}

	public void opponentPoint()
	{
		game.opponentPoint();
	}

	public boolean setOver()
	{
		if (set.setOver())
		{
			if (set.targetWon())
			{
				targetSets++;
			}
			else
			{
				opponentSets++;
			}
			set.reset();
			return true;
		}
		return false;
	}

	public boolean tiebreak()
	{
		return set.tiebreak();
	}

	public boolean isOddPoint()
	{
		return game.isOddPoint();
	}

	public boolean tiebreakOver()
	{
		if (game.tiebreakOver())
		{
			resetGame();
			return true;
		}
		return false;
	}

	public boolean gameOver()
	{
		if (game.gameOver())
		{
			resetGame();
			return true;
		}
		return false;
	}

	private void resetGame()
	{
		if (game.targetWon())
		{
			set.targetGame();
		}
		else
		{
			set.opponentGame();
		}

		game.reset();
	}

	public boolean matchOver()
	{
		return targetSets == numSetsForWin || opponentSets == numSetsForWin;
	}

	public boolean targetWon()
	{
		return targetSets == numSetsForWin;
	}

	private int getTargetSets()
	{
		return targetSets;
	}

	private int getOpponentSets()
	{
		return opponentSets;
	}

	private int getNumSetsForWin()
	{
		return numSetsForWin;
	}

	private SetState getSetState()
	{
		return new SetState(set);
	}

	private GameState getGameState()
	{
		return new GameState(game);
	}
}
