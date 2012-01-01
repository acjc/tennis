package tennis.data;

import java.io.IOException;

public class Player
{
	private final String name;

	private final double firstServesIn;
	private final double firstServePointsWon;
	private final double secondServePointsWon;
	private final double servicePointsWon;

	private final double firstServeReturnsWon;
	private final double secondServeReturnsWon;
	private final double returnPointsWon;

	public Player(final String name) throws IOException
	{
		this.name = name;

		final PlayerDataRetriever retriever = new PlayerDataRetriever();
		final String stats = retriever.downloadPlayerStatistics(retriever.getPlayerId(retriever.downloadPlayerProfile(name)));
		firstServesIn = retriever.findStat(stats, "1st Serve %");
		firstServePointsWon = retriever.findStat(stats, "1st Serve W%");
		secondServePointsWon = retriever.findStat(stats, "2nd Serve W%");
		servicePointsWon = retriever.findStat(stats, "Service Pts W%");
		firstServeReturnsWon = retriever.findStat(stats, "1st Return W%");
		secondServeReturnsWon = retriever.findStat(stats, "2nd Return W%");
		returnPointsWon = retriever.findStat(stats, "Rtn Points W%");
	}

	public double firstServesIn()
	{
		return firstServesIn;
	}

	public double firstServePointsWon()
	{
		return firstServePointsWon;
	}

	public double secondServePointsWon()
	{
		return secondServePointsWon;
	}

	public double servicePointsWon()
	{
		return servicePointsWon;
	}

	public double firstServeReturnsWon()
	{
		return firstServeReturnsWon;
	}

	public double secondServeReturnsWon()
	{
		return secondServeReturnsWon;
	}

	public double returnPointsWon()
	{
		return returnPointsWon;
	}
}
