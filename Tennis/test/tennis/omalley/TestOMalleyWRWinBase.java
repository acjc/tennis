package tennis.omalley;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import java.text.DecimalFormat;

import org.junit.Test;

import tennis.simulator.RetirementRisk;

public class TestOMalleyWRWinBase
{
	private final OMalleyWRWin omalley = new OMalleyWRWin(1000.0, 0.85, false);

	@Test
	public void testMatchInProgress()
	{
		// Test against pre-play formulae
		assertThat(round(omalley.matchInProgress(0.67, 0.62, 3)), equalTo(round(OMalley.bestOfFive(0.67, 0.62))));
		assertThat(round(omalley.matchInProgress(0.67, 0.62, 2)), equalTo(round(OMalley.bestOfThree(0.67, 0.62))));

		// Liu results
		assertThat(round(omalley.matchInProgress(0.55, 0.45, 3)), equalTo(0.953));
		assertThat(round(omalley.matchInProgress(0.51, 0.49, 3)), equalTo(0.632));

		// Barnett, Brown, Clarke results
		assertThat(round(omalley.matchInProgress(0.52, 0.48, 3)), equalTo(0.749));
		assertThat(round(omalley.matchInProgress(0.54, 0.46, 3)), equalTo(0.910));

		// Huang results
		assertThat(round(omalley.matchInProgress(0.65, 0.54, 2)), equalTo(0.923)); // 0.549
		assertThat(round(omalley.matchInProgress(0.58, 0.62, new RetirementRisk(), new CurrentMatchScore(1, 0), new CurrentSetScore(5, 1), new CurrentGameScore(), false, 2)), equalTo(0.979)); // 0.441
		assertThat(round(omalley.matchInProgress(0.56, 0.56, new RetirementRisk(), new CurrentMatchScore(1, 1), new CurrentSetScore(4, 3), new CurrentGameScore(), true, 2)), equalTo(0.750)); // 0.841
		assertThat(round(omalley.matchInProgress(0.60, 0.57, new RetirementRisk(), new CurrentMatchScore(0, 1), new CurrentSetScore(), new CurrentGameScore(), true, 2)), equalTo(0.364));

		// Basic match assertion
		assertThat(omalley.matchInProgress(0.50, 0.50, 3), equalTo(0.5));
	}

	@Test
	public void testGameInProgress()
	{
		// Test against pre-play formulae
		assertThat(round(omalley.gameInProgress(0.592, 0.592, new RetirementRisk(), true)), equalTo(round(OMalley.game(0.592))));

		// Basic game assertions
		assertThat(omalley.gameInProgress(0.00), equalTo(0.0));
		assertThat(omalley.gameInProgress(0.50), equalTo(0.5));
		assertThat(omalley.gameInProgress(1.00), equalTo(1.0));

		// O'Malley results
		assertThat(round(omalley.gameInProgress(0.60)), equalTo(0.736));
		assertThat(round(omalley.gameInProgress(0.70)), equalTo(0.901));

		// Barnett, Brown, Clarke results
		assertThat(round(omalley.gameInProgress(0.54)), equalTo(0.599));
		assertThat(round(omalley.gameInProgress(0.58)), equalTo(0.693));
		assertThat(round(omalley.gameInProgress(0.64)), equalTo(0.813));

		assertThat(round(omalley.gameInProgress(0.54, 0.50, new RetirementRisk(), new CurrentGameScore(0, 1), true)), equalTo(0.435));
		assertThat(round(omalley.gameInProgress(0.54, 0.50, new RetirementRisk(), new CurrentGameScore(2, 1), true)), equalTo(0.759));
		assertThat(round(omalley.gameInProgress(0.54, 0.50, new RetirementRisk(), new CurrentGameScore(1, 3), true)), equalTo(0.169));
	}

