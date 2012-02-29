package tennis.charts.retires.average;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.lpm.LpmChart;

public class FishMonaco
{
	public static void main(final String[] args) throws IOException
	{
		final LpmChart chart = new LpmChart("Fish vs Monaco - Paris Masters 2011 Third Round",
															"doc\\fish_monaco\\Mardy Fish.csv", "doc\\fish_monaco\\Fish 2 - 0.csv",
																				   				"doc\\fish_monaco\\Fish 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
