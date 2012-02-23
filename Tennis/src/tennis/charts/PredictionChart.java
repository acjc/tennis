package tennis.charts;

import java.awt.Dimension;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.simulator.Simulator;

public class PredictionChart extends XYLineChart
{
	private final String title;

	private final List<Double> predictions;

	public PredictionChart(final List<Double> predictions) throws IOException
	{
	    super("Match Model", "Points", "Match-winning Probability");
	    this.title = "Match Model";
		this.predictions = predictions;
	}

	@Override
	protected void drawChart() throws IOException
	{
		final ChartPanel chartPanel = new ChartPanel(createXYLineChart(createDataset()));
	    chartPanel.setPreferredSize(new Dimension(500, 270));
	    setContentPane(chartPanel);
	}

	@Override
	protected XYDataset createDataset()
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

	public static void main(final String[] args) throws IOException
	{
		final PredictionChart chart = new Simulator().simulate(0.6, 0.4, 1).targetPredictionChart();
		chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
