package tennis.graphs.matches;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.read.ReadMatch;

public class MurrayNieminenCaseStudy
{
	public static void main(final String[] args) throws IOException
	{
		final ReadMatch chart = new ReadMatch("Murray vs Nieminen - French Open 2012 Second Round", 0.556, 213, "doc\\murraynieminen.csv",
											  new PlayerOdds("Andy Murray", "examples\\treatment", "Murray vs Nieminen - French Open 2012 Second Round"),
											  new PlayerOdds("Jarkko Nieminen", "examples\\treatment", "Murray vs Nieminen - French Open 2012 Second Round"));
		chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
