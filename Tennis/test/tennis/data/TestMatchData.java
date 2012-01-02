package tennis.data;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

public class TestMatchData
{
	private final MatchData matchData1;
	private final MatchData matchData2;

	public TestMatchData() throws MalformedURLException, IOException
	{
		matchData1 = new MatchData("http://www.atpworldtour.com/Share/Match-Facts-Pop-Up.aspx?t=352&y=2011&r=6&p=T786", 171834501,
								   "Jo-Wilfried Tsonga", "John Isner");
		matchData2 = new MatchData("http://tennisinsight.com/match_stats_popup.php?matchID=172004201", 172004201,
								   "Jo-Wilfried Tsonga", "Roger Federer");
	}

	@Test
	public void testCheckIfPlayerIsFirst()
	{
		assertThat(matchData1.checkIfPlayerFirst("Jo-Wilfried Tsonga", "John Isner"), equalTo(true));
		assertThat(matchData1.checkIfPlayerFirst("John Isner", "Jo-Wilfried Tsonga"), equalTo(false));
		assertThat(matchData2.checkIfPlayerFirst("Jo-Wilfried Tsonga", "Roger Federer"), equalTo(false));
		assertThat(matchData2.checkIfPlayerFirst("Roger Federer", "Jo-Wilfried Tsonga"), equalTo(true));
	}

	@Test
	public void testFindStat()
	{
		assertThat(matchData1.findStat("1st Serve"), equalTo(56.0));
		assertThat(matchData2.findStat("1st Serve"), equalTo(50.0));
	}
}
