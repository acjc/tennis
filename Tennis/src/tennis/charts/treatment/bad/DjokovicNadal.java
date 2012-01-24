package tennis.charts.treatment.bad;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.FiveSetLpmChart;

public class DjokovicNadal
{
	public static void main(final String[] args) throws IOException
	{
	    final FiveSetLpmChart chart = new FiveSetLpmChart("Djokovic vs Nadal (US Open 2011 Final)",
	    												  "doc\\djokovic_nadal\\Novak Djokovic.csv", "doc\\djokovic_nadal\\Djokovic 3 - 0.csv",
				  										 										     "doc\\djokovic_nadal\\Djokovic 3 - 1.csv",
										   			     										     "doc\\djokovic_nadal\\Djokovic 3 - 2.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
