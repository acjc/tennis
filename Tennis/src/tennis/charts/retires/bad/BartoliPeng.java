package tennis.charts.retires.bad;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.lpm.LpmChart;

public class BartoliPeng
{
	public static void main(final String[] args) throws IOException
	{
		final LpmChart chart = new LpmChart("Bartoli vs Peng - Family Circle Cup 2010 Second Round",
															"doc\\bartoli_peng\\Marion Bartoli.csv", "doc\\bartoli_peng\\Bartoli 2 - 0.csv",
																				   					 "doc\\bartoli_peng\\Bartoli 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
