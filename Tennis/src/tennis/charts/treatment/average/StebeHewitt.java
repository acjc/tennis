package tennis.charts.treatment.average;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.lpm.DefaultFiveSetLpmChart;

public class StebeHewitt
{
	public static void main(final String[] args) throws IOException
	{
	    final DefaultFiveSetLpmChart chart = new DefaultFiveSetLpmChart("Stebe vs Hewitt (Australian Open 2012 First Round)",
	    												  "doc\\stebe_hewitt\\Cedrik Marcel Stebe.csv", "doc\\stebe_hewitt\\Stebe 3 - 0.csv",
	    												  												"doc\\stebe_hewitt\\Stebe 3 - 1.csv",
	    																								"doc\\stebe_hewitt\\Stebe 3 - 2.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
