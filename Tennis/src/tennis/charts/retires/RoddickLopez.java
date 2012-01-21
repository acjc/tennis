package tennis.charts.retires;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.ThreeSetLpmChart;

public class RoddickLopez
{
	public static void main(final String[] args) throws IOException
	{
		final ThreeSetLpmChart chart = new ThreeSetLpmChart("Roddick vs Garcia Lopez (Shanghai Masters 2010 Second Round)",
															"doc\\roddick_lopez\\Andy Roddick.csv", "doc\\roddick_lopez\\Roddick 2 - 0.csv",
																				   					"doc\\roddick_lopez\\Roddick 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}