package tennis.charts.retires;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.DefaultFiveSetLpmChart;

public class RoddickHewitt
{
	public static void main(final String[] args) throws IOException
	{
		final DefaultFiveSetLpmChart chart = new DefaultFiveSetLpmChart("Roddick vs Hewitt - Australian Open 2012 Second Round",
														  "doc\\roddick_hewitt\\Andy Roddick.csv", "doc\\roddick_hewitt\\Roddick 3 - 0.csv",
																				   				   "doc\\roddick_hewitt\\Roddick 3 - 1.csv",
																				   				   "doc\\roddick_hewitt\\Roddick 3 - 2.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
