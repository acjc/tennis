package tennis.data;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.junit.Test;

public class TestPlayerDataParser
{
	private final PlayerDataParser parser = new PlayerDataParser();

	@Test
	public void testGetPlayerId() throws MalformedURLException, IOException
	{
		assertThat(parser.getPlayerId("activity.php?player_id=777' test"), equalTo(777));
	}

	@Test
	public void testGetTournamentId() throws IOException
	{
		final String html = "<test>Next Match<test>test<test=tournamentid777.htm>test";
		assertThat(parser.inTournament(html), equalTo(true));
		assertThat(parser.getTournamentId(html), equalTo(777));
	}

	@Test
	public void testGetTournamentName() throws IOException
	{
		assertThat(parser.getTournamentName("<test=\"fontMainTitle\">Tournie<test>"), equalTo("Tournie"));
	}

	@Test
	public void testGetNumberOfMatches() throws IOException
	{
		assertThat(parser.getNumberOfMatches("<test>   \n" +
												"<test>84%    (61-12)<test>\n" +
												"  <test>Match W/L<test>"), equalTo(73));
	}

	@Test
	public void testFindStat() throws IOException
	{
		assertThat(parser.findStat("<test>   \n" +
									  "<test>5.84%    (61-12)<test>\n" +
									  "  <test>1st Stat %<test>", "1st Stat %"), equalTo(5.84));
	}

	@Test
	public void testFindTourAverage() throws IOException
	{
		assertThat(parser.findTourAverage("<test>5.84%    (61-12)<test>\n" +
											 "  <test>Stat<test>\n" +
											 "<test>50.57%    (265-250)<test>", "Stat"), equalTo(50.57));
	}

	@Test
	public void testGetOpponentsDefeated()
	{
		final List<String> players = parser.getPreviousOpponentsDefeated("<test>Def. (2W)<test>Novak Djokovic<test>(2,6.23)" +
																   "<test>Def. (2W)<test>Novak Djokovic<test>(2,6.23)" +
																   "<test>Def. <test>Andy Murray<test>(4,5.57)" +
																   "<test>Lost to <test>Roger Federer<test>(3,3.94)");

		assertThat(players.size(), equalTo(3));
		assertThat(players, contains("Novak Djokovic", "Novak Djokovic", "Andy Murray"));
	}

	@Test
	public void testGetVictoryIds() throws MalformedURLException, IOException
	{
		final List<Integer> ids = parser.getVictoryIds("<test>Def. <test>    <test?match_id=777>test" +
														 "<test>Def. <test>    <test?match_id=888>test" +
														 "<test>Lost to <test>    <test?match_id=999>test");

		assertThat(ids.size(), equalTo(2));
		assertThat(ids, contains(777, 888));
	}

	@Test
	public void testGetOpponentsLostTo()
	{
		final List<String> players = parser.getPreviousOpponentsLostTo("<test>Lost to (2W)<test>Novak Djokovic<test>(2,6.23)" +
																 "<test>Lost to (2W)<test>Novak Djokovic<test>(2,6.23)" +
																 "<test>Lost to <test>Andy Murray<test>(4,5.57)" +
																 "<test>Def. <test>Roger Federer<test>(3,3.94)");

		assertThat(players.size(), equalTo(3));
		assertThat(players, contains("Novak Djokovic", "Novak Djokovic", "Andy Murray"));
	}
	@Test
	public void testGetDefeatIds() throws MalformedURLException, IOException
	{
		final List<Integer> ids = parser.getDefeatIds("<test>Lost to <test>    <test?match_id=777>test" +
														"<test>Lost to <test>    <test?match_id=888>test" +
														"<test>Def. <test>    <test?match_id=999>test");

		assertThat(ids.size(), equalTo(2));
		assertThat(ids, contains(777, 888));
	}
}
