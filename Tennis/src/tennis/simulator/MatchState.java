package tennis.simulator;

public class MatchState implements Score
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

	@Override
	public void incrementTarget()
	{
		game.incrementTarget();
	}

	@Override
	public void incrementOpponent()
	{
		game.incrementOpponent();
	}

	public boolean setFinished()
	{
		if (set.finished())
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

	public boolean gameFinished()
	{
		if (game.finished())
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
			set.incrementTarget();
		}
		else
		{
			set.incrementOpponent();
		}

		game.reset();
	}

	@Override
	public boolean finished()
	{
		return targetSets == numSetsForWin || opponentSets == numSetsForWin;
	}

	@Override
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
