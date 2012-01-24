package tennis.charts.treatment.bad;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.FiveSetLpmChart;

public class HewittRoddick
{
	public static void main(final String[] args) throws IOException
	{
	    final FiveSetLpmChart chart = new FiveSetLpmChart("Hewitt vs Roddick (Wimbledon 2009 Quarter Final)",
	    												  "doc\\hewitt_roddick\\Lleyton Hewitt.csv", "doc\\hewitt_roddick\\Hewitt 3 - 0.csv",
	    												  										     "doc\\hewitt_roddick\\Hewitt 3 - 1.csv",
	    																						     "doc\\hewitt_roddick\\Hewitt 3 - 2.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
