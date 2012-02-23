package tennis.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
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

public class PredictionChart extends ApplicationFrame
{
	private final String title;

	public PredictionChart(final List<Double> predictions) throws IOException
	{
	    super("Match Model");
		this.title = "Match Model";

		final ChartPanel chartPanel = new ChartPanel(createXYLineChart(createDataset(predictions)));
	    chartPanel.setPreferredSize(new Dimension(500, 270));
	    setContentPane(chartPanel);
	}

	private XYDataset createDataset(final List<Double> predictions)
	{
		final XYSeries series = new XYSeries(title);
	    for(int i = 0; i < predictions.size(); i++)
	    {
			series.add(i, predictions.get(i));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	private JFreeChart createXYLineChart(final XYDataset dataset) throws IOException
	{
	    final JFreeChart chart = ChartFactory.createXYLineChart(
	        title,
	        "Points",
	        "Match-winning Probability",
	        dataset,
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

	    final XYSplineRenderer renderer = new XYSplineRenderer();
	    renderer.setSeriesLinesVisible(0, true);
	    renderer.setSeriesShapesVisible(0, false);
	    plot.setRenderer(renderer);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\" + title + ".png"), chart, 1000, 570);

	    return chart;
	}

	public static void main(final String[] args) throws IOException
	{
		final PredictionChart chart = new Simulator().simulate(0.6, 0.4, 1).targetPredictionChart();
		chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
