package tennis.charts.treatment;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.DefaultFiveSetLpmChart;

public class NadalPotro
{
	public static void main(final String[] args) throws IOException
	{
		final DefaultFiveSetLpmChart chart = new DefaultFiveSetLpmChart("Nadal vs Del Potro (Wimbledon 2011 Fourth Round)",
													      "doc\\potro_nadal\\Rafael Nadal.csv", "doc\\potro_nadal\\Nadal 3 - 0.csv",
															  				   					"doc\\potro_nadal\\Nadal 3 - 1.csv",
																			   					"doc\\potro_nadal\\Nadal 3 - 2.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}