package tennis.simulator;

import tennis.omalley.CurrentGameScore;
import tennis.omalley.CurrentMatchScore;
import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalley;

public class MatchState implements Score
{
	private final GameState game;
	private final SetState set;
	private int targetSets;
	private int opponentSets;
	private boolean targetRetired = false;
	private boolean opponentRetired = false;

	private final int numSetsForWin;

	private int numDeuces = 0;
	private int totalPointsPostDeuce = 0;
	private int numTiebreaks = 0;
	private int totalTiebreakPoints = 0;
	private final double[][] setScores = new double[8][8];

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
		if (retirementOccurred())
		{
			return true;
		}
		return false;
	}

	private boolean retirementOccurred()
	{
		return targetRetired || opponentRetired;
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
			totalTiebreakPoints += game.getTargetPoints();
			totalTiebreakPoints += game.getOpponentPoints();
			numTiebreaks++;
			resetGame();
			return true;
		}
		if (retirementOccurred())
		{
			return true;
		}
		return false;
	}

	public boolean gameOver()
	{
		if (game.over())
		{
			if (game.wentToDeuce())
			{
				numDeuces++;
				totalPointsPostDeuce += game.getTargetPoints() - 3;
				totalPointsPostDeuce += game.getOpponentPoints() - 3;
			}
			resetGame();
			return true;
		}
		if (retirementOccurred())
		{
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
		return targetSets == numSetsForWin || opponentSets == numSetsForWin || retirementOccurred();
	}

	@Override
	public boolean targetWon()
	{
		return targetSets == numSetsForWin;
	}

	public void targetRetires()
	{
		targetRetired = true;
	}

	public void opponentRetires()
	{
		opponentRetired = true;
	}

	public boolean targetRetired()
	{
		return targetRetired;
	}

	public boolean opponentRetired()
	{
		return opponentRetired;
	}

	public boolean inFirstSet()
	{
		return targetSets + opponentSets == 0;
	}

	public int getTargetSets()
	{
		return targetSets;
	}

	public int getOpponentSets()
	{
		return opponentSets;
	}

	public int getTargetGames()
	{
		return set.getTargetGames();
	}

	public int getOpponentGames()
	{
		return set.getOpponentGames();
	}

	public int getTargetPoints()
	{
		return game.getTargetPoints();
	}

	public int getOpponentPoints()
	{
		return game.getOpponentPoints();
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

	public int getNumTiebreaks()
	{
		return numTiebreaks;
	}

	public int getTotalTiebreakPoints()
	{
		return totalTiebreakPoints;
	}

	public int getNumDeuces()
	{
		return numDeuces;
	}

	public int getTotalPointsPostDeuce()
	{
		return totalPointsPostDeuce;
	}

	public double getSetScores(final int i, final int j)
	{
		return setScores[i][j];
	}

	public double getTargetPrediction(final double pa, final double pb, final boolean serving)
	{
		return OMalley.matchInProgress(pa, pb, new CurrentMatchScore(targetSets, opponentSets),
											   new CurrentSetScore(getTargetGames(), getOpponentGames()),
				  					  	       new CurrentGameScore(getTargetPoints(), getOpponentPoints()),
				  					   isServingNext(), numSetsForWin);
	}

	public Double getTargetInjuryPrediction(final double pa, final double pb, final double risk, final boolean serving)
	{
		final double targetInjuryPrediction = getTargetPrediction(pa, pb, serving) - risk;
		return targetInjuryPrediction < 0 ? 0 : targetInjuryPrediction;
	}
}
