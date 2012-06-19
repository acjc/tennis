package tennis.graphs;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.OMalley;

public class PWPDifferenceChart extends XYLineChart
{
	public PWPDifferenceChart() throws IOException
	{
	    super("Match-winning probability as a linear function of PA - PB", "d", "Match-winning Probability");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("BestOfFive(0.6, 0.6 - d)");
	    for(double d = -0.1; d <= 0.1; d += 0.001)
	    {
			series.add(d, OMalley.bestOfFive(0.6, 0.6 - d));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final PWPDifferenceChart chart = new PWPDifferenceChart();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
