package tennis.charts.treatment;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.ThreeSetLpmChart;

public class WozniackiPeng
{
	public static void main(final String[] args) throws IOException
	{
		final ThreeSetLpmChart chart = new ThreeSetLpmChart("Wozniacki vs Peng (Brussles Open 2011 Final)",
															"doc\\wozniacki_peng\\Caroline Wozniacki.csv", "doc\\wozniacki_peng\\Wozniacki 2 - 0.csv",
																				   						   "doc\\wozniacki_peng\\Wozniacki 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}