package tennis.graphs.examples;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.odds.DefaultOddsChart;
import tennis.graphs.odds.OddsChart;

public class NadalFerrer
{
	public static void main(final String[] args) throws IOException
	{
		final OddsChart chart = new DefaultOddsChart(new PlayerOdds("Rafael Nadal", "examples\\treatment", "Nadal vs Ferrer - Australian Open 2011 Quarter Final"),
												     new PlayerOdds("David Ferrer", "examples\\treatment", "Nadal vs Ferrer - Australian Open 2011 Quarter Final"));
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
