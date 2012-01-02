package tennis.data;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Player
{
	private final String name;
	private final int id;

	private final double firstServesIn;
	private final double firstServePointsWon;
	private final double secondServePointsWon;
	private final double servicePointsWon;

	private final double firstServeReturnsWon;
	private final double secondServeReturnsWon;
	private final double returnPointsWon;

	private final Set<String> opponentsDefeated;
	private final Set<String> opponentsLostTo;
	private final Set<String> opponents;
	private final Set<Integer> victoryIds;
	private final Set<String> victoryStats;
	private final Set<Integer> defeatIds;
	private final Set<String> defeatStats;

	public Player(final String name) throws IOException
	{
		this.name = name;

		final PlayerDataRetriever retriever = new PlayerDataRetriever();
		id = retriever.getPlayerId(retriever.downloadPlayerProfile(name));

		final String stats = retriever.downloadPlayerStatistics(id);
		firstServesIn = retriever.findStat(stats, "1st Serve %");
		firstServePointsWon = retriever.findStat(stats, "1st Serve W%");
		secondServePointsWon = retriever.findStat(stats, "2nd Serve W%");
		servicePointsWon = retriever.findStat(stats, "Service Pts W%");
		firstServeReturnsWon = retriever.findStat(stats, "1st Return W%");
		secondServeReturnsWon = retriever.findStat(stats, "2nd Return W%");
		returnPointsWon = retriever.findStat(stats, "Rtn Points W%");

		final String activity = retriever.downloadPlayerActivity(id);
		opponentsDefeated = retriever.getOpponentsDefeated(activity);
		opponentsLostTo = retriever.getOpponentsLostTo(activity);
		opponents = new HashSet<String>(opponentsDefeated); opponents.addAll(opponentsLostTo);

		victoryIds = retriever.getVictoryIds(activity);
		victoryStats = retriever.getVictoryStatistics(activity);
		defeatIds = retriever.getDefeatIds(activity);
		defeatStats = retriever.getDefeatStatistics(activity);

		System.out.println("Finished retrieving data for: " + name + "(" + id + ")");
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
