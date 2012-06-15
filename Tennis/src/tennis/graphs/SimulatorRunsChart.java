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
	public void buildChart() throws IOException
	{
		final JFreeChart chart = createChart();
		((XYPlot) chart.getPlot()).getRangeAxis().setRange(0.48, 0.52);
		final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new Dimension(1000, 570));
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
	    for(double i = 100; i <= 25000; i += 100)
	    {
			final double mwp = new Simulator().simulate(0.6, 0.6, i).proportionTargetWon();
			simulatorSeries.add(i, mwp);
			System.out.println("Runs = " + i + ", MWP = " + mwp);

			solutionSeries.add(i, 0.50);
			upperSeries.add(i, 0.51);
			lowerSeries.add(i, 0.49);
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
	    final SimulatorRunsChart chart = new SimulatorRunsChart("Tennis Simulator Accuracy (best-of-five, pa = 0.6, pb = 0.6)");
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
