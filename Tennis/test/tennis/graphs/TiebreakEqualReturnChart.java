package tennis.graphs;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.graphs.XYLineChart;
import tennis.omalley.OMalley;

public class TiebreakEqualReturnChart extends XYLineChart{

	public TiebreakEqualReturnChart() throws IOException
	{
	    super("Probability of winning a tiebreak", "p", "tiebreak(p, 0.5)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Tiebreak");
	    for(double i = 0; i < 1.0; i += 0.01)
	    {
			series.add(i, OMalley.tiebreak(i, 0.5));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final TiebreakEqualReturnChart chart = new TiebreakEqualReturnChart();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}