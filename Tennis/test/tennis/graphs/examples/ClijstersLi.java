package tennis.graphs.examples;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.lpm.DefaultOddsChart;
import tennis.graphs.lpm.OddsChart;

public class ClijstersLi
{
	public static void main(final String[] args) throws IOException
	{
		final OddsChart chart = new DefaultOddsChart(new PlayerOdds("Kim Clijsters", "examples\\treatment", "Clijsters vs Li - Australian Open 2012 Fourth Round"),
												     new PlayerOdds("Na Li", "examples\\treatment", "Clijsters vs Li - Australian Open 2012 Fourth Round"));
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
