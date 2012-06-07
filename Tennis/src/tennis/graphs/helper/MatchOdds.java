package tennis.graphs.helper;

public class MatchOdds
{
	private final long time;
	private final double odds;

	public MatchOdds(final long time, final double odds)
	{
		this.time = time;
		this.odds = odds;
	}

	public long getTime()
	{
		return time;
	}

	public double getOddsProbability()
	{
		return 1 / odds;
	}

	public double getOddsPercentage()
	{
		return 100 / odds;
	}
}
