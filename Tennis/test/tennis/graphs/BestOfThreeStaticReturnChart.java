package tennis.graphs;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.OMalley;

public class BestOfThreeStaticReturnChart extends XYLineChart
{
	public BestOfThreeStaticReturnChart() throws IOException
	{
	    super("Probability of winning a best-of-3 set match with return pwp 0.5", "p", "bestOfThree(p, 0.5)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("BestOfThree");
	    for(double i = 0; i < 1.0; i += 0.01)
	    {
			series.add(i, OMalley.bestOfThree(i, 0.5));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final BestOfThreeStaticReturnChart chart = new BestOfThreeStaticReturnChart();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
