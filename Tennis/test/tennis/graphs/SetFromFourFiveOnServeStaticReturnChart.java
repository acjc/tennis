package tennis.graphs;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalley;

public class SetFromFourFiveOnServeStaticReturnChart extends XYLineChart
{
	public SetFromFourFiveOnServeStaticReturnChart() throws IOException
	{
	    super("Probability of winning the set when returning at 4-5", "p", "setInPlay(p, 0.5, 4, 5, false)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("FourFiveReturning");
	    for(double i = 0; i < 1.0; i += 0.01)
	    {
			series.add(i, OMalley.setInProgress(i, 0.5, new CurrentSetScore(4, 5), false));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final SetFromFourFiveOnServeStaticReturnChart chart = new SetFromFourFiveOnServeStaticReturnChart();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
