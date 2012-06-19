package tennis.graphs.matches;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.read.ReadMatch;
import tennis.read.RiskChart;

public class AzarenkaSharapovaCaseStudy
{
	public static void main(final String[] args) throws IOException
	{
		final ReadMatch marketChart = new ReadMatch(93, 2, "doc\\azarenkasharapova.csv", "azarenkasharapovamodel",
											        new PlayerOdds("Victoria Azarenka", "examples\\retirements", "Azarenka vs Sharapova - Rome Masters 2011 Quarter Final"),
											        new PlayerOdds("Maria Sharapova", "examples\\retirements", "Azarenka vs Sharapova - Rome Masters 2011 Quarter Final"),
											        true);
		marketChart.buildChart();
	    marketChart.pack();
	    RefineryUtilities.centerFrameOnScreen(marketChart);
	    marketChart.setVisible(true);

	    final RiskChart riskChart = new RiskChart("Victoria Azarenka", "Azarenka", "Sharapova", "azarenkasharapovarisk", marketChart.pointLevelRisks, marketChart.risks);
	    riskChart.buildChart();
	    riskChart.pack();
	    RefineryUtilities.centerFrameOnScreen(riskChart);
	    riskChart.setVisible(true);
	}
}
