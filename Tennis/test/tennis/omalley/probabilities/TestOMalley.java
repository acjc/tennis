package tennis.omalley.probabilities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static tennis.omalley.probabilities.OMalley.game;
import static tennis.omalley.probabilities.OMalley.setInPlay;

import org.junit.Test;

public class TestOMalley {

	@Test
	public void testGame()
	{
		assertThat(game(0.0), equalTo(0.0));
		assertThat(game(0.5), equalTo(0.5));
		assertThat(game(1.0), equalTo(1.0));
	}

	@Test
	public void testSetInPlay()
	{
//		System.out.println(OMalley.setInPlay(0.6, 0.5, 3, 3, true));
		assertThat(setInPlay(0.5, 0.5, 5, 5, true), equalTo(0.5));
		assertThat(setInPlay(0.5, 0.5, 5, 5, false), equalTo(0.5));
		assertThat(setInPlay(0.6, 0.4, 4, 5, true), greaterThan(setInPlay(0.6, 0.4, 4, 5, false)));
	}
}
