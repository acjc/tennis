package tennis.graphs.matches;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.read.ReadMatch;

public class NadalDelPotroCaseStudy
{
	public static void main(final String[] args) throws IOException
	{
		final ReadMatch chart = new ReadMatch("Del Potro vs Nadal - Wimbledon 2011 Fourth Round", 269, "doc\\nadaldelpotro.csv",
											  new PlayerOdds("Rafael Nadal", "examples\\treatment", "Del Potro vs Nadal - Wimbledon 2011 Fourth Round"),
											  new PlayerOdds("Juan-Martin Del-Potro", "examples\\treatment", "Del Potro vs Nadal - Wimbledon 2011 Fourth Round"));
		chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
