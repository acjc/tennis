package tennis.charts.treatment.average;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.FiveSetLpmChart;

public class HewittStebe
{
	public static void main(final String[] args) throws IOException
	{
	    final FiveSetLpmChart chart = new FiveSetLpmChart("Hewitt vs Stebe (Australian Open 2012 First Round)",
	    												  "doc\\stebe_hewitt\\Lleyton Hewitt.csv", "doc\\stebe_hewitt\\Hewitt 3 - 0.csv",
	    												  										   "doc\\stebe_hewitt\\Hewitt 3 - 1.csv",
	    																						   "doc\\stebe_hewitt\\Hewitt 3 - 2.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
