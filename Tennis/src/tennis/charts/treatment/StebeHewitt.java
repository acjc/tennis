package tennis.charts.treatment;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.FiveSetLpmChart;

public class StebeHewitt
{
	public static void main(final String[] args) throws IOException
	{
	    final FiveSetLpmChart chart = new FiveSetLpmChart("Stebe vs Hewitt (Australian Open 2012 First Round)",
	    												  "doc\\stebe_hewitt\\Cedrik Marcel Stebe.csv", "doc\\stebe_hewitt\\Stebe 3 - 0.csv",
	    												  												"doc\\stebe_hewitt\\Stebe 3 - 1.csv",
	    																								"doc\\stebe_hewitt\\Stebe 3 - 2.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
