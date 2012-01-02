package tennis.data;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Set;

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
									  "  <test>Stat<test>", "Stat"), equalTo(5.84));
	}

	@Test
	public void testFindTourAverage() throws IOException
	{
		assertThat(retriever.findTourAverage("<test>5.84%    (61-12)<test>\n" +
											 "  <test>Stat<test>\n" +
											 "<test>50.57%    (265-250)<test>", "Stat"), equalTo(50.57));
	}

	// All Matches = 1
	// Player ID: 1 = Roger Federer
	@Test
	public void testGetPlayerActivity() throws IOException
	{
		assertThat(retriever.downloadPlayerActivity(1), containsString("Last 1-50 Activity - Roger Federer - All Matches with Any Odds"));
	}

	@Test
	public void testGetOpponentsDefeated()
	{
		final Set<String> players = retriever.getOpponentsDefeated("<test>Def. (2W)<test>Novak Djokovic<test>(2,6.23) " +
																   "<test>Def. (2W)<test>Novak Djokovic<test>(2,6.23) " +
																   "<test>Def. <test>Andy Murray<test>(4,5.57)  ");

		assertThat(players.size(), equalTo(2));
		assertThat(players, contains("Novak Djokovic", "Andy Murray"));
	}

	@Test
	public void testGetVictoryIds() throws MalformedURLException, IOException
	{
		final Set<Integer> ids = retriever.getVictoryIds("<test>Def. <test>    <test?match_id=777>test" +
														 "<test>Def. <test>    <test?match_id=888>test");
		assertThat(ids.size(), equalTo(2));
		assertThat(ids, contains(888, 777));
	}

	@Test
	public void testGetDefeatIds() throws MalformedURLException, IOException
	{
		final Set<Integer> ids = retriever.getDefeatIds("<test>Lost to <test>    <test?match_id=777>test" +
														"<test>Lost to <test>    <test?match_id=888>test");
		assertThat(ids.size(), equalTo(2));
		assertThat(ids, contains(888, 777));
	}

	@Test
	public void testGetOpponentsLostTo()
	{
		final Set<String> players = retriever.getOpponentsLostTo("<test>Lost to (2W)<test>Novak Djokovic<test>(2,6.23) " +
																 "<test>Lost to (2W)<test>Novak Djokovic<test>(2,6.23) " +
																 "<test>Lost to <test>Andy Murray<test>(4,5.57)  ");

		assertThat(players.size(), equalTo(2));
		assertThat(players, contains("Novak Djokovic", "Andy Murray"));
	}

	@Test
	public void testFindPlayerByName() throws MalformedURLException, IOException
	{
		final String playerHtml = retriever.downloadPlayerProfile("Jo-Wilfried Tsonga");

		assertThat(playerHtml, containsString("TennisInsight.com"));
		assertThat(playerHtml, containsString("Jo-Wilfried Tsonga"));
	}

	// One Year = 380
	// Player ID: 1 = Roger Federer
	@Test
	public void testDownloadPlayerStatsOneYear() throws MalformedURLException, IOException
	{
		assertThat(retriever.downloadPlayerStatistics(1), containsString("<option value=\"380\" selected=\"selected\">1 year</option>"));
	}

	@Test
	public void testGetPlayerId() throws MalformedURLException, IOException
	{
		assertThat(retriever.getPlayerId("test_test?player_id=777\" test"), equalTo(777));
	}
}
