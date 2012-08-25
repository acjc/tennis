package tennis.graphs.matches;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.read.ReadMatch;
import tennis.read.RiskChart;

public class MurrayBerrerCaseStudy
{
	public static void main(final String[] args) throws IOException
	{
		final ReadMatch marketChart = new ReadMatch(148, 3, "doc\\murrayberrer.csv", "murrayberrermodel",
											        new PlayerOdds("Andy Murray", "examples\\treatment", "Murray vs Berrer - French Open 2011 Third Round"),
											        new PlayerOdds("Michael Berrer", "examples\\treatment", "Murray vs Berrer - French Open 2011 Third Round"),
											        false, 0.645);
		marketChart.buildChart();
	    marketChart.pack();
	    RefineryUtilities.centerFrameOnScreen(marketChart);
	    marketChart.setVisible(true);

	    final RiskChart riskChart = new RiskChart("Andy Murray", "Murray", "Berrer", "murrayberrerrisk", marketChart.pointLevelRisks, marketChart.risks);
	    riskChart.buildChart();
	    riskChart.pack();
	    RefineryUtilities.centerFrameOnScreen(riskChart);
	    riskChart.setVisible(true);
	}
}
