package tennis.graphs.matches;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.read.ReadMatch;

public class RoddickHewittCaseStudy
{
	public static void main(final String[] args) throws IOException
	{
		final ReadMatch chart = new ReadMatch("Roddick vs Hewitt - Australian Open 2012 Second Round", 0.597, 168, "doc\\roddickhewitt.csv",
				  						     new PlayerOdds("Andy Roddick", "examples\\retirements", "Roddick vs Hewitt - Australian Open 2012 Second Round"),
				  						     new PlayerOdds("Lleyton Hewitt", "examples\\retirements", "Roddick vs Hewitt - Australian Open 2012 Second Round"));
		chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
