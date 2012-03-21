package tennis.graphs.examples;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.lpm.DefaultOddsChart;
import tennis.graphs.lpm.OddsChart;

public class CilicMurray
{
	public static void main(final String[] args) throws IOException
	{
		final OddsChart chart1 = new DefaultOddsChart(new PlayerOdds("Marin Cilic", "treatment\\five", "Cilic vs Murray - French Open 2009 Fourth Round (SYNC)"),
													new PlayerOdds("Andy Murray", "treatment\\five", "Cilic vs Murray - French Open 2009 Fourth Round (SYNC)"));
		chart1.pack();
		RefineryUtilities.centerFrameOnScreen(chart1);
		chart1.setVisible(true);

//	    final LpmChart chart = new CrossMatchLpmChart(new PlayerOdds("Andy Murray", "treatment\\five", "Cilic vs Murray - French Open 2009 Fourth Round"),
//	    											  new PlayerOdds("Marin Cilic", "treatment\\five", "Cilic vs Murray - French Open 2009 Fourth Round"));
//	    chart.pack();
//	    RefineryUtilities.centerFrameOnScreen(chart);
//	    chart.setVisible(true);
	}
}
