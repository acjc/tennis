package tennis.charts.treatment.bad;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.lpm.DefaultFiveSetLpmChart;

public class HewittStebe
{
	public static void main(final String[] args) throws IOException
	{
	    final DefaultFiveSetLpmChart chart = new DefaultFiveSetLpmChart("Hewitt vs Stebe - Australian Open 2012 First Round",
	    												  "doc\\stebe_hewitt\\Lleyton Hewitt.csv", "doc\\stebe_hewitt\\Hewitt 3 - 0.csv",
	    												  										   "doc\\stebe_hewitt\\Hewitt 3 - 1.csv",
	    																						   "doc\\stebe_hewitt\\Hewitt 3 - 2.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
