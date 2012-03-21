package tennis.graphs.helper;

public class SetOdds
{
	private final long time;
	private final String date;
	private final double lpm;
	private double bestBackPrice;
	private double bestLayPrice;
	private final boolean matchedBet;

	public SetOdds(final long time, final String date, final double lpm, final double bestBackPrice, final double bestLayPrice, final boolean matchedBet)
	{
		this.time = time;
		this.date = date;
		this.lpm = lpm;
		this.bestBackPrice = bestBackPrice;
		this.bestLayPrice = bestLayPrice;
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

	public double getBestBackPrice()
	{
		return bestBackPrice;
	}

	public double getBestLayPrice()
	{
		return bestLayPrice;
	}

	public void modifyBackPrice(final double price)
	{
		bestBackPrice = price;
	}

	public void modifyLayPrice(final double price)
	{
		bestLayPrice = price;
	}

	public boolean isMatchedBet()
	{
		return matchedBet;
	}
}
