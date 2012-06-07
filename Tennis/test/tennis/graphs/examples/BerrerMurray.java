package tennis.graphs.examples;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.odds.DefaultOddsChart;
import tennis.graphs.odds.OddsChart;

public class BerrerMurray
{
	public static void main(final String[] args) throws IOException
	{
		final OddsChart chart = new DefaultOddsChart(new PlayerOdds("Michael Berrer", "treatment\\five", "Murray vs Berrer - French Open 2011 Third Round"),
													 new PlayerOdds("Andy Murray", "treatment\\five", "Murray vs Berrer - French Open 2011 Third Round"));
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
