package tennis.charts.treatment.average;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.lpm.LpmChart;

public class NadalDjokovic
{
	public static void main(final String[] args) throws IOException
	{
		final LpmChart chart = new LpmChart("Nadal vs Djokovic - Madrid Masters 2009 Semi Final",
															"doc\\nadal_djokovic\\Rafael Nadal.csv", "doc\\nadal_djokovic\\Nadal 2 - 0.csv",
																				   					 "doc\\nadal_djokovic\\Nadal 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
