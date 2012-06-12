package tennis.graphs.examples;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.odds.DefaultOddsChart;
import tennis.graphs.odds.OddsChart;

public class DjokovicNadal
{
	public static void main(final String[] args) throws IOException
	{
		final OddsChart chart = new DefaultOddsChart(new PlayerOdds("Novak Djokovic", "examples\\treatment", "Djokovic vs Nadal - US Open 2011 Final"),
													 new PlayerOdds("Rafael Nadal", "examples\\treatment", "Djokovic vs Nadal - US Open 2011 Final"));
		chart.buildChart();
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
