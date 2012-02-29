package tennis.charts.treatment;

import java.io.IOException;

import org.jfree.ui.RefineryUtilities;

import tennis.graphs.lpm.LpmChart;

public class JankovicDokic
{
	public static void main(final String[] args) throws IOException
	{
		final LpmChart chart = new LpmChart("Jankovic vs Dokic - US Open 2011 Second Round",
															"doc\\jankovic_dokic\\Jelena Jankovic.csv", "doc\\jankovic_dokic\\Jankovic 2 - 0.csv",
																				   					    "doc\\jankovic_dokic\\Jankovic 2 - 1.csv");
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}
