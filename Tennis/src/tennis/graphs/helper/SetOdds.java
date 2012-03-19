package tennis.graphs.helper;

public class SetOdds
{
	private final long time;
	private final String date;
	private final double lpm;
	private final double layPrice;
	private final boolean matchedBet;

	public SetOdds(final long time, final String date, final double lpm, final double layPrice, final boolean matchedBet)
	{
		this.time = time;
		this.date = date;
		this.lpm = lpm;
		this.layPrice = layPrice;
		this.matchedBet = matchedBet;
	}

	public void printDate()
	{
		System.out.println(date);
	}

	public String getDate()
	{
		return date;
	}

	public long getTime()
	{
		return time;
	}

	public double getOddsPercentage()
	{
		return 100 / lpm;
	}

	public double getOddsProbability()
	{
		return 1 / lpm;
	}

	public double getOdds()
	{
		return lpm;
	}

	public double getLayPrice()
	{
		return layPrice;
	}

	public boolean isMatchedBet()
	{
		return matchedBet;
	}
}
