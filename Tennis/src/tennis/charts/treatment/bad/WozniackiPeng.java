package tennis.charts.treatment.bad;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.lpm.ThreeSetLpmChart;

public class WozniackiPeng
{
	public static void main(final String[] args) throws IOException
	{
		final ThreeSetLpmChart chart = new ThreeSetLpmChart("Wozniacki vs Peng - Brussels Open 2011 Final",
															"doc\\wozniacki_peng\\Caroline Wozniacki.csv", "doc\\wozniacki_peng\\Wozniacki 2 - 0.csv",
																				   						   "doc\\wozniacki_peng\\Wozniacki 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
