package tennis.charts.retires;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.ThreeSetLpmChart;

public class AzarenkaSharapova
{
	public static void main(final String[] args) throws IOException
	{
		final ThreeSetLpmChart chart = new ThreeSetLpmChart("Azarenka vs Sharapova (Rome Masters 2011 Quarter Final)",
															"doc\\azarenka_sharapova\\Victoria Azarenka.csv", "doc\\azarenka_sharapova\\Azarenka 2 - 0.csv",
																				   					          "doc\\azarenka_sharapova\\Azarenka 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
