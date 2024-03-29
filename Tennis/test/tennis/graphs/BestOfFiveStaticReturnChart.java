package tennis.graphs;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.OMalley;

public class BestOfFiveStaticReturnChart extends XYLineChart
{
	public BestOfFiveStaticReturnChart() throws IOException
	{
	    super("Probability of winning a best-of-5 set match with return pwp 0.5", "p", "bestOfFive(p, 0.5)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("BestOfFive");
	    for(double i = 0; i < 1.0; i += 0.01)
	    {
			series.add(i, OMalley.bestOfFive(i, 0.5));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final BestOfFiveStaticReturnChart chart = new BestOfFiveStaticReturnChart();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
