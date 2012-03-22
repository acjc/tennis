package tennis.omalley;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static tennis.omalley.OMalley.*;

import java.text.DecimalFormat;

import org.junit.Test;

public class TestOMalley
{
	@Test
	public void testGame()
	{
		assertThat(game(0.0), equalTo(0.0));
		assertThat(game(0.5), equalTo(0.5));
		assertThat(game(1.0), equalTo(1.0));
	}

	@Test
	public void testMatchInProgress()
	{
		assertThat(round(matchInProgress(0.67, 0.38, 3)), equalTo(round(bestOfFive(0.67, 0.38))));
		assertThat(round(matchInProgress(0.67, 0.38, 2)), equalTo(round(bestOfThree(0.67, 0.38))));

		assertThat(round(matchInProgress(0.55, 0.55, 3)), equalTo(0.953));

		assertThat(matchInProgress(0.5, 0.5, 3), equalTo(0.5));
	}

	@Test
	public void testGameInProgress()
	{
		assertThat(round(gameInProgress(0.592)), equalTo(round(game(0.592))));

		assertThat(gameInProgress(0.5), equalTo(0.5));
		assertThat(round(gameInProgress(0.6)), equalTo(0.736));
		assertThat(round(gameInProgress(0.7)), equalTo(0.901));
	}

	@Test
	public void testSetInProgress()
	{
		assertThat(round(setInProgress(0.592, 0.435, false)), equalTo(round(set(0.592, 0.435))));

		assertThat(setInProgress(0.5, 0.5, new CurrentSetScore(5, 5), true), equalTo(0.5));
		assertThat(setInProgress(0.5, 0.5, new CurrentSetScore(5, 5), false), equalTo(0.5));

		assertThat(setInProgress(0.5, 0.5, true), equalTo(0.5));
		assertThat(setInProgress(0.5, 0.5, false), equalTo(0.5));

		assertThat(round(setInProgress(0.61, 0.37, true)), equalTo(round(setInProgress(0.61, 0.37, false))));

		assertThat(setInProgress(0.6, 0.4, new CurrentSetScore(4, 5), true), greaterThan(setInProgress(0.6, 0.4, new CurrentSetScore(4, 5), false)));

		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(3, 5), false)), equalTo(round(0.116)));
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(3, 5), true)), equalTo(round(0.043)));

		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(1, 4), true)), equalTo(0.175));
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(1, 4), true)), equalTo(0.057));

		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(2, 3), false)), equalTo(0.287));
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(2, 3), false)), equalTo(0.112));

		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(1, 3), false)), equalTo(0.255));
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(1, 3), false)), equalTo(0.090));

		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(0, 3), true)), equalTo(0.220));
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(0, 3), true)), equalTo(0.070));
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.###").format(value));
	}
}
