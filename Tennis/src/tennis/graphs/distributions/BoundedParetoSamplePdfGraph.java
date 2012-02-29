package tennis.graphs.distributions;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.graphs.XYLineChart;

public class BoundedParetoSamplePdfGraph extends BoundedParetoGraph
{
	public BoundedParetoSamplePdfGraph() throws IOException
	{
		super("Bounded Pareto PDF", "x", "f(x)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Bounded Pareto PDF");
		final BoundedParetoDistribution pareto = new BoundedParetoDistribution(alpha, lowerBound, upperBound, decay);
	    for(double x = 0; x < 1000; x++)
	    {
			final double sample = pareto.sample();
			series.add(sample, pareto.f(sample));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final XYLineChart chart = new BoundedParetoSamplePdfGraph();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
