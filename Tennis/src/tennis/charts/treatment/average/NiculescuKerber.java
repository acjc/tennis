package tennis.charts.treatment.average;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.helper.PlayerOdds;
import tennis.charts.lpm.CrossMatchLpmChart;
import tennis.charts.lpm.DefaultLpmChart;
import tennis.charts.lpm.LpmChart;

public class NiculescuKerber
{
	public static void main(final String[] args) throws IOException
	{
		final LpmChart chart1 = new DefaultLpmChart(new PlayerOdds("Monica Niculescu", "treatment\\three", "Niculescu vs Kerber - US Open 2011 Fourth Round"),
													new PlayerOdds("Angelique Kerber", "treatment\\three", "Niculescu vs Kerber - US Open 2011 Fourth Round"));
		chart1.pack();
		RefineryUtilities.centerFrameOnScreen(chart1);
		chart1.setVisible(true);

	    final LpmChart chart = new CrossMatchLpmChart(new PlayerOdds("Angelique Kerber", "treatment\\three", "Niculescu vs Kerber - US Open 2011 Fourth Round"),
	    											  new PlayerOdds("Monica Niculescu", "treatment\\three", "Niculescu vs Kerber - US Open 2011 Fourth Round"));
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
