package tennis.charts.treatment.bad;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.DefaultFiveSetLpmChart;

public class PotroNadal
{
	public static void main(final String[] args) throws IOException
	{
		final DefaultFiveSetLpmChart chart = new DefaultFiveSetLpmChart("Del Potro vs Nadal - Wimbledon 2011 Fourth Round",
													      "doc\\potro_nadal\\Juan Martin Del Potro.csv", "doc\\potro_nadal\\Del Potro 3 - 0.csv",
															  				   					     	 "doc\\potro_nadal\\Del Potro 3 - 1.csv",
																			   					     	 "doc\\potro_nadal\\Del Potro 3 - 2.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
