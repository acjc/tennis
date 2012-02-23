package tennis.distributions;

import java.awt.Dimension;
import java.io.IOException;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.charts.XYLineChart;

public class RetirementRiskChart extends XYLineChart
{
	public RetirementRiskChart() throws IOException
	{
		super("Retirement Risk Model", "Time", "Retirement Risk");
	}

	@Override
	protected void drawChart() throws IOException
	{
		final JFreeChart chart = createXYLineChart(createDataset());
		((XYPlot) chart.getPlot()).getRangeAxis().setRange(0, 100);
		final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new Dimension(500, 270));
	    setContentPane(chartPanel);
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Retirement Risk Model");
		final BoundedParetoDistribution pareto = new BoundedParetoDistribution(0.9, 0.01, 100);
		double r = 0.0;
		final double d = 0.9;
	    for(double t = 0; t <= 200; t++)
	    {
			series.add(t, r);
			r *= d;
			r += pareto.sample();
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final XYLineChart chart = new RetirementRiskChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
