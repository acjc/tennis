package tennis.charts.treatment.average;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.lpm.CrossMatchLpmChart;
import tennis.graphs.lpm.DefaultLpmChart;
import tennis.graphs.lpm.LpmChart;

public class CilicMurray
{
	public static void main(final String[] args) throws IOException
	{
		final LpmChart chart1 = new DefaultLpmChart(new PlayerOdds("Marin Cilic", "treatment\\five", "Cilic vs Murray - French Open 2009 Fourth Round"),
													new PlayerOdds("Andy Murray", "treatment\\five", "Cilic vs Murray - French Open 2009 Fourth Round"));
		chart1.pack();
		RefineryUtilities.centerFrameOnScreen(chart1);
		chart1.setVisible(true);

	    final LpmChart chart = new CrossMatchLpmChart(new PlayerOdds("Andy Murray", "treatment\\five", "Cilic vs Murray - French Open 2009 Fourth Round"),
	    											  new PlayerOdds("Marin Cilic", "treatment\\five", "Cilic vs Murray - French Open 2009 Fourth Round"));
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
