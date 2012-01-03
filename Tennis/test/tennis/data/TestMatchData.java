package tennis.data;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Test;

public class TestMatchData
{
	private final MatchData matchData1;

	public TestMatchData() throws MalformedURLException, IOException
	{
		matchData1 = new MatchData(171834501, "Jo-Wilfried Tsonga", "John Isner");
		matchData1.downloadMatchData();
	}

	@Test
	public void testCheckIfPlayerIsFirst()
	{
		assertThat(matchData1.checkIfPlayerFirst("Jo-Wilfried Tsonga", "John Isner"), equalTo(true));
		assertThat(matchData1.checkIfPlayerFirst("John Isner", "Jo-Wilfried Tsonga"), equalTo(false));
	}

	@Test
	public void testFindStat()
	{
		assertThat(matchData1.findStat("1st Serve Percentage"), equalTo(57.0));
	}
}
