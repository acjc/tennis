package tennis.charts.treatment.bad;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.lpm.DefaultFiveSetLpmChart;

public class HewittRoddick
{
	public static void main(final String[] args) throws IOException
	{
	    final DefaultFiveSetLpmChart chart = new DefaultFiveSetLpmChart("Hewitt vs Roddick - Wimbledon 2009 Quarter Final",
	    												  "doc\\hewitt_roddick\\Lleyton Hewitt.csv", "doc\\hewitt_roddick\\Hewitt 3 - 0.csv",
	    												  										     "doc\\hewitt_roddick\\Hewitt 3 - 1.csv",
	    																						     "doc\\hewitt_roddick\\Hewitt 3 - 2.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
