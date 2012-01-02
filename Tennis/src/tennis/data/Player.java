package tennis.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	private final List<String> previousOpponentsDefeated;
	private final List<String> previousOpponentsLostTo;
	private final List<String> previousOpponents;

	private final List<Integer> victoryIds;
	private final List<String> victoryStats;
	private final List<Integer> defeatIds;
	private final List<String> defeatStats;

	private final List<Integer> matchIds;
	private final List<String> matchStats;

	public Player(final String name) throws IOException
	{
		this.name = name;

		final DataDownloader downloader = new DataDownloader();
		final DataParser parser = new DataParser();

		id = parser.getPlayerId(downloader.downloadPlayerProfile(name));

		final String stats = downloader.downloadPlayerOverview(id);
		firstServesIn = parser.findStat(stats, "1st Serve %");
		firstServePointsWon = parser.findStat(stats, "1st Serve W%");
		secondServePointsWon = parser.findStat(stats, "2nd Serve W%");
		servicePointsWon = parser.findStat(stats, "Service Pts W%");
		firstServeReturnsWon = parser.findStat(stats, "1st Return W%");
		secondServeReturnsWon = parser.findStat(stats, "2nd Return W%");
		returnPointsWon = parser.findStat(stats, "Return Pts W%");

		final String activity = downloader.downloadPlayerActivity(id);
		previousOpponentsDefeated = parser.getPreviousOpponentsDefeated(activity);
		previousOpponentsLostTo = parser.getPreviousOpponentsLostTo(activity);
		previousOpponents = new ArrayList<String>(previousOpponentsDefeated); previousOpponents.addAll(previousOpponentsLostTo);

		victoryIds = parser.getVictoryIds(activity);
		victoryStats = parser.getVictoryStatistics(activity);
		defeatIds = parser.getDefeatIds(activity);
		defeatStats = parser.getDefeatStatistics(activity);

		matchIds = new ArrayList<Integer>(victoryIds); matchIds.addAll(defeatIds);
		matchStats = new ArrayList<String>(victoryStats); matchStats.addAll(defeatStats);

		System.out.println("Finished retrieving data for: " + name + " (" + id + ")");
	}

	public void adjustStatistics(final Player opponent)
	{
		for (int i = 0; i < previousOpponents.size(); i++)
		{
			if (previousOpponents.get(i).equals(opponent.name()))
			{
				System.out.println("Found match vs " + previousOpponents.get(i) + " with Match ID: " + matchIds.get(i));
			}
		}
	}

	private String name()
	{
		return name;
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

	public List<String> getOpponentsDefeated()
	{
		return previousOpponentsDefeated;
	}

	public List<String> getOpponentsLostTo()
	{
		return previousOpponentsLostTo;
	}

	public List<String> getOpponents()
	{
		return previousOpponents;
	}

	public List<Integer> getVictoryIds()
	{
		return victoryIds;
	}

	public List<String> getVictoryStats()
	{
		return victoryStats;
	}

	public List<Integer> getDefeatIds()
	{
		return defeatIds;
	}

	public List<String> getDefeatStats()
	{
		return defeatStats;
	}
}
