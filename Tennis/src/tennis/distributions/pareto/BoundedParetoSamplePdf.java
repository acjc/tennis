package tennis.distributions.pareto;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.graphs.XYLineChart;

public class BoundedParetoSamplePdf extends XYLineChart
{
	public BoundedParetoSamplePdf() throws IOException
	{
		super("Bounded Pareto PDF", "x", "f(x)");
	}

	@Override
	public void buildChart() throws IOException
	{
		final JFreeChart chart = createChart();
		final ChartPanel chartPanel = new ChartPanel(chart);
		((XYPlot) chart.getPlot()).getRangeAxis().setRange(0, 1000);
	    chartPanel.setPreferredSize(new Dimension(1000, 570));
	    setContentPane(chartPanel);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\" + title + ".png"), chart, 1000, 570);
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Bounded Pareto PDF");
		final BoundedParetoDistribution pareto = new BoundedParetoDistribution(1.0, 0.85);
	    for(double x = 0; x < 1000; x++)
	    {
			final double sample = pareto.sample();
			System.out.println(sample);
			series.add(sample, pareto.f(sample));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final BoundedParetoSamplePdf chart = new BoundedParetoSamplePdf();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
