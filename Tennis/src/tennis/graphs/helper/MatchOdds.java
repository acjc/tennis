package tennis.graphs.helper;

import java.util.Date;

import org.jfree.data.time.Second;

public class MatchOdds
{
	private final long time;
	private final double odds;

	public MatchOdds(final long time, final double odds)
	{
		this.time = time;
		this.odds = odds;
	}

	public Second getTime()
	{
		return new Second(new Date(time));
	}

	public double getOddsPercentage()
	{
		return 100 / odds;
	}
}
