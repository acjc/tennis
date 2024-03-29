package tennis.distributions.pareto;

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

public class BoundedParetoCdf extends XYLineChart
{
	public BoundedParetoCdf() throws IOException
	{
		super("Shifted Bounded Pareto CDF", "x", "F(x)");
	}

	@Override
	public void buildChart() throws IOException
	{
		final JFreeChart chart = createChart();
		final ChartPanel chartPanel = new ChartPanel(chart);
		((XYPlot) chart.getPlot()).getRangeAxis().setRange(0, 1.2);
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	    renderer.setBaseShapesVisible(false);
	    ((XYPlot) chart.getPlot()).setRenderer(renderer);
	    chartPanel.setPreferredSize(new Dimension(1000, 570));
	    setContentPane(chartPanel);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\" + title + ".png"), chart, 1000, 570);
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Shifted Bounded Pareto CDF");
		final BoundedParetoDistribution pareto = new BoundedParetoDistribution(2000, 0.85);
	    for(double x = 1.0; x <= 1.1; x += 0.0001)
	    {
			series.add(x - 1, pareto.F(x));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final BoundedParetoCdf chart = new BoundedParetoCdf();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
