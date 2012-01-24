package tennis.charts.retires.bad;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.FiveSetLpmChart;

public class TipsarevicDjokovic
{
	public static void main(final String[] args) throws IOException
	{
		final FiveSetLpmChart chart = new FiveSetLpmChart("Tipsarevic vs Djokovic (US Open 2011 Quarter Final)",
														  "doc\\tipsarevic_djokovic\\Janko Tipsarevic.csv", "doc\\tipsarevic_djokovic\\Tipsarevic 3 - 0.csv",
																				   							"doc\\tipsarevic_djokovic\\Tipsarevic 3 - 1.csv",
																				   							"doc\\tipsarevic_djokovic\\Tipsarevic 3 - 2.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
