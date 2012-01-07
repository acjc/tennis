package tennis.data;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

public class TestDataDownloader
{
	private final DataDownloader downloader = new DataDownloader();

	@Test
	public void testPrintCookies() throws IOException
	{
		downloader.printCookies();
	}

	// Tournament ID: 17220 = Qatar Open 2012
	@Test
	public void testDownloadTournamentData() throws MalformedURLException, IOException
	{
		final String tournamentData = downloader.downloadTournamentData(17220);

		assertThat(tournamentData, containsString("TennisInsight.com"));
		assertThat(tournamentData, containsString("Doha ATP"));
	}

	// All Matches = 1
	// Player ID: 1 = Roger Federer
	@Test
	public void testGetPlayerActivity() throws IOException
	{
		final String activity = downloader.downloadPlayerActivity(1, ActivitySurface.ALL);

		assertThat(activity, containsString("TennisInsight.com"));
		assertThat(activity, containsString("Last 1-50 Activity - Roger Federer - All Matches with Any Odds"));
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
	public void testDownloadPlayerStats() throws MalformedURLException, IOException
	{
		final String overview = downloader.downloadPlayerOverview(1, Surface.GRASS);

		assertThat(overview, containsString("TennisInsight.com"));
		assertThat(overview, containsString("Roger Federer"));
		assertThat(overview, containsString("<option value=\"380\" selected=\"selected\">1 year</option>"));
	}

	// A match involving Jo-Wilfried Tsonga and John Isner
	@Test
	public void testDownloadMatchStats() throws MalformedURLException, IOException
	{
		final String matchData = downloader.downloadMatchData(172004201);

		assertThat(matchData, containsString("TennisInsight.com"));
		assertThat(matchData, containsString("Jo-Wilfried Tsonga"));
		assertThat(matchData, containsString("Roger Federer"));
	}
}
