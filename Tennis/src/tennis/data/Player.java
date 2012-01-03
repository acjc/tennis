package tennis.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class Player
{
	private final String name;
	private final int id;

	private double firstServesIn;
	private double firstServePointsWon;
	private double secondServePointsWon;
	private double servicePointsWon;

	private double firstServeReturnsWon;
	private double secondServeReturnsWon;
	private double returnPointsWon;

	private final List<String> previousOpponentsDefeated;
	private final List<String> previousOpponentsLostTo;
	private final List<String> previousOpponents;

	private final List<Integer> victoryIds;
	private final List<Integer> defeatIds;
	private final List<Integer> matchIds;

	public Player(final String name) throws IOException
	{
		this.name = name;

		final DataDownloader downloader = new DataDownloader();
		final PlayerDataParser parser = new PlayerDataParser();

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
		defeatIds = parser.getDefeatIds(activity);
		matchIds = new ArrayList<Integer>(victoryIds); matchIds.addAll(defeatIds);

		System.out.println("Finished retrieving data for: " + name + " (" + id + ")");
	}

	public void adjustStatistics(final Player opponent) throws MalformedURLException, IOException
	{
		double weight = 0.0;
		double totalFirstServesIn = 0, totalFirstServePointsWon = 0, totalSecondServePointsWon = 0, totalFirstServeReturnsWon = 0,
			   totalSecondServeReturnsWon = 0, totalServicePointsWon = 0, totalReturnPointsWon = 0;
		for (int i = 0; i < previousOpponents.size(); i++)
		{
			if (previousOpponents.get(i).equals(opponent.name()))
			{
				System.out.println("Found match vs " + previousOpponents.get(i) + " with Match ID: " + matchIds.get(i));
				final MatchData matchData = new MatchData(matchIds.get(i), name(), opponent.name());
				if (matchData.downloadMatchData())
				{
					System.out.println("FSI = " + matchData.firstServesIn() + ", FSPW = " + matchData.firstServePointsWon() + ", SSPW = " + matchData.secondServePointsWon() +
									   ", FSRW = " + matchData.firstServeReturnsWon() + ", SSRW = " + matchData.secondServeReturnsWon() +
									   ", SPW = " + matchData.servicePointsWon() + ", RPW = " + matchData.returnPointsWon());
					weight++;
					totalFirstServesIn += matchData.firstServesIn();
					totalFirstServePointsWon += matchData.firstServePointsWon();
					totalSecondServePointsWon += matchData.secondServePointsWon();
					totalFirstServeReturnsWon += matchData.firstServeReturnsWon();
					totalSecondServeReturnsWon += matchData.secondServeReturnsWon();
					totalServicePointsWon += matchData.servicePointsWon();
					totalReturnPointsWon += matchData.returnPointsWon();
				}
				else
				{
					System.out.println("No data for match: " + matchIds.get(i));
					previousOpponents.remove(i);
					matchIds.remove(i);
				}
			}

		}

		firstServesIn = totalFirstServesIn / weight;
		firstServePointsWon = totalFirstServePointsWon / weight;
		secondServePointsWon = totalSecondServePointsWon / weight;
		firstServeReturnsWon = totalFirstServeReturnsWon / weight;
		secondServeReturnsWon = totalSecondServeReturnsWon / weight;
		servicePointsWon = totalServicePointsWon / weight;
		returnPointsWon = totalReturnPointsWon / weight;

		System.out.println("OVERALL: FSI = " + firstServesIn + ", FSPW = " + firstServePointsWon + ", SSPW = " + secondServePointsWon +
						   ", FSRW = " + firstServeReturnsWon + ", SSRW = " + secondServeReturnsWon +
						   ", SPW = " + servicePointsWon + ", RPW = " + returnPointsWon);
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

	public List<Integer> getDefeatIds()
	{
		return defeatIds;
	}
}
