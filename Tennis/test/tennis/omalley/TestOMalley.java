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
		// Basic game assertions
		assertThat(game(0.0), equalTo(0.0));
		assertThat(game(0.5), equalTo(0.5));
		assertThat(game(1.0), equalTo(1.0));
	}

	@Test
	public void testMatchInProgress()
	{
		// Test against pre-play formulae
		assertThat(round(matchInProgress(0.67, 0.38, 3)), equalTo(round(bestOfFive(0.67, 0.38))));
		assertThat(round(matchInProgress(0.67, 0.38, 2)), equalTo(round(bestOfThree(0.67, 0.38))));

		// Liu results
		assertThat(round(matchInProgress(0.55, 0.55, 3)), equalTo(0.953));
		assertThat(round(matchInProgress(0.51, 0.51, 3)), equalTo(0.632));

		// Huang results
		assertThat(round(matchInProgress(0.60, 0.43, new CurrentMatchScore(1, 0), new CurrentSetScore(), new CurrentGameScore(), true, 2)), equalTo(0.842)); // 0.843
		assertThat(round(matchInProgress(0.65, 0.46, new CurrentMatchScore(1, 0), new CurrentSetScore(5, 1), new CurrentGameScore(), true, 2)), equalTo(0.999)); // 0.977
		assertThat(round(matchInProgress(0.56, 0.44, new CurrentMatchScore(1, 1), new CurrentSetScore(4, 3), new CurrentGameScore(), true, 2)), equalTo(0.750)); // 0.841
		assertThat(round(matchInProgress(0.55, 0.40, new CurrentMatchScore(0, 1), new CurrentSetScore(), new CurrentGameScore(), true, 2)), equalTo(0.109));

		// Basic match assertion
		assertThat(matchInProgress(0.5, 0.5, 3), equalTo(0.5));
	}

	@Test
	public void testGameInProgress()
	{
		// Test against pre-play formulae
		assertThat(round(gameInProgress(0.592)), equalTo(round(game(0.592))));

		// Basic game assertion
		assertThat(gameInProgress(0.5), equalTo(0.5));

		// O'Malley results
		assertThat(round(gameInProgress(0.6)), equalTo(0.736));
		assertThat(round(gameInProgress(0.7)), equalTo(0.901));
	}

	@Test
	public void testSetInProgress()
	{
		// Test against pre-play formulae
		assertThat(round(setInProgress(0.592, 0.435, true)), equalTo(round(set(0.592, 0.435))));
		assertThat(round(setInProgress(0.592, 0.435, false)), equalTo(round(set(0.592, 0.435))));

		// From 5-all equal chance
		assertThat(setInProgress(0.5, 0.5, new CurrentSetScore(5, 5), true), equalTo(0.5));
		assertThat(setInProgress(0.5, 0.5, new CurrentSetScore(5, 5), false), equalTo(0.5));

		// Basic set assertions
		assertThat(setInProgress(0.5, 0.5, true), equalTo(0.5));
		assertThat(setInProgress(0.5, 0.5, false), equalTo(0.5));

		// First server doesn't matter pre-play
		assertThat(round(setInProgress(0.61, 0.37, true)), equalTo(round(setInProgress(0.61, 0.37, false))));

		// First server does matter in-play
		assertThat(setInProgress(0.6, 0.4, new CurrentSetScore(4, 5), true), greaterThan(setInProgress(0.6, 0.4, new CurrentSetScore(4, 5), false)));

		// O'Malley break down results - some values are different
		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(4, 5), false)), equalTo(round(0.135)));
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(4, 5), false)), equalTo(round(0.056)));

		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(3, 5), true)), equalTo(round(0.116)));
		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(3, 5), false)), equalTo(round(0.116)));
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(3, 5), true)), equalTo(round(0.043)));
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(3, 5), false)), equalTo(round(0.043)));

		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(2, 5), true)), equalTo(round(0.100)));
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(2, 5), true)), equalTo(round(0.033))); // 0.34

		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(3, 4), false)), equalTo(round(0.227)));
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(3, 4), false)), equalTo(round(0.091)));

		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(2, 4), true)), equalTo(round(0.199)));
		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(2, 4), false)), equalTo(round(0.199)));
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(2, 4), true)), equalTo(round(0.072)));
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(2, 4), false)), equalTo(round(0.072)));

		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(1, 4), true)), equalTo(0.175));
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(1, 4), true)), equalTo(0.057));

		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(2, 3), false)), equalTo(0.295)); // 0.287
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(2, 3), false)), equalTo(0.114)); // 0.112

		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(1, 3), true)), equalTo(0.262)); // 0.255
		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(1, 3), false)), equalTo(0.262)); // 0.255
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(1, 3), true)), equalTo(0.092)); // 0.090
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(1, 3), false)), equalTo(0.092)); // 0.090

		assertThat(round(setInProgress(0.67, 0.38, new CurrentSetScore(0, 3), true)), equalTo(0.233)); // 0.220
		assertThat(round(setInProgress(0.62, 0.33, new CurrentSetScore(0, 3), true)), equalTo(0.074)); // 0.070
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.###").format(value));
	}
}
