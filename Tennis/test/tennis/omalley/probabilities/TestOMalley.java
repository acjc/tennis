package tennis.omalley.probabilities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class TestOMalley {

	@Test
	public void testGame()
	{
		assertThat(OMalley.game(0.0), equalTo(0.0));
		assertThat(OMalley.game(0.5), equalTo(0.5));
		assertThat(OMalley.game(1.0), equalTo(1.0));
	}
}
