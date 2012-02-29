package tennis.distributions;

import java.io.IOException;

import tennis.graphs.XYLineChart;

public abstract class BoundedParetoChart extends XYLineChart
{
	protected final double lowerBound = 0.01;
	protected final double upperBound = 100.0;
	protected final double alpha = 1.0;
	protected final double decay = 0.9;

	public BoundedParetoChart(final String title, final String xLabel, final String yLabel) throws IOException
	{
		super(title, xLabel, yLabel);
		buildChart();
	}
}