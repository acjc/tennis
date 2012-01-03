package tennis.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchData
{
	private final String matchHtml;

	private final boolean playerIsFirst;
	private final boolean tennisInsight;

	public MatchData(final String matchUrl, final int id, final String target, final String opponent) throws MalformedURLException, IOException
	{
		final DataDownloader downloader = new DataDownloader();
		matchHtml = downloader.downloadMatchData(matchUrl, id);
		tennisInsight = !matchUrl.contains("http");

		playerIsFirst = checkIfPlayerFirst(target, opponent);
	}

	public boolean checkIfPlayerFirst(final String target, final String opponent)
	{
		final String text = matchHtml.replaceAll("\\<.*?>","");
		final Pattern pattern = Pattern.compile(target + "\\s*" + opponent);
		final Matcher matcher = pattern.matcher(text);
		return matcher.find();
	}

	public double findStat(final String stat)
	{
		final String text = matchHtml.replaceAll("\\<.*?>","");
		Pattern pattern;
		if (playerIsFirst)
		{
			pattern = Pattern.compile(stat + "\\s*([\\d\\.]+)");
		}
		else
		{
			pattern = Pattern.compile(stat + "\\s*[\\d\\.]+.*\\s*([\\d\\.]+)");
		}
		final Matcher matcher = pattern.matcher(text);
		matcher.find();
		return Integer.parseInt(matcher.group(1));
	}

	public double firstServesIn()
	{
		return tennisInsight ? findStat("1st Serve Percentage") : findStat("1st Serve");
	}

	public double firstServePointsWon()
	{
		return findStat("1st Serve Points Won");
	}

	public double secondServePointsWon()
	{
		return findStat("2nd Serve Points Won");
	}

	public double firstServeReturnsWon()
	{
		return tennisInsight ? findStat("1st Return Points Won") : findStat("1st Serve Return Points Won");
	}

	public double secondServeReturnsWon()
	{
		return tennisInsight ? findStat("2nd Return Points Won") : findStat("2nd Serve Return Points Won");
	}

	public double servicePointsWon()
	{
		return findStat("Total Service Points Won");
	}

	public double returnPointsWon()
	{
		return findStat("Total Return Points Won");
	}
}
