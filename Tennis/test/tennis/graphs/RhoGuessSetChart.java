package tennis.graphs;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.CurrentGameScore;
import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalleyCount;
import tennis.omalley.OMalleyCount.MatchAnalysis;
import tennis.omalley.OMalleyWithRetirement;

public class RhoGuessSetChart extends XYLineChart
{
	public RhoGuessSetChart() throws IOException
	{
	    super("MWP Difference Set", "r", "MWP Difference");
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries mwpDifference1 = new XYSeries("MWP Difference 1");
		final XYSeries mwpDifference2 = new XYSeries("MWP Difference 2");
		final XYSeries mwpDifference3 = new XYSeries("MWP Difference 3");
		final XYSeries mwpDifference4 = new XYSeries("MWP Difference 4");
		final XYSeries mwpDifference5 = new XYSeries("MWP Difference 5");
		final XYSeries mwpDifference6 = new XYSeries("MWP Difference 6");
		final XYSeries mwpDifference7 = new XYSeries("MWP Difference 7");

		final double rho = 0.2;
	    for(double r = 0; r <= 0.5; r += 0.01)
	    {
			MatchAnalysis analysis = OMalleyCount.setInProgressCount(0.55, 0.46, true);
			double modifiedMwp = OMalleyWithRetirement.setInProgressWithRetirement(0.55, 0.46, new CurrentSetScore(), new CurrentGameScore(), true, r, rho, analysis.mwp);
			mwpDifference1.add(r, analysis.mwp - modifiedMwp);

			analysis = OMalleyCount.setInProgressCount(0.55, 0.46, new CurrentSetScore(1, 0), new CurrentGameScore(), true, 0, 0);
			modifiedMwp = OMalleyWithRetirement.setInProgressWithRetirement(0.55, 0.46, new CurrentSetScore(1, 0), new CurrentGameScore(), true, r, rho, analysis.mwp);
			mwpDifference2.add(r, analysis.mwp - modifiedMwp);

			analysis = OMalleyCount.setInProgressCount(0.55, 0.46, new CurrentSetScore(2, 1), new CurrentGameScore(), true, 0, 0);
			modifiedMwp = OMalleyWithRetirement.setInProgressWithRetirement(0.55, 0.46, new CurrentSetScore(2, 1), new CurrentGameScore(), true, r, rho, analysis.mwp);
			mwpDifference3.add(r, analysis.mwp - modifiedMwp);

			analysis = OMalleyCount.setInProgressCount(0.55, 0.46, new CurrentSetScore(1, 4), new CurrentGameScore(), true, 0, 0);
			modifiedMwp = OMalleyWithRetirement.setInProgressWithRetirement(0.55, 0.46, new CurrentSetScore(1, 4), new CurrentGameScore(), true, r, rho, analysis.mwp);
			mwpDifference4.add(r, analysis.mwp - modifiedMwp);

			analysis = OMalleyCount.setInProgressCount(0.55, 0.46, new CurrentSetScore(4, 3), new CurrentGameScore(), true, 0, 0);
			modifiedMwp = OMalleyWithRetirement.setInProgressWithRetirement(0.55, 0.46, new CurrentSetScore(4, 3), new CurrentGameScore(), true, r, rho, analysis.mwp);
			mwpDifference5.add(r, analysis.mwp - modifiedMwp);

			analysis = OMalleyCount.setInProgressCount(0.55, 0.46, new CurrentSetScore(4, 4), new CurrentGameScore(), true, 0, 0);
			modifiedMwp = OMalleyWithRetirement.setInProgressWithRetirement(0.55, 0.46, new CurrentSetScore(4, 4), new CurrentGameScore(), true, r, rho, analysis.mwp);
			mwpDifference6.add(r, analysis.mwp - modifiedMwp);

			analysis = OMalleyCount.setInProgressCount(0.55, 0.46, new CurrentSetScore(5, 4), new CurrentGameScore(), true, 0, 0);
			modifiedMwp = OMalleyWithRetirement.setInProgressWithRetirement(0.55, 0.46, new CurrentSetScore(5, 4), new CurrentGameScore(), true, r, rho, analysis.mwp);
			mwpDifference7.add(r, analysis.mwp - modifiedMwp);
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(mwpDifference1);
	    dataset.addSeries(mwpDifference2);
	    dataset.addSeries(mwpDifference3);
	    dataset.addSeries(mwpDifference4);
	    dataset.addSeries(mwpDifference5);
	    dataset.addSeries(mwpDifference6);
	    dataset.addSeries(mwpDifference7);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final RhoGuessSetChart chart = new RhoGuessSetChart();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
