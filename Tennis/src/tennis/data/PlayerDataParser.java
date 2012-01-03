package tennis.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerDataParser
{
	public int getPlayerId(final String playerHtml) throws IOException
	{
		final Pattern pattern = Pattern.compile("activity.php\\?player_id=(\\d*)");
		final Matcher matcher = pattern.matcher(playerHtml);
		matcher.find();
		return Integer.parseInt(matcher.group(1));
	}

	public int getNumberOfMatches(final String html)
	{
		final String text = html.replaceAll("\\<.*?>","");
		final Pattern pattern = Pattern.compile("\\((\\d*)-(\\d*)\\)\\s*Match W/L");
		final Matcher matcher = pattern.matcher(text);
		matcher.find();
		return Integer.parseInt(matcher.group(1)) + Integer.parseInt(matcher.group(2));
	}

	public double findStat(final String html, final String stat)
	{
		final String text = html.replaceAll("\\<.*?>","");
		final Pattern pattern = Pattern.compile("([\\d\\.]+)%?.*\\s*" + stat);
		final Matcher matcher = pattern.matcher(text);
		matcher.find();
		return Double.parseDouble(matcher.group(1));
	}

	public double findTourAverage(final String html, final String stat)
	{
		final String text = html.replaceAll("\\<.*?>","");
		final Pattern pattern = Pattern.compile(stat + "\\s*([\\d\\.]+)");
		final Matcher matcher = pattern.matcher(text);
		matcher.find();
		return Double.parseDouble(matcher.group(1));
	}

	public List<String> getPreviousOpponentsDefeated(final String html)
	{
		final List<String> players = new ArrayList<String>();
		final String text = html.replaceAll("\\<.*?>","");
		final Pattern pattern = Pattern.compile("Def\\. (\\(\\w*\\))*(.*?)\\(");
		final Matcher matcher = pattern.matcher(text);
		while (matcher.find())
		{
			players.add(matcher.group(matcher.groupCount()));
		}
		return players;
	}

	public List<Integer> getVictoryIds(final String html)
	{
		final List<Integer> ids = new ArrayList<Integer>();
		final Pattern pattern = Pattern.compile("Def\\..*?match_id=(\\d*)");
		final Matcher matcher = pattern.matcher(html);
		while (matcher.find())
		{
			ids.add(Integer.parseInt(matcher.group(1)));
		}
		return ids;
	}

	public List<String> getPreviousOpponentsLostTo(final String html)
	{
		final List<String> players = new ArrayList<String>();
		final String text = html.replaceAll("\\<.*?>","");
		final Pattern pattern = Pattern.compile("Lost to (\\(\\w*\\))?(.*?)\\(");
		final Matcher matcher = pattern.matcher(text);
		while (matcher.find())
		{
			players.add(matcher.group(matcher.groupCount()));
		}
		return players;
	}

	public List<Integer> getDefeatIds(final String html)
	{
		final List<Integer> ids = new ArrayList<Integer>();
		final Pattern pattern = Pattern.compile("Lost to.*?match_id=(\\d*)");
		final Matcher matcher = pattern.matcher(html);
		while (matcher.find())
		{
			ids.add(Integer.parseInt(matcher.group(1)));
		}
		return ids;
	}
}
