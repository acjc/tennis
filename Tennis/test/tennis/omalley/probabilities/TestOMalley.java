package tennis.omalley.probabilities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class TestOMalley {

	OMalley oMalley = new OMalley();

	@Test
	public void testGame()
	{
		assertThat(oMalley.game(0.0), equalTo(0.0));
		assertThat(oMalley.game(0.5), equalTo(0.5));
		assertThat(oMalley.game(1.0), equalTo(1.0));
	}
}
