package tennis.charts.retires;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.lpm.LpmChart;

public class DjokovicMurray
{
	public static void main(final String[] args) throws IOException
	{
		final LpmChart chart = new LpmChart("Djokovic vs Murray - Cincinnati 2011 Final",
															"doc\\djokovic_murray\\Novak Djokovic.csv", "doc\\djokovic_murray\\Djokovic 2 - 0.csv",
																				   						"doc\\djokovic_murray\\Djokovic 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
