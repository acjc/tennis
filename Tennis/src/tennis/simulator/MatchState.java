package tennis.simulator;

import java.util.ArrayList;
import java.util.List;

import tennis.omalley.OMalley;

public class MatchState implements Score
{
	private final GameState game;
	private final SetState set;
	private int targetSets;
	private int opponentSets;

	private final int numSetsForWin;

	private int tiebreaks = 0;
	private final double[][] setScores = new double[8][8];

	private final List<Double> targetPrediction;

	public MatchState()
	{
		this(3);
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
		targetPrediction = new ArrayList<Double>();
	}

	public MatchState(final int numSetsToWin)
	{
		this(0, 0, new SetState(), new GameState(), numSetsToWin);
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

	public boolean setOver()
	{
		if (set.over())
		{
			setScores[set.getTargetGames()][set.getOpponentGames()]++;
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
			tiebreaks++;
			return true;
		}
		return false;
	}

	public boolean gameOver()
	{
		if (game.over())
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
	public boolean over()
	{
		return targetSets == numSetsForWin || opponentSets == numSetsForWin;
	}

	@Override
	public boolean targetWon()
	{
		return targetSets == numSetsForWin;
	}

	public int getTargetSets()
	{
		return targetSets;
	}

	public int getOpponentSets()
	{
		return opponentSets;
	}

	public int setsPlayed()
	{
		return targetSets + opponentSets;
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

	public void coinToss()
	{
		game.coinToss();
	}

	public int tiebreaks()
	{
		return tiebreaks;
	}

	public double getSetScores(final int i, final int j)
	{
		return setScores[i][j];
	}

	public double getTargetPrediction(final double p, final double q, final boolean serving)
	{
		return OMalley.matchInProgress(p, q, targetSets, opponentSets, set.getTargetGames(), set.getOpponentGames(),
				  					  	     game.getTargetPoints(), game.getOpponentPoints(), isServingNext(),
											 numSetsForWin);
	}

	public Double getTargetInjuryPrediction(final double p, final double q, final double risk, final boolean serving)
	{
		final double targetInjuryPrediction = getTargetPrediction(p, q, serving) - risk;
		return targetInjuryPrediction < 0 ? 0 : targetInjuryPrediction;
	}
}
