package tennis.data;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class TestPlayerDataRetriever
{
	private final PlayerDataRetriever retriever = new PlayerDataRetriever();

	@Test
	public void testDownloadFile() throws MalformedURLException, IOException
	{
		retriever.downloadFile(new URL("http://www.tennisinsight.com/player_search_action.php?player_search=Jo-Wilfried Tsonga"));
		final String playerHtml = FileUtils.readFileToString(new File("doc\\player.html"));
		assertThat(playerHtml, containsString("TennisInsight.com"));
		assertThat(playerHtml, containsString("Jo-Wilfried Tsonga"));
	}

	@Test
	public void testGetPlayerId() throws MalformedURLException, IOException
	{
		assertThat(retriever.getPlayerId("test_test?player_id=777\" test"), equalTo(777));
	}
}
