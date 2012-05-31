package tennis.graphs.distributions.exp;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.graphs.XYLineChart;
import tennis.graphs.distributions.pareto.BoundedParetoGraph;

public class TruncatedExponentialCdf extends BoundedParetoGraph
{

	public TruncatedExponentialCdf() throws IOException
	{
		super("Truncated Exponential CDF", "x", "f(x)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Truncated Exponential CDF");
		final TruncatedExponentialDistribution exp = new TruncatedExponentialDistribution(50);
	    for(double x = 0; x <= 1; x += 0.01)
	    {
			series.add(x, exp.F(x));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final XYLineChart chart = new TruncatedExponentialCdf();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
