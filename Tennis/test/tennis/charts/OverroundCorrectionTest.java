package tennis.charts;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

public class OverroundCorrectionTest
{
	@Test
	public void testOverroundCorrection()
	{
		double overround = (1 / 1.5) + (1 / 2.4);
		assertThat(1 / (overround * 1.5) + 1 / (overround * 2.4), equalTo(1.0));

		overround = (1 / 2.2) + (1 / 2.4);
		assertThat(1 / (overround * 2.2) + 1 / (overround * 2.4), equalTo(1.0));
	}
}
