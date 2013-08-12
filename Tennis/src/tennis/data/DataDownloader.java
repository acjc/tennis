package tennis.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FileUtils;

public class DataDownloader
{
	public String readFileToString(final File file) throws IOException
	{
		return FileUtils.readFileToString(file);
	}

	public String downloadPlayerProfile(final String name) throws IOException
	{
		final File file = new File("doc\\data\\player.html");
		downloadFile(new URL("http://www.tennisinsight.com/player_search_action.php?player_search=" + name.replace(" ", "%20")), file);
		return readFileToString(file);
	}

	public String downloadTournamentData(final int id) throws MalformedURLException, IOException
	{
		System.out.println("Downloading data for tournament: " + id);
		final File file = new File("doc\\data\\tournament-" + id + ".html");
		downloadFile(new URL("http://www.tennisinsight.com/tournamentid" + id + ".htm"), file);
		return readFileToString(file);
	}

	public String downloadPlayerOverview(final int id, final Surface surface) throws IOException
	{
		final File file = new File("doc\\data\\player.html");
		downloadFile(new URL("http://www.tennisinsight.com/player_overview.php?player_id=" + id + "&surface=" + surface.getValue() + "&duration=380"), file);
		return readFileToString(file);
	}

	public String downloadPlayerActivity(final int id, final ActivitySurface surface) throws MalformedURLException, IOException
	{
		final File file = new File("doc\\data\\activity.html");
		downloadFile(new URL("http://www.tennisinsight.com/player_activity.php?player_id="+ id + "&activity=" + surface.getValue()), file);
		return readFileToString(file);
	}

	public String downloadMatchData(final int id) throws MalformedURLException, IOException
	{
		System.out.println("Downloading statistics for match: " + id);
		final File file = new File("doc\\data\\match-" + id + ".html");
		if (!file.isFile())
		{
			downloadFile(new URL("http://www.tennisinsight.com/match_stats_popup.php?matchID=" + id), file);
		}
		return readFileToString(file);
	}

	public void printCookies() throws IOException
	{
		final URL myUrl = new URL("http://www.tennisinsight.com/");
		final URLConnection connection = myUrl.openConnection();
		connection.connect();

		String headerName;
		for (int i = 1; (headerName = connection.getHeaderFieldKey(i)) != null; i++)
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
}
