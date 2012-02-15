package tennis.charts.distributions;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.charts.XYLineChart;
import tennis.simulator.BoundedParetoDistribution;

public class BoundedParetoSamplePdfChart extends XYLineChart
{
	public BoundedParetoSamplePdfChart() throws IOException
	{
		super("Bounded Pareto PDF", "x", "f(x)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Bounded Pareto PDF");
		final double lowerBound = 0.01;
		final double upperBound = 1.0;
		final BoundedParetoDistribution pareto = new BoundedParetoDistribution(0.3, lowerBound, upperBound);
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
	    final XYLineChart chart = new BoundedParetoSamplePdfChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
