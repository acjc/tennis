package tennis.graphs;

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

import tennis.simulator.Simulator;

public class SimulatorRunsChart extends XYLineChart
{
	public SimulatorRunsChart(final String title) throws IOException
	{
	    super(title, "Runs", "Match-winning Probability");
	}

	@Override
	protected void buildChart() throws IOException
	{
		final JFreeChart chart = createXYLineChart(createDataset());
		((XYPlot) chart.getPlot()).getRangeAxis().setRange(0.4975, 0.5025);
		final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new Dimension(800, 400));
	    setContentPane(chartPanel);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\" + title + ".png"), chart, 1000, 570);
	}

	@Override
	protected XYDataset createDataset() throws IOException
	{
		final XYSeries simulatorSeries = new XYSeries("Simulator");
		final XYSeries solutionSeries = new XYSeries("Exact Solution");
		final XYSeries upperSeries = new XYSeries("Upper Bound");
		final XYSeries lowerSeries = new XYSeries("Lower Bound");
	    for(double i = 10000; i <= 750000; i += 10000)
	    {
			final double mwp = new Simulator().simulate(0.6, 0.6, i).proportionTargetWon();
			simulatorSeries.add(i, mwp);
			System.out.println("Runs = " + i + ", MWP = " + mwp);

			solutionSeries.add(i, 0.500);
			upperSeries.add(i, 0.501);
			lowerSeries.add(i, 0.499);
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(simulatorSeries);
	    dataset.addSeries(solutionSeries);
	    dataset.addSeries(upperSeries);
	    dataset.addSeries(lowerSeries);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final SimulatorRunsChart chart = new SimulatorRunsChart("Tennis Simulator Accuracy (best-of-five, p = 0.6, q = 0.4)");
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
