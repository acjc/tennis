package tennis.charts.retires;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.lpm.LpmChart;

public class WozniackiArvidsson
{
	public static void main(final String[] args) throws IOException
	{
		final LpmChart chart = new LpmChart("Wozniacki vs Arvidsson - Collector Swedish Open 2011 Second Round",
															"doc\\wozniacki_arvidsson\\Caroline Wozniacki.csv", "doc\\wozniacki_arvidsson\\Wozniacki 2 - 0.csv",
																				   								"doc\\wozniacki_arvidsson\\Wozniacki 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
