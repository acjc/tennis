package tennis.charts.treatment.average;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.helper.PlayerOdds;
import tennis.charts.lpm.CrossMatchFiveSetLpmChart;
import tennis.charts.lpm.DefaultFiveSetLpmChart;
import tennis.charts.lpm.FiveSetLpmChart;

public class CilicMurray
{
	public static void main(final String[] args) throws IOException
	{
	    final FiveSetLpmChart chart = new CrossMatchFiveSetLpmChart("Cilic",
	    														    new PlayerOdds("Andy Murray", "treatment\\five", "Cilic vs Murray - French Open 2009 Fourth Round"));
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);

	    final FiveSetLpmChart chart1 = new DefaultFiveSetLpmChart("CilicOld",
				  												  new PlayerOdds("Marin Cilic", "treatment\\five", "Cilic vs Murray - French Open 2009 Fourth Round"));
	    chart1.pack();
		RefineryUtilities.centerFrameOnScreen(chart1);
		chart1.setVisible(true);
	}
}
