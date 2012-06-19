package tennis.graphs.matches;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.read.ReadMatch;
import tennis.read.RiskChart;

public class MurrayNieminenCaseStudy
{
	public static void main(final String[] args) throws IOException
	{
		final ReadMatch marketChart = new ReadMatch(213, 3, "doc\\murraynieminen.csv", "murraynieminenmodel",
											        new PlayerOdds("Andy Murray", "examples\\treatment", "Murray vs Nieminen - French Open 2012 Second Round"),
											        new PlayerOdds("Jarkko Nieminen", "examples\\treatment", "Murray vs Nieminen - French Open 2012 Second Round"),
											        false);
		marketChart.buildChart();
	    marketChart.pack();
	    RefineryUtilities.centerFrameOnScreen(marketChart);
	    marketChart.setVisible(true);

	    final RiskChart riskChart = new RiskChart("Andy Murray", "Murray", "Niemenen", "murraynieminenrisk", marketChart.pointLevelRisks, marketChart.risks);
	    riskChart.buildChart();
	    riskChart.pack();
	    RefineryUtilities.centerFrameOnScreen(riskChart);
	    riskChart.setVisible(true);
	}
}
