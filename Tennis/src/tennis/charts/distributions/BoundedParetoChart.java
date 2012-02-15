package tennis.charts.distributions;

import java.io.IOException;

import tennis.charts.XYLineChart;
import tennis.simulator.BoundedParetoDistribution;

public abstract class BoundedParetoChart extends XYLineChart
{
	protected final double lowerBound = 0.01;
	protected final double upperBound = 1.0;
	protected final double alpha = 0.3;
	protected final BoundedParetoDistribution pareto = new BoundedParetoDistribution(alpha, lowerBound, upperBound);

	public BoundedParetoChart(final String title, final String xLabel, final String yLabel) throws IOException
	{
		super(title, xLabel, yLabel);
	}

}