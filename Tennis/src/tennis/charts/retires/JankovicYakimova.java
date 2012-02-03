package tennis.charts.retires;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.lpm.ThreeSetLpmChart;

public class JankovicYakimova
{
	public static void main(final String[] args) throws IOException
	{
		final ThreeSetLpmChart chart = new ThreeSetLpmChart("Jankovic vs Yakimova - Slovenia Open 2010 Second Round",
															"doc\\jankovic_yakimova\\Jelena Jankovic.csv", "doc\\jankovic_yakimova\\Jankovic 2 - 0.csv",
																				   					       "doc\\jankovic_yakimova\\Jankovic 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}