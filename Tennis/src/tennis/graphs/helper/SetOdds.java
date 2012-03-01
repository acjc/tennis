package tennis.graphs.helper;

import java.util.Date;

import org.jfree.data.time.Second;

public class SetOdds
{
	long time;
	double [] odds;

	public SetOdds(final long time, final double [] odds)
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
		double result = 0.0;
		for (int i = 0; i < odds.length; i++)
		{
			result += 100 / odds[i];
		}

		return result;
	}
}
