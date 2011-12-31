package tennis.data;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;

public class TestPlayerDataRetriever
{
	private final PlayerDataRetriever retriever = new PlayerDataRetriever();

	@Ignore
	@Test
	public void testPrintCookies() throws IOException
	{
		retriever.printCookies();
	}

	@Test
	public void testGetNumberOfMatches() throws IOException
	{
		assertThat(retriever.getNumberOfMatches("<test>   \n" +
												"<test>84%    (61-12)<test>\n" +
												"  <test>Match W/L<test>"), equalTo(73));
	}

	@Test
	public void testFindStat() throws IOException
	{
		assertThat(retriever.findStat("<test>   \n" +
									  "<test>5.84%    (61-12)<test>\n" +
									  "  <test>Stat<test>", "Stat"), equalTo("5.84%"));
	}

	@Test
	public void testFindPlayerByName() throws MalformedURLException, IOException
	{
		final File file = new File("doc\\player.html");
		retriever.downloadFile(new URL("http://www.tennisinsight.com/player_search_action.php?player_search=Jo-Wilfried Tsonga"), file);
		final String playerHtml = retriever.readFileToString(file);

		assertThat(playerHtml, containsString("TennisInsight.com"));
		assertThat(playerHtml, containsString("Jo-Wilfried Tsonga"));
	}

	// One Year = 380
	// Player ID: 1 = Roger Federer
	@Test
	public void testGetPlayerStatsOneYear() throws MalformedURLException, IOException
	{
		final File file = new File("doc\\player.html");
		retriever.downloadFile(new URL("http://www.tennisinsight.com/player_overview.php?player_id=1&duration=380"), file);
		final String playerHtml = retriever.readFileToString(file);

		assertThat(playerHtml, containsString("<option value=\"380\" selected=\"selected\">1 year</option>"));
	}

	@Test
	public void testGetPlayerId() throws MalformedURLException, IOException
	{
		assertThat(retriever.getPlayerId("test_test?player_id=777\" test"), equalTo(777));
	}
}
