package tennis.graphs;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.OMalley;

public class TiebreakReturnSlightlyBetterChart extends XYLineChart{

	public TiebreakReturnSlightlyBetterChart() throws IOException
	{
	    super("Probability of winning a tiebreak with return pwp 0.02 better", "p", "tiebreak(p, 1-(p-0.02)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Tiebreak");
	    for(double i = 0.02; i <= 1.0; i += 0.01)
	    {
			series.add(i, OMalley.tiebreak(i, 1 - (i - 0.02)));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final TiebreakReturnSlightlyBetterChart chart = new TiebreakReturnSlightlyBetterChart();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
