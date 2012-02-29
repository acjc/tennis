package tennis.charts.retires.average;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.lpm.LpmChart;

public class ClijstersHantuchova
{
	public static void main(final String[] args) throws IOException
	{
		final LpmChart chart = new LpmChart("Clijsters vs Hantuchova - Brisbane International 2011 Semi Final",
															"doc\\clijsters_hantuchova\\Kim Clijsters.csv", "doc\\clijsters_hantuchova\\Clijsters 2 - 0.csv",
																				   					     	"doc\\clijsters_hantuchova\\Clijsters 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
