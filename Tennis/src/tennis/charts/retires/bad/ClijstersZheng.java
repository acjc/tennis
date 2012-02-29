package tennis.charts.retires.bad;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.lpm.LpmChart;

public class ClijstersZheng
{
	public static void main(final String[] args) throws IOException
	{
		final LpmChart chart = new LpmChart("Clijsters vs Zheng - Rogers Cup 2011 Second Round",
															"doc\\clijsters_zheng\\Kim Clijsters.csv", "doc\\clijsters_zheng\\Clijsters 2 - 0.csv",
																				   					   "doc\\clijsters_zheng\\Clijsters 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
