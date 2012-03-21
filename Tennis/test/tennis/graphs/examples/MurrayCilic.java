package tennis.graphs.examples;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.lpm.DefaultOddsChart;
import tennis.graphs.lpm.OddsChart;

public class MurrayCilic
{
	public static void main(final String[] args) throws IOException
	{
		final OddsChart chart = new DefaultOddsChart(new PlayerOdds("Andy Murray", "treatment\\five", "Cilic vs Murray - French Open 2009 Fourth Round (SYNC)"),
												   new PlayerOdds("Marin Cilic", "treatment\\five", "Cilic vs Murray - French Open 2009 Fourth Round (SYNC)"));
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
