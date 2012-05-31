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

import tennis.simulator.GameState;
import tennis.simulator.MatchState;
import tennis.simulator.SetState;
import tennis.simulator.SimulatorWR;
import tennis.simulator.SimulatorWRPareto;

public class SimulatorWRParetoRunsChart extends XYLineChart
{
	public SimulatorWRParetoRunsChart(final String title) throws IOException
	{
	    super(title, "Runs", "Retirement Rate");
	}

	@Override
	protected void buildChart() throws IOException
	{
		final JFreeChart chart = createXYLineChart(createDataset());
		((XYPlot) chart.getPlot()).getRangeAxis().setRange(0.19, 0.21);
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

		final SimulatorWR simulator = new SimulatorWRPareto(2.6446, 200.0, 0.8940, true);
		final MatchState initialState = new MatchState(1, 0, new SetState(1, 2), new GameState(true), 3);

	    for(double i = 10000; i <= 200000; i += 10000)
	    {
			final double rate = simulator.simulate(0.63, 0.61, initialState, true, i).proportionTargetRetirements();
			simulatorSeries.add(i, rate);
			System.out.println("Runs = " + i + ", Rate of Retirement = " + rate);

			solutionSeries.add(i, 0.2);
			upperSeries.add(i, 0.205);
			lowerSeries.add(i, 0.195);
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
	    final SimulatorWRParetoRunsChart chart = new SimulatorWRParetoRunsChart("Tennis Simulator Retirement Rate Accuracy (best-of-five, pa = 0.6, pb = 0.6)");
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
