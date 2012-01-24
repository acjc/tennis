package tennis.charts.treatment.bad;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.FiveSetLpmChart;

public class LopezMurray
{
	public static void main(final String[] args) throws IOException
	{
		final FiveSetLpmChart chart = new FiveSetLpmChart("Lopez vs Murray (Wimbledon 2011 Quarter Final)",
														  "doc\\murray_lopez\\Feliciano Lopez.csv", "doc\\murray_lopez\\Lopez 3 - 0.csv",
														 											"doc\\murray_lopez\\Lopez 3 - 1.csv",
										 			     											"doc\\murray_lopez\\Lopez 3 - 2.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
