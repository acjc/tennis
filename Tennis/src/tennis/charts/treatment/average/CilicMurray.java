package tennis.charts.treatment.average;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.DefaultFiveSetLpmChart;

public class CilicMurray
{
	public static void main(final String[] args) throws IOException
	{
	    final DefaultFiveSetLpmChart chart = new DefaultFiveSetLpmChart("Cilic vs Murray - French Open 2009 Fourth Round",
	    												  "doc\\cilic_murray\\Marin Cilic.csv", "doc\\cilic_murray\\Cilic 3 - 0.csv",
				  										 										"doc\\cilic_murray\\Cilic 3 - 1.csv",
										   			     										"doc\\cilic_murray\\Cilic 3 - 2.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
