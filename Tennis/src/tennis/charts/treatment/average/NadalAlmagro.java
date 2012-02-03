package tennis.charts.treatment.average;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.DefaultFiveSetLpmChart;

public class NadalAlmagro
{
	public static void main(final String[] args) throws IOException
	{
		final DefaultFiveSetLpmChart chart = new DefaultFiveSetLpmChart("Nadal vs Almagro - US Open 2009 Third Round",
													      "doc\\nadal_almagro\\Rafael Nadal.csv", "doc\\nadal_almagro\\Nadal 3 - 0.csv",
															  				   					  "doc\\nadal_almagro\\Nadal 3 - 1.csv",
																			   					  "doc\\nadal_almagro\\Nadal 3 - 2.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
