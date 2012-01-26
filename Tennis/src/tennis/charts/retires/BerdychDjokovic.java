package tennis.charts.retires;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.ThreeSetLpmChart;

public class BerdychDjokovic
{
	public static void main(final String[] args) throws IOException
	{
		final ThreeSetLpmChart chart = new ThreeSetLpmChart("Berdych vs Djokovic (Dubai Championships 2011 Semi Final)",
															"doc\\berdych_djokovic\\Tomas Berdych.csv", "doc\\berdych_djokovic\\Berdych 2 - 0.csv",
																				   					    "doc\\berdych_djokovic\\Berdych 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
