package tennis.omalley.charts;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.probabilities.OMalley;

public class SetFromFourFiveOnServeEqualReturnChart extends XYLineChart
{
	public SetFromFourFiveOnServeEqualReturnChart() throws IOException
	{
	    super("Probability of winning the set when returning at 4-5", "p", "setInPlay(p, 0.5, 4, 5, false)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("FourFiveReturning");
	    for(double i = 0; i < 1.0; i += 0.02) {
			series.add(i, OMalley.setInPlay(i, 0.5, 4, 5, false));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final XYLineChart chart = new SetFromFourFiveOnServeEqualReturnChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
