package tennis.distributions;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.charts.XYLineChart;

public class BoundedParetoCdfChart extends BoundedParetoChart
{

	public BoundedParetoCdfChart() throws IOException
	{
		super("Bounded Pareto CDF", "x", "f(x)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Bounded Pareto CDF");
		final BoundedParetoDistribution pareto = new BoundedParetoDistribution(alpha, lowerBound, upperBound);
	    for(double x = lowerBound; x <= upperBound; x += 0.01)
	    {
			series.add(x, pareto.F(x));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final XYLineChart chart = new BoundedParetoCdfChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
