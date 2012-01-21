package tennis.charts.treatment;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.FiveSetLpmChart;

public class MurrayBerrer
{
	public static void main(final String[] args) throws IOException
	{
	    final FiveSetLpmChart chart = new FiveSetLpmChart("Murray vs Berrer (French Open 2011 Third Round)",
	    												  "doc\\murray_berrer\\Andy Murray.csv", "doc\\murray_berrer\\Murray 3 - 0.csv",
				  										 										 "doc\\murray_berrer\\Murray 3 - 1.csv",
										   			     										 "doc\\murray_berrer\\Murray 3 - 2.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
