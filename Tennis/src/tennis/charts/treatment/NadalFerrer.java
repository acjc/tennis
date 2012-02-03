package tennis.charts.treatment;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.DefaultFiveSetLpmChart;

public class NadalFerrer
{
	public static void main(final String[] args) throws IOException
	{
		final DefaultFiveSetLpmChart chart = new DefaultFiveSetLpmChart("Nadal vs Ferrer - Australian Open 2011 Quarter Final",
														  "doc\\nadal_ferrer\\Rafael Nadal.csv", "doc\\nadal_ferrer\\Nadal 3 - 0.csv",
																				   				 "doc\\nadal_ferrer\\Nadal 3 - 1.csv",
																				   				 "doc\\nadal_ferrer\\Nadal 3 - 2.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
