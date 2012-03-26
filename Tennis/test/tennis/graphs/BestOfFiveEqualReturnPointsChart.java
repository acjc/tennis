package tennis.graphs;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.CurrentGameScore;
import tennis.omalley.CurrentMatchScore;
import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalleyCount;

public class BestOfFiveEqualReturnPointsChart extends XYLineChart
{
	public BestOfFiveEqualReturnPointsChart() throws IOException
	{
	    super("Predicted Number of Points in a best-of-5 set match", "p", "Points");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Points");
	    for(double i = 0; i < 1.0; i += 0.01)
	    {
			series.add(i, OMalleyCount.matchInProgressCount(i, 0.5, new CurrentMatchScore(), new CurrentSetScore(), new CurrentGameScore(),
																	  Math.random() < 0.5 ? true : false, 3, 0, 0).points);
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final BestOfFiveEqualReturnPointsChart chart = new BestOfFiveEqualReturnPointsChart();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
