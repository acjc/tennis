package tennis.graphs.matches;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.read.ReadMatch;
import tennis.read.RiskChart;

public class NadalFerrerCaseStudy
{
	public static void main(final String[] args) throws IOException
	{
		final ReadMatch marketChart = new ReadMatch(168, 3, "doc\\nadalferrer.csv", "nadalferrermodel",
											        new PlayerOdds("Rafael Nadal", "examples\\treatment", "Nadal vs Ferrer - Australian Open 2011 Quarter Final"),
											        new PlayerOdds("David Ferrer", "examples\\treatment", "Nadal vs Ferrer - Australian Open 2011 Quarter Final"),
													false);
		marketChart.buildChart();
	    marketChart.pack();
	    RefineryUtilities.centerFrameOnScreen(marketChart);
	    marketChart.setVisible(true);

	    final RiskChart riskChart = new RiskChart("Rafael Nadal", "Nadal", "Ferrer", "nadalferrerrisk", marketChart.pointLevelRisks, marketChart.risks);
	    riskChart.buildChart();
	    riskChart.pack();
	    RefineryUtilities.centerFrameOnScreen(riskChart);
	    riskChart.setVisible(true);
	}
}
