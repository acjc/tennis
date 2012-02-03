package tennis.charts.treatment.average;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.lpm.DefaultFiveSetLpmChart;

public class MurrayGasquet
{
	public static void main(final String[] args) throws IOException
	{
	    final DefaultFiveSetLpmChart chart = new DefaultFiveSetLpmChart("Murray vs Gasquet - French Open 2010 First Round",
	    												  "doc\\murray_gasquet\\Andy Murray.csv", "doc\\murray_gasquet\\Murray 3 - 0.csv",
				  										 										  "doc\\murray_gasquet\\Murray 3 - 1.csv",
										   			     										  "doc\\murray_gasquet\\Murray 3 - 2.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
