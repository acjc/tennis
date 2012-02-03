package tennis.charts.treatment.average;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.DefaultFiveSetLpmChart;

public class GasquetMurray
{
	public static void main(final String[] args) throws IOException
	{
	    final DefaultFiveSetLpmChart chart = new DefaultFiveSetLpmChart("Gasquet vs Murray (French Open 2010 First Round)",
	    												  "doc\\murray_gasquet\\Richard Gasquet.csv", "doc\\murray_gasquet\\Gasquet 3 - 0.csv",
				  										 										      "doc\\murray_gasquet\\Gasquet 3 - 1.csv",
										   			     										      "doc\\murray_gasquet\\Gasquet 3 - 2.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
