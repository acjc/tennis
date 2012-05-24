package tennis.graphs;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.CurrentGameScore;
import tennis.omalley.OMalleyCount;
import tennis.omalley.OMalleyCount.MatchAnalysis;
import tennis.omalley.OMalleyWithRetirement;

public class RhoGuessGameChart extends XYLineChart
{
	public RhoGuessGameChart() throws IOException
	{
	    super("MWP Difference Game", "r", "MWP Difference");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries mwpDifference1 = new XYSeries("MWP Difference 1");
		final XYSeries mwpDifference2 = new XYSeries("MWP Difference 2");
		final XYSeries mwpDifference3 = new XYSeries("MWP Difference 3");
		final XYSeries mwpDifference4 = new XYSeries("MWP Difference 4");
		final XYSeries mwpDifference5 = new XYSeries("MWP Difference 5");

		final double rho = 0.2;
	    for(double r = 0; r <= 0.5; r += 0.01)
	    {
			MatchAnalysis analysis = OMalleyCount.gameInProgressCount(0.55, 0.01);
			double modifiedMwp = OMalleyWithRetirement.gameInProgressWithRetirement(0.55, new CurrentGameScore(), r, rho, analysis.mwp);
			mwpDifference1.add(r, analysis.mwp - modifiedMwp);

			analysis = OMalleyCount.gameInProgressCount(0.55, 0.01, new CurrentGameScore(1, 0), 0, 0, 0);
			modifiedMwp = OMalleyWithRetirement.gameInProgressWithRetirement(0.55, new CurrentGameScore(1, 0), r, rho, analysis.mwp);
			mwpDifference2.add(r, analysis.mwp - modifiedMwp);

			analysis = OMalleyCount.gameInProgressCount(0.55, 0.01, new CurrentGameScore(2, 1), 0, 0, 0);
			modifiedMwp = OMalleyWithRetirement.gameInProgressWithRetirement(0.55, new CurrentGameScore(2, 1), r, rho, analysis.mwp);
			mwpDifference3.add(r, analysis.mwp - modifiedMwp);

			analysis = OMalleyCount.gameInProgressCount(0.55, 0.01, new CurrentGameScore(2, 2), 0, 0, 0);
			modifiedMwp = OMalleyWithRetirement.gameInProgressWithRetirement(0.55, new CurrentGameScore(2, 2), r, rho, analysis.mwp);
			mwpDifference4.add(r, analysis.mwp - modifiedMwp);

			analysis = OMalleyCount.gameInProgressCount(0.55, 0.01, new CurrentGameScore(3, 2), 0, 0, 0);
			modifiedMwp = OMalleyWithRetirement.gameInProgressWithRetirement(0.55, new CurrentGameScore(3, 2), r, rho, analysis.mwp);
			mwpDifference5.add(r, analysis.mwp - modifiedMwp);
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(mwpDifference1);
	    dataset.addSeries(mwpDifference2);
	    dataset.addSeries(mwpDifference3);
	    dataset.addSeries(mwpDifference4);
	    dataset.addSeries(mwpDifference5);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final RhoGuessGameChart chart = new RhoGuessGameChart();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
