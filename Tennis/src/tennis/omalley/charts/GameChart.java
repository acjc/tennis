package tennis.omalley.charts;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.probabilities.OMalley;

public class GameChart extends TennisChart{

	public GameChart()
	{
	    super("Probability of winning a game", "p", "game(p)");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries series = new XYSeries("Game");
	    final OMalley oMalley = new OMalley();
	    for(double i = 0; i < 1.0; i += 0.1) {
			series.add(i, oMalley.game(i));
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);

	    return dataset;
	}

	public static void main(final String[] args) {

	    final TennisChart chart = new GameChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
