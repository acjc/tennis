package tennis.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import tennis.simulator.Simulator;

public class SimulatorRunsChart extends ApplicationFrame
{
	private final String title;

	public SimulatorRunsChart(final String title) throws IOException
	{
	    super(title);
	    this.title = title;
		final ChartPanel chartPanel = new ChartPanel(createXYLineChart());
	    chartPanel.setPreferredSize(new Dimension(800, 400));
	    setContentPane(chartPanel);
	}

	private JFreeChart createXYLineChart()
	{
	    final JFreeChart chart = ChartFactory.createXYLineChart(
	        title,
	        "Runs",
	        "Match-Winning Probability",
	        createDataset(),
	        PlotOrientation.VERTICAL,
	        false,                    			 // legend
	        true,                     			 // tooltips
	        false                     			// urls
	    );

	    chart.setBackgroundPaint(Color.white);

	    final XYPlot plot = chart.getXYPlot();
	    plot.setBackgroundPaint(Color.lightGray);
	    plot.setDomainGridlinePaint(Color.white);
	    plot.setRangeGridlinePaint(Color.white);
	    plot.getRangeAxis().setRange(0.944, 0.956);

	    final XYSplineRenderer renderer = new XYSplineRenderer();
	    for (int i = 0; i < 6; i++)
		{
	    	renderer.setSeriesLinesVisible(i, true);
	    	renderer.setSeriesShapesVisible(i, false);
		}
	    plot.setRenderer(renderer);

	    return chart;
	}

	private XYDataset createDataset()
	{
		final XYSeries simulatorSeries = new XYSeries("Simulator");
		final XYSeries solutionSeries = new XYSeries("Exact Solution");
		final XYSeries upperSeries3 = new XYSeries("Upper Bound (3dp)");
		final XYSeries lowerSeries3 = new XYSeries("Lower Bound (3dp)");
		final XYSeries upperSeries2 = new XYSeries("Upper Bound (2dp)");
		final XYSeries lowerSeries2 = new XYSeries("Lower Bound (2dp)");
	    for(double i = 10000; i <= 750000; i += 10000)
	    {
			final double mwp = new Simulator().simulate(0.55, 0.55, i).proportionMatchesWon();
			simulatorSeries.add(i, mwp);
			System.out.println("Runs = " + i + ", MWP = " + mwp);

			solutionSeries.add(i, 0.953);
			upperSeries3.add(i, 0.9535);
			lowerSeries3.add(i, 0.9525);
			upperSeries2.add(i, 0.955);
			lowerSeries2.add(i, 0.945);
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(simulatorSeries);
	    dataset.addSeries(solutionSeries);
	    dataset.addSeries(upperSeries3);
	    dataset.addSeries(lowerSeries3);
	    dataset.addSeries(upperSeries2);
	    dataset.addSeries(lowerSeries2);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final SimulatorRunsChart chart = new SimulatorRunsChart("Tennis Simulator Accuracy (best-of-five, p = q = 0.55)");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
