package tennis.graphs.matches;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.read.ReadMatch;
import tennis.read.RiskChart;

public class NadalDelPotroCaseStudy
{
	public static void main(final String[] args) throws IOException
	{
		final ReadMatch marketChart = new ReadMatch(269, 3, "doc\\nadaldelpotro.csv", "nadaldelpotromodel",
											        new PlayerOdds("Rafael Nadal", "examples\\treatment", "Del Potro vs Nadal - Wimbledon 2011 Fourth Round"),
											        new PlayerOdds("Juan-Martin Del-Potro", "examples\\treatment", "Del Potro vs Nadal - Wimbledon 2011 Fourth Round"),
											        false);
		marketChart.buildChart();
	    marketChart.pack();
	    RefineryUtilities.centerFrameOnScreen(marketChart);
	    marketChart.setVisible(true);

	    final RiskChart riskChart = new RiskChart("Rafael Nadal", "Nadal", "Del Potro", "nadaldelpotrorisk", marketChart.pointLevelRisks, marketChart.risks);
	    riskChart.buildChart();
	    riskChart.pack();
	    RefineryUtilities.centerFrameOnScreen(riskChart);
	    riskChart.setVisible(true);
	}
}
