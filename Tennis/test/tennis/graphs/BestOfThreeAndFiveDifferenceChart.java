package tennis.graphs;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.OMalley;

public class BestOfThreeAndFiveDifferenceChart extends XYLineChart
{
	public BestOfThreeAndFiveDifferenceChart() throws IOException
	{
	    super("Difference between probabilities of winning a best-of-3 and best-of-5 set matches with equal return pwp", "p", "bestOfThree(p, p");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("MatchDifference");
	    for(double i = 0; i <= 1.0; i += 0.01)
	    {
			series.add(i, OMalley.bestOfFive(i, i) - OMalley.bestOfThree(i, i));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final BestOfThreeAndFiveDifferenceChart chart = new BestOfThreeAndFiveDifferenceChart();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
