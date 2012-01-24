package tennis.charts.treatment;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.ThreeSetLpmChart;

public class JankovicDokic
{
	public static void main(final String[] args) throws IOException
	{
		final ThreeSetLpmChart chart = new ThreeSetLpmChart("Jankovic vs Dokic (Us Open 2011 Second Round)",
															"doc\\jankovic_dokic\\Jelena Jankovic.csv", "doc\\jankovic_dokic\\Jankovic 2 - 0.csv",
																				   					    "doc\\jankovic_dokic\\Jankovic 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
