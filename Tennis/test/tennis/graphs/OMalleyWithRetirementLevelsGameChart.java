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

public class OMalleyWithRetirementLevelsGameChart extends XYLineChart
{
	private final double p;
	private final double retirementRisk;

	public OMalleyWithRetirementLevelsGameChart(final double p, final double retirementRisk) throws IOException
	{
		super("OMalleyWithRetirementLevelsGame", "Target Games", "MWP");
		this.p = p;
		this.retirementRisk = retirementRisk;
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries mwpSeries = new XYSeries("MWP");
		final XYSeries modifiedMwpSeries = new XYSeries("Modified MWP");
		final XYSeries mwpDifferenceSeries = new XYSeries("MWP Difference");
		final XYSeries riskDifferenceSeries = new XYSeries("Risk Difference");

		int games = 0;
		for (int i = 0; i < 4; i++)
		{
			System.out.println("i = " + i);

			final MatchAnalysis analysis = OMalleyCount.gameInProgressCount(p, new CurrentGameScore(i, i+1), 0, 0);

			System.out.println("Expected Mwp: " + analysis.mwp);
			System.out.println("Recursion Levels Remaining: " + analysis.levels);
			mwpSeries.add(games, analysis.mwp);

			final double modifiedMwp = OMalleyWithRetirement.gameInProgressWithRetirement(p, new CurrentGameScore(i, i+1), retirementRisk, analysis.levels, analysis.mwp);
			modifiedMwpSeries.add(games, modifiedMwp);

			final double mwpDifference = analysis.mwp - modifiedMwp;
			mwpDifferenceSeries.add(games, mwpDifference);
			riskDifferenceSeries.add(games, Math.abs(mwpDifference - retirementRisk));
			games++;
		}

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(mwpSeries);
	    dataset.addSeries(modifiedMwpSeries);
	    dataset.addSeries(mwpDifferenceSeries);
	    dataset.addSeries(riskDifferenceSeries);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final OMalleyWithRetirementLevelsGameChart chart = new OMalleyWithRetirementLevelsGameChart(0.55, 0.2);
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
