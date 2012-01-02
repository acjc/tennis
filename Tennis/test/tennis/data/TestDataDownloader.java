package tennis.data;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Ignore;
import org.junit.Test;

public class TestDataDownloader
{
	private final DataDownloader downloader = new DataDownloader();

	@Ignore
	@Test
	public void testPrintCookies() throws IOException
	{
		downloader.printCookies();
	}

	// All Matches = 1
	// Player ID: 1 = Roger Federer
	@Test
	public void testGetPlayerActivity() throws IOException
	{
		assertThat(downloader.downloadPlayerActivity(1), containsString("Last 1-50 Activity - Roger Federer - All Matches with Any Odds"));
	}

	@Test
	public void testFindPlayerByName() throws MalformedURLException, IOException
	{
		final String playerHtml = downloader.downloadPlayerProfile("Jo-Wilfried Tsonga");

		assertThat(playerHtml, containsString("TennisInsight.com"));
		assertThat(playerHtml, containsString("Jo-Wilfried Tsonga"));
	}

	// One Year = 380
	// Player ID: 1 = Roger Federer
	@Test
	public void testDownloadPlayerStatsOneYear() throws MalformedURLException, IOException
	{
		assertThat(downloader.downloadPlayerOverview(1), containsString("<option value=\"380\" selected=\"selected\">1 year</option>"));
	}

	// Two matches involving Jo-Wilfried Tsonga
	@Test
	public void testDownloadMatchStats() throws MalformedURLException, IOException
	{
		assertThat(downloader.downloadMatchData("http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=352&y=2011&r=6&p=T786", 171834501),
				   containsString("Jo-Wilfried Tsonga"));
		assertThat(downloader.downloadMatchData("http://tennisinsight.com/match_stats_popup.php?matchID=172004201", 172004201),
				   containsString("Jo-Wilfried Tsonga"));
	}
}
