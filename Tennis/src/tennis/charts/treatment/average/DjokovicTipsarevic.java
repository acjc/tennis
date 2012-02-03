package tennis.charts.treatment.average;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.DefaultFiveSetLpmChart;

public class DjokovicTipsarevic
{
	public static void main(final String[] args) throws IOException
	{
	    final DefaultFiveSetLpmChart chart = new DefaultFiveSetLpmChart("Djokovic vs Tipsarevic (US Open 2011 Quarter Final)",
	    												  "doc\\tipsarevic_djokovic\\Novak Djokovic.csv", "doc\\tipsarevic_djokovic\\Djokovic 3 - 0.csv",
				  										 										          "doc\\tipsarevic_djokovic\\Djokovic 3 - 1.csv",
	    																								  "doc\\tipsarevic_djokovic\\Djokovic 3 - 2.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
