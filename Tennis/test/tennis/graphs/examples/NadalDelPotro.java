package tennis.graphs.examples;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.lpm.DefaultOddsChart;
import tennis.graphs.lpm.OddsChart;

public class NadalDelPotro
{
	public static void main(final String[] args) throws IOException
	{
		final OddsChart chart = new DefaultOddsChart(new PlayerOdds("Rafael Nadal", "treatment\\five", "Del Potro vs Nadal - Wimbledon 2011 Fourth Round"),
													new PlayerOdds("Juan-Martin Del-Potro", "treatment\\five", "Del Potro vs Nadal - Wimbledon 2011 Fourth Round"));
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
