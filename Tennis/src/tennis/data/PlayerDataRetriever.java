package tennis.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class PlayerDataRetriever
{
	public int getPlayerId(final String playerHtml) throws IOException
	{
		final Pattern pattern = Pattern.compile("player_id=(\\d*)");
		final Matcher matcher = pattern.matcher(playerHtml);
		matcher.find();
		return Integer.parseInt(matcher.group(1));
	}

	public String readFileToString(final File file) throws IOException
	{
		return FileUtils.readFileToString(file);
	}

	public Set<String> getOpponentsDefeated(final String html)
	{
		final Set<String> players = new HashSet<String>();
		final String text = html.replaceAll("\\<.*?>","");
		final Pattern pattern = Pattern.compile("Def\\. (\\(\\w*\\))*(.*?)\\(");
		final Matcher matcher = pattern.matcher(text);
		while (matcher.find())
		{
			players.add(matcher.group(matcher.groupCount()));
		}
		return players;
	}

	public Set<Integer> getVictoryIds(final String html)
	{
		final Set<Integer> ids = new HashSet<Integer>();
		final Pattern pattern = Pattern.compile("Def\\..*?match_id=(\\d*)");
		final Matcher matcher = pattern.matcher(html);
		while (matcher.find())
		{
			ids.add(Integer.parseInt(matcher.group(1)));
		}
		return ids;
	}

	public Set<Integer> getDefeatIds(final String html)
	{
		final Set<Integer> ids = new HashSet<Integer>();
		final Pattern pattern = Pattern.compile("Lost to.*?match_id=(\\d*)");
		final Matcher matcher = pattern.matcher(html);
		while (matcher.find())
		{
			ids.add(Integer.parseInt(matcher.group(1)));
		}
		return ids;
	}

	public String downloadPlayerProfile(final String name) throws IOException
	{
		final File file = new File("doc\\player.html");
		downloadFile(new URL("http://www.tennisinsight.com/player_search_action.php?player_search=" + name), file);
		return readFileToString(file);
	}

	public Set<String> getOpponentsLostTo(final String html)
	{
		final Set<String> players = new HashSet<String>();
		final String text = html.replaceAll("\\<.*?>","");
		final Pattern pattern = Pattern.compile("Lost to (\\(\\w*\\))?(.*?)\\(");
		final Matcher matcher = pattern.matcher(text);
		while (matcher.find())
		{
			players.add(matcher.group(matcher.groupCount()));
		}
		return players;
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

	public String downloadPlayerStatistics(final int id) throws IOException
	{
		final File file = new File("doc\\player.html");
		downloadFile(new URL("http://www.tennisinsight.com/player_overview.php?player_id=" + id + "&duration=380"), file);
		return readFileToString(file);
	}

	public String downloadPlayerActivity(final int id) throws MalformedURLException, IOException
	{
		final File file = new File("doc\\activity.html");
		downloadFile(new URL("http://www.tennisinsight.com/player_activity.php?player_id="+ id + "&activity=1"), file);
		return readFileToString(file);
	}

	public void printCookies() throws IOException
	{
		final URL myUrl = new URL("http://www.tennisinsight.com/");
		final URLConnection connection = myUrl.openConnection();
		connection.connect();

		String headerName = null;
		for (int i=1; (headerName = connection.getHeaderFieldKey(i))!=null; i++)
		{
		 	if (headerName.equals("Set-Cookie"))
		 	{
		 		String cookie = connection.getHeaderField(i);
		 		cookie = cookie.substring(0, cookie.indexOf(";"));
		        System.out.println("Name: " + cookie.substring(0, cookie.indexOf("=")) + ", " +
		        				   "Value: " + cookie.substring(cookie.indexOf("=") + 1, cookie.length()));
		 	}
		}
	}

	public void downloadFile(final URL theUrl, final File file) throws IOException
	{
		URLConnection connection;
		connection = theUrl.openConnection();
		final String cookies = "cookTube4=Better+Off; cookname=debapriyapal; cookpass=enigmatic";
		connection.setRequestProperty("Cookie", cookies);
		connection.connect();

		System.out.println("Downloading: " + theUrl.toString());

		final String type = connection.getContentType();
		if (type != null)
		{
			final byte[] buffer = new byte[4 * 1024];
			int read;

			final FileOutputStream os = new FileOutputStream(file);
			final InputStream in = connection.getInputStream();

			while ((read = in.read(buffer)) > 0) {
				os.write(buffer, 0, read);
			}

			os.close();
			in.close();
		}

	}

	public Set<String> getVictoryStatistics(final String html)
	{
		return null;
	}
}
