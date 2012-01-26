package tennis.charts.treatment;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.ThreeSetLpmChart;

public class ClijstersLi
{
	public static void main(final String[] args) throws IOException
	{
		final ThreeSetLpmChart chart = new ThreeSetLpmChart("Clijsters vs Li (Australian Open 2012 Fourth Round)",
															"doc\\clijsters_li\\Kim Clijsters.csv", "doc\\clijsters_li\\Clijsters 2 - 0.csv",
																				   				    "doc\\clijsters_li\\Clijsters 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
