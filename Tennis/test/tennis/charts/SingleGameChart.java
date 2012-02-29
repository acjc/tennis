package tennis.charts;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.graphs.XYLineChart;
import tennis.omalley.OMalley;

public class SingleGameChart extends XYLineChart
{
	public SingleGameChart() throws IOException
	{
	    super("Probability of winning a game", "p", "game(p)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Game");
	    for(double i = 0; i < 1.0; i += 0.01)
	    {
			series.add(i, OMalley.game(i));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final XYLineChart chart = new SingleGameChart();
	    chart.pack();
	    chart.buildChart();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
