package tennis.distributions.exp;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.graphs.XYLineChart;

public class TruncatedExponentialCdf extends XYLineChart
{
	private final double lambda;

	public TruncatedExponentialCdf(final double lambda) throws IOException
	{
		super("Truncated Exponential CDF (lambda = " + lambda + ")", "x", "F(x)");
		this.lambda = lambda;
	}

	@Override
	public void buildChart() throws IOException
	{
		final JFreeChart chart = createChart();
		chart.setBackgroundPaint(new Color(255, 255, 255, 0));
		chart.setBackgroundImageAlpha(0.0f);
		chart.getLegend().setBackgroundPaint(new Color(255, 255, 255, 0));

		final XYPlot plot = (XYPlot) chart.getPlot();
		plot.getRangeAxis().setRange(0, 1.2);
		plot.setBackgroundPaint(new Color(255, 255, 255, 0));
	    plot.setBackgroundImageAlpha(0.0f);

	    final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	    renderer.setBaseShapesVisible(true);

	    plot.setRenderer(renderer);

	    final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new Dimension(1000, 570));

	    setContentPane(chartPanel);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\" + title + ".png"), chart, 1000, 570);
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Truncated Exponential CDF");
		final TruncatedExponentialDistribution exp = new TruncatedExponentialDistribution(lambda);
		final double tmp = 0.00015;
		System.out.println("tmp = " + tmp + ", F(tmp) = " + exp.F(tmp));
	    for(double x = 0; x <= 1.0; x += 0.0001)
	    {
			series.add(x, exp.F(x));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final TruncatedExponentialCdf chart = new TruncatedExponentialCdf(10);
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
