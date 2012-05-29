package tennis.simulator;

public interface Score
{
	void incrementTarget();
	void incrementOpponent();

	boolean over();
	boolean targetWon();
}