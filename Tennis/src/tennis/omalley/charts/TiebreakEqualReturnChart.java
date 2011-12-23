package tennis.omalley.charts;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.probabilities.OMalley;

public class TiebreakEqualReturnChart extends TennisChart{

	public TiebreakEqualReturnChart()
	{
	    super("Probability of winning a tiebreak", "p", "tiebreak(p, 0.5)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Tiebreak");
	    final OMalley oMalley = new OMalley();
	    for(double i = 0; i < 1.0; i += 0.1) {
			series.add(i, oMalley.tiebreak(i, 0.5));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) {

	    final TennisChart chart = new TiebreakEqualReturnChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
