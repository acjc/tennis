package tennis.charts;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.OMalley;

public class BestOfThreeEqualReturnChart extends XYLineChart
{
	public BestOfThreeEqualReturnChart() throws IOException
	{
	    super("Probability of winning a best-of-3 set match", "p", "bestOfThree(p, 0.5)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("BestOfThree");
	    for(double i = 0; i < 1.0; i += 0.02)
	    {
			series.add(i, OMalley.bestOfThree(i, 0.5));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final XYLineChart chart = new BestOfThreeEqualReturnChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
