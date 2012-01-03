package tennis.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player
{
	private final String name;
	private final int id;

	private double firstServesIn;
	private double firstServePointsWon;
	private double secondServePointsWon;
	private double servicePointsWon;
	private final double tourAverageServicePointsWon;

	private double firstServeReturnsWon;
	private double secondServeReturnsWon;
	private double returnPointsWon;
	private final double tourAverageReturnPointsWon;

	private final List<String> previousOpponentsDefeated;
	private final List<String> previousOpponentsLostTo;
	private final List<String> previousOpponents;
	private final Map<String, Integer> opponentCount;

	private final List<Integer> victoryIds;
	private final List<Integer> defeatIds;
	private final List<Integer> matchIds;

	public Player(final String name) throws IOException
	{
		this.name = name;

		final DataDownloader downloader = new DataDownloader();
		final PlayerDataParser parser = new PlayerDataParser();

		id = parser.getPlayerId(downloader.downloadPlayerProfile(name));

		final String overview = downloader.downloadPlayerOverview(id);
		firstServesIn = parser.findStat(overview, "1st Serve %");
		firstServePointsWon = parser.findStat(overview, "1st Serve W%");
		secondServePointsWon = parser.findStat(overview, "2nd Serve W%");
		servicePointsWon = parser.findStat(overview, "Service Pts W%");
		tourAverageServicePointsWon = parser.findTourAverage(overview, "Service Pts W%");

		firstServeReturnsWon = parser.findStat(overview, "1st Return W%");
		secondServeReturnsWon = parser.findStat(overview, "2nd Return W%");
		returnPointsWon = parser.findStat(overview, "Return Pts W%");
		tourAverageReturnPointsWon = parser.findTourAverage(overview, "Return Pts W%");

		final String activity = downloader.downloadPlayerActivity(id);
		previousOpponentsDefeated = parser.getPreviousOpponentsDefeated(activity);
		previousOpponentsLostTo = parser.getPreviousOpponentsLostTo(activity);
		previousOpponents = new ArrayList<String>(previousOpponentsDefeated); previousOpponents.addAll(previousOpponentsLostTo);
		opponentCount = new HashMap<String, Integer>();
		for (final String opponent : previousOpponents)
		{
			if (!opponentCount.containsKey(opponent))
			{
				opponentCount.put(opponent, 1);
			}
			else
			{
				opponentCount.put(opponent, opponentCount.get(opponent) + 1);
			}
		}

		victoryIds = parser.getVictoryIds(activity);
		defeatIds = parser.getDefeatIds(activity);
		matchIds = new ArrayList<Integer>(victoryIds); matchIds.addAll(defeatIds);

		System.out.println("Finished retrieving player data for: " + name + " (ID: " + id + ")");
		System.out.println("-> FSI = " + firstServesIn + ", FSPW = " + firstServePointsWon + ", SSPW = " + secondServePointsWon +
				   		   ", FSRW = " + firstServeReturnsWon + ", SSRW = " + secondServeReturnsWon +
				   		   ", SPW = " + servicePointsWon + ", RPW = " + returnPointsWon + '\n');
	}

	public void adjustStatistics(final Player opponent) throws MalformedURLException, IOException
	{
		System.out.println("Retrieving past match data for " + name + " vs " + opponent.name());
		double totalFirstServesIn = firstServesIn, totalFirstServePointsWon = firstServePointsWon, totalSecondServePointsWon = secondServePointsWon,
			   totalFirstServeReturnsWon = firstServeReturnsWon, totalSecondServeReturnsWon = secondServeReturnsWon,
			   totalServicePointsWon = servicePointsWon, totalReturnPointsWon = returnPointsWon;
		double weight = 1.0;
		for (int i = 0; i < previousOpponents.size(); i++)
		{
			final String previousOpponent = previousOpponents.get(i);
			if (previousOpponent.equals(opponent.name()) || checkIfCommonOpponent(previousOpponent, opponent.getOpponents()))
			{
				System.out.println("Found match vs " + previousOpponent + " with Match ID: " + matchIds.get(i));
				final MatchData matchData = new MatchData(matchIds.get(i), name(), previousOpponent);
				if (matchData.downloadMatchData())
				{
					System.out.println("-> FSI = " + matchData.firstServesIn() + ", FSPW = " + matchData.firstServePointsWon() + ", SSPW = " + matchData.secondServePointsWon() +
									   ", FSRW = " + matchData.firstServeReturnsWon() + ", SSRW = " + matchData.secondServeReturnsWon() +
									   ", SPW = " + matchData.servicePointsWon() + ", RPW = " + matchData.returnPointsWon());
					final double matchWeight = 1.0 / opponentCount.get(previousOpponent);
					weight += matchWeight;
					totalFirstServesIn += matchData.firstServesIn() * matchWeight;
					totalFirstServePointsWon += matchData.firstServePointsWon() * matchWeight;
					totalSecondServePointsWon += matchData.secondServePointsWon() * matchWeight;
					totalFirstServeReturnsWon += matchData.firstServeReturnsWon() * matchWeight;
					totalSecondServeReturnsWon += matchData.secondServeReturnsWon() * matchWeight;
					totalServicePointsWon += matchData.servicePointsWon() * matchWeight;
					totalReturnPointsWon += matchData.returnPointsWon() * matchWeight;
				}
				else
				{
					System.out.println("No data available for match: " + matchIds.get(i));
					previousOpponents.remove(i);
					matchIds.remove(i);
				}
			}

		}

		firstServesIn = round(totalFirstServesIn / weight);
		firstServePointsWon = round(totalFirstServePointsWon / weight);
		secondServePointsWon = round(totalSecondServePointsWon / weight);
		firstServeReturnsWon = round(totalFirstServeReturnsWon / weight);
		secondServeReturnsWon = round(totalSecondServeReturnsWon / weight);
		servicePointsWon = round(totalServicePointsWon / weight);
		returnPointsWon = round(totalReturnPointsWon / weight);

		System.out.println("OVERALL: FSI = " + firstServesIn + ", FSPW = " + firstServePointsWon + ", SSPW = " + secondServePointsWon +
						   ", FSRW = " + firstServeReturnsWon + ", SSRW = " + secondServeReturnsWon +
						   ", SPW = " + servicePointsWon + ", RPW = " + returnPointsWon + '\n');
	}

	private boolean checkIfCommonOpponent(final String player, final List<String> opponents)
	{
		for (final String opponent : opponents)
		{
			if (player.equals(opponent))
			{
				return true;
			}
		}
		return false;
	}

	public String name()
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

	public double getTourAverageServicePointsWon()
	{
		return tourAverageServicePointsWon;
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

	public double getTourAverageReturnPointsWon()
	{
		return tourAverageReturnPointsWon;
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

	public double servicePointsWonAgainst(final Player opponent)
	{
		return (servicePointsWon - opponent.returnPointsWon() + tourAverageReturnPointsWon) / 100;
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.#####").format(value));
	}
}
