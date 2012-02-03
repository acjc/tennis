package tennis.charts.retires;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.ThreeSetLpmChart;

public class LjubicicNadal
{
	public static void main(final String[] args) throws IOException
	{
		final ThreeSetLpmChart chart = new ThreeSetLpmChart("Ljubicic vs Nadal - Shanghai Masters 2009 Quarter Final",
															"doc\\ljubicic_nadal\\Ivan Ljubicic.csv", "doc\\ljubicic_nadal\\Ljubicic 2 - 0.csv",
																				   				      "doc\\ljubicic_nadal\\Ljubicic 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
