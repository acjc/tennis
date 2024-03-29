package tennis.graphs;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalley;

public class SetFromThreeAllOnServeStaticReturnChart extends XYLineChart
{
	public SetFromThreeAllOnServeStaticReturnChart() throws IOException
	{
	    super("Probability of winning the set when on serve at 3-3", "p", "setInPlay(p, 0.5, 3, 3, true)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("ThreeAllOnServe");
	    for(double i = 0; i < 1.0; i += 0.01)
	    {
			series.add(i, OMalley.setInProgress(i, 0.5, new CurrentSetScore(3, 3), true));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final SetFromThreeAllOnServeStaticReturnChart chart = new SetFromThreeAllOnServeStaticReturnChart();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
