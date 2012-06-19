package tennis.graphs.matches;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.read.ReadMatch;
import tennis.read.RiskChart;

public class RoddickHewittCaseStudy
{
	public static void main(final String[] args) throws IOException
	{
		final ReadMatch marketChart = new ReadMatch(168, 3, "doc\\roddickhewitt.csv", "roddickhewittmodel",
				  						            new PlayerOdds("Andy Roddick", "examples\\retirements", "Roddick vs Hewitt - Australian Open 2012 Second Round"),
				  						            new PlayerOdds("Lleyton Hewitt", "examples\\retirements", "Roddick vs Hewitt - Australian Open 2012 Second Round"),
				  						            true);
		marketChart.buildChart();
	    marketChart.pack();
	    RefineryUtilities.centerFrameOnScreen(marketChart);
	    marketChart.setVisible(true);

	    final RiskChart riskChart = new RiskChart("Andy Roddick", "Roddick", "Hewitt", "roddickhewittrisk", marketChart.pointLevelRisks, marketChart.risks);
	    riskChart.buildChart();
	    riskChart.pack();
	    RefineryUtilities.centerFrameOnScreen(riskChart);
	    riskChart.setVisible(true);
	}
}
