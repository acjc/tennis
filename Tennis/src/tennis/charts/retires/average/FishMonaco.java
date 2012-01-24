package tennis.charts.retires.average;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.ThreeSetLpmChart;

public class FishMonaco
{
	public static void main(final String[] args) throws IOException
	{
		final ThreeSetLpmChart chart = new ThreeSetLpmChart("Fish vs Monaco (Paris Masters 2011 Third Round)",
															"doc\\fish_monaco\\Mardy Fish.csv", "doc\\fish_monaco\\Fish 2 - 0.csv",
																				   				"doc\\fish_monaco\\Fish 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
