package tennis.data;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchData
{
	private String matchHtml;
	private boolean playerIsFirst;
	private final int id;
	private final String target;
	private final String opponent;

	public MatchData(final int id, final String target, final String opponent)
	{
		this.id = id;
		this.target = target;
		this.opponent = opponent;
	}

	public boolean downloadMatchData() throws MalformedURLException, IOException
	{
		final DataDownloader downloader = new DataDownloader();
		matchHtml = downloader.downloadMatchData(id);
		if (matchHtml.contains("Match stats currently not available"))
		{
			return false;
		}
		else
		{
			playerIsFirst = checkIfPlayerFirst(target, opponent);
			return true;
		}
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
		return findStat("1st Serve Percentage");
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
		return findStat("1st Return Points Won");
	}

	public double secondServeReturnsWon()
	{
		return findStat("2nd Return Points Won");
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
