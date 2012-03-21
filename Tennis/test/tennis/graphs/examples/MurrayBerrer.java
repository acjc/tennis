package tennis.graphs.examples;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.lpm.DefaultOddsChart;
import tennis.graphs.lpm.OddsChart;

public class MurrayBerrer
{
	public static void main(final String[] args) throws IOException
	{
		final OddsChart chart1 = new DefaultOddsChart(new PlayerOdds("Andy Murray", "treatment\\five", "Murray vs Berrer - French Open 2011 Third Round"),
													new PlayerOdds("Michael Berrer", "treatment\\five", "Murray vs Berrer - French Open 2011 Third Round"));
		chart1.pack();
		RefineryUtilities.centerFrameOnScreen(chart1);
		chart1.setVisible(true);

//	    final LpmChart chart = new DefaultLpmChart(new PlayerOdds("Michael Berrer", "treatment\\five", "Murray vs Berrer - French Open 2011 Third Round"),
//	    										   new PlayerOdds("Andy Murray", "treatment\\five", "Murray vs Berrer - French Open 2011 Third Round"));
//	    chart.pack();
//	    RefineryUtilities.centerFrameOnScreen(chart);
//	    chart.setVisible(true);
	}
}
