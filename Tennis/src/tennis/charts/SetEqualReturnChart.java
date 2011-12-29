package tennis.charts;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.OMalley;

public class SetEqualReturnChart extends XYLineChart
{
	public SetEqualReturnChart() throws IOException
	{
	    super("Probability of winning a set", "p", "set(p, 0.5)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Set");
	    for(double i = 0; i < 1.0; i += 0.01)
	    {
			series.add(i, OMalley.set(i, 0.5));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final XYLineChart chart = new SetEqualReturnChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