	@Test
	public void testSetInProgress()
	{
		// Test against pre-play formulae
		assertThat(round(omalley.setInProgress(0.592, 0.565, true)), equalTo(round(OMalley.set(0.592, 0.565))));
		assertThat(round(omalley.setInProgress(0.592, 0.565, false)), equalTo(round(OMalley.set(0.592, 0.565))));

		// From 5-all equal chance
		assertThat(omalley.setInProgress(0.5, 0.5, new CurrentSetScore(5, 5), true), equalTo(0.5));
		assertThat(omalley.setInProgress(0.5, 0.5, new CurrentSetScore(5, 5), false), equalTo(0.5));

		// Basic set assertions
		assertThat(omalley.setInProgress(0.5, 0.5, true), equalTo(0.5));
		assertThat(omalley.setInProgress(0.5, 0.5, false), equalTo(0.5));

		// First server doesn't matter pre-play ever
		assertThat(round(omalley.setInProgress(0.61, 0.63, true)), equalTo(round(omalley.setInProgress(0.61, 0.63, false))));

		// First server does matter in-play sometimes
		assertThat(omalley.setInProgress(0.6, 0.6, new CurrentSetScore(4, 5), true), greaterThan(omalley.setInProgress(0.6, 0.6, new CurrentSetScore(4, 5), false)));
		assertThat(omalley.setInProgress(0.6, 0.6, new CurrentSetScore(5, 3), true), equalTo(omalley.setInProgress(0.6, 0.6, new CurrentSetScore(5, 3), false)));

		// Barnett, Brown, Clarke results
		assertThat(round(omalley.setInProgress(0.54, 0.46, true)), equalTo(round(0.763)));

		// O'Malley break down results - some values are different
		assertThat(round(omalley.setInProgress(0.67, 0.62, new CurrentSetScore(4, 5), false)), equalTo(round(0.135)));
		assertThat(round(omalley.setInProgress(0.62, 0.67, new CurrentSetScore(4, 5), false)), equalTo(round(0.056)));

		assertThat(round(omalley.setInProgress(0.67, 0.62, new CurrentSetScore(3, 5), true)), equalTo(round(0.116)));
		assertThat(round(omalley.setInProgress(0.67, 0.62, new CurrentSetScore(3, 5), false)), equalTo(round(0.116)));
		assertThat(round(omalley.setInProgress(0.62, 0.67, new CurrentSetScore(3, 5), true)), equalTo(round(0.043)));
		assertThat(round(omalley.setInProgress(0.62, 0.67, new CurrentSetScore(3, 5), false)), equalTo(round(0.043)));

		assertThat(round(omalley.setInProgress(0.67, 0.62, new CurrentSetScore(2, 5), true)), equalTo(round(0.100)));
		assertThat(round(omalley.setInProgress(0.62, 0.67, new CurrentSetScore(2, 5), true)), equalTo(round(0.033))); // 0.34

		assertThat(round(omalley.setInProgress(0.67, 0.62, new CurrentSetScore(3, 4), false)), equalTo(round(0.227)));
		assertThat(round(omalley.setInProgress(0.62, 0.67, new CurrentSetScore(3, 4), false)), equalTo(round(0.091)));

		assertThat(round(omalley.setInProgress(0.67, 0.62, new CurrentSetScore(2, 4), true)), equalTo(round(0.199)));
		assertThat(round(omalley.setInProgress(0.67, 0.62, new CurrentSetScore(2, 4), false)), equalTo(round(0.199)));
		assertThat(round(omalley.setInProgress(0.62, 0.67, new CurrentSetScore(2, 4), true)), equalTo(round(0.072)));
		assertThat(round(omalley.setInProgress(0.62, 0.67, new CurrentSetScore(2, 4), false)), equalTo(round(0.072)));

		assertThat(round(omalley.setInProgress(0.67, 0.62, new CurrentSetScore(1, 4), true)), equalTo(0.175));
		assertThat(round(omalley.setInProgress(0.62, 0.67, new CurrentSetScore(1, 4), true)), equalTo(0.057));

		assertThat(round(omalley.setInProgress(0.67, 0.62, new CurrentSetScore(2, 3), false)), equalTo(0.295)); // 0.287
		assertThat(round(omalley.setInProgress(0.62, 0.67, new CurrentSetScore(2, 3), false)), equalTo(0.114)); // 0.112

		assertThat(round(omalley.setInProgress(0.67, 0.62, new CurrentSetScore(1, 3), true)), equalTo(0.262)); // 0.255
		assertThat(round(omalley.setInProgress(0.67, 0.62, new CurrentSetScore(1, 3), false)), equalTo(0.262)); // 0.255
		assertThat(round(omalley.setInProgress(0.62, 0.67, new CurrentSetScore(1, 3), true)), equalTo(0.092)); // 0.090
		assertThat(round(omalley.setInProgress(0.62, 0.67, new CurrentSetScore(1, 3), false)), equalTo(0.092)); // 0.090

		assertThat(round(omalley.setInProgress(0.67, 0.62, new CurrentSetScore(0, 3), true)), equalTo(0.233)); // 0.220
		assertThat(round(omalley.setInProgress(0.62, 0.67, new CurrentSetScore(0, 3), true)), equalTo(0.074)); // 0.070
	}

	private double round(final double value)
	{
		return Double.parseDouble(new DecimalFormat("#.###").format(value));
	}
}
