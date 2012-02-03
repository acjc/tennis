package tennis.charts.retires;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.charts.lpm.ThreeSetLpmChart;

public class TipsarevicSeppi
{
	public static void main(final String[] args) throws IOException
	{
		final ThreeSetLpmChart chart = new ThreeSetLpmChart("Tipsarevic vs Seppi - Eastbourne International 2011 Final",
															"doc\\tipsarevic_seppi\\Janko Tipsarevic.csv", "doc\\tipsarevic_seppi\\Tipsarevic 2 - 0.csv",
																				   					       "doc\\tipsarevic_seppi\\Tipsarevic 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
