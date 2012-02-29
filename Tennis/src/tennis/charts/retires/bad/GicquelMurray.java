package tennis.charts.retires.bad;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.lpm.LpmChart;

public class GicquelMurray
{
	public static void main(final String[] args) throws IOException
	{
		final LpmChart chart = new LpmChart("Gicquel vs Murray - ABM Amro World Tennis Championships 2009 Quarter Final",
															"doc\\gicquel_murray\\Marc Gicquel.csv", "doc\\gicquel_murray\\Gicquel 2 - 0.csv",
																				   					 "doc\\gicquel_murray\\Gicquel 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
