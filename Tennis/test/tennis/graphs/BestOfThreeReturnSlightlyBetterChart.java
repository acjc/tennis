package tennis.graphs;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.OMalley;

public class BestOfThreeReturnSlightlyBetterChart extends XYLineChart
{
	public BestOfThreeReturnSlightlyBetterChart() throws IOException
	{
	    super("Match-winning probabilities for a three-set match with varying point-winning probabilities", "PA", "BestOfThree(PA, PA - 0.02)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("BestOfThree(PA, PA - 0.02)");
	    for(double i = 0.02; i < 1.01; i += 0.01)
	    {
			series.add(i, OMalley.bestOfThree(i, i - 0.02));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final BestOfThreeReturnSlightlyBetterChart chart = new BestOfThreeReturnSlightlyBetterChart();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
