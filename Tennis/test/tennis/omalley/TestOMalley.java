package tennis.omalley;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static tennis.omalley.OMalley.game;
import static tennis.omalley.OMalley.set;
import static tennis.omalley.OMalley.setInPlay;

import java.text.DecimalFormat;

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
	public void testMatch()
	{
		System.out.println(OMalley.bestOfFive(0.55, 0.55));
	}

	@Test
	public void testSetInPlay()
	{
		assertThat(round(setInPlay(0.592, 0.435, 0, 0, false)), equalTo(round(set(0.592, 0.435))));

		assertThat(setInPlay(0.5, 0.5, 5, 5, true), equalTo(0.5));
		assertThat(setInPlay(0.5, 0.5, 5, 5, false), equalTo(0.5));

		assertThat(setInPlay(0.5, 0.5, 0, 0, true), equalTo(0.5));
		assertThat(setInPlay(0.5, 0.5, 0, 0, false), equalTo(0.5));

		assertThat(round(setInPlay(0.61, 0.37, 0, 0, true)), equalTo(round(setInPlay(0.61, 0.37, 0, 0, false))));
		assertThat(setInPlay(0.6, 0.4, 4, 5, true), greaterThan(setInPlay(0.6, 0.4, 4, 5, false)));

		assertThat(round(setInPlay(0.67, 0.38, 3, 5, false)), equalTo(round(0.116)));
		assertThat(round(setInPlay(0.62, 0.33, 3, 5, true)), equalTo(round(0.043)));

		assertThat(round(setInPlay(0.67, 0.38, 1, 4, true)), equalTo(0.175));
		assertThat(round(setInPlay(0.62, 0.33, 1, 4, true)), equalTo(0.057));

		assertThat(round(setInPlay(0.67, 0.38, 2, 3, false)), equalTo(0.287));
		assertThat(round(setInPlay(0.62, 0.33, 2, 3, false)), equalTo(0.112));

		assertThat(round(setInPlay(0.67, 0.38, 1, 3, false)), equalTo(0.255));
		assertThat(round(setInPlay(0.62, 0.33, 1, 3, false)), equalTo(0.090));

		assertThat(round(setInPlay(0.67, 0.38, 0, 3, true)), equalTo(0.220));
		assertThat(round(setInPlay(0.62, 0.33, 0, 3, true)), equalTo(0.070));
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.###").format(value));
	}
}
