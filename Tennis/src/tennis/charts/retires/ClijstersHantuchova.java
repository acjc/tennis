package tennis.charts.retires;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.ThreeSetLpmChart;

public class ClijstersHantuchova
{
	public static void main(final String[] args) throws IOException
	{
		final ThreeSetLpmChart chart = new ThreeSetLpmChart("Clijsters vs Hantuchova (Brisbane International 2011 Semi Final)",
															"doc\\clijsters_hantuchova\\Kim Clijsters.csv", "doc\\clijsters_hantuchova\\Clijsters 2 - 0.csv",
																				   					     	"doc\\clijsters_hantuchova\\Clijsters 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
