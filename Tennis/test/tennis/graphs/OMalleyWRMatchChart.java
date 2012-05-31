package tennis.graphs;

import java.io.IOException;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.CurrentGameScore;
import tennis.omalley.CurrentMatchScore;
import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalleyCount;
import tennis.omalley.OMalleyCount.MatchAnalysis;
import tennis.omalley.OMalleyWRAnalytically;

public class OMalleyWRMatchChart extends XYLineChart
{
	private final double p;
	private final double q;
	private final double r;
	private final boolean servingNext;
	private final int numSetsToWin;
	private final double retirementRisk;

	public OMalleyWRMatchChart(final double p, final double q, final double r, final boolean servingNext, final int numSetsToWin, final double retirementRisk) throws IOException
	{
		super("OMalleyWithRetirementLevels", "Target Games", "MWP");
		this.p = p;
		this.q = q;
		this.r = r;
		this.servingNext = servingNext;
		this.numSetsToWin = numSetsToWin;
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
		for (int i = 1; i < 3; i++)
		{
			for (int j = 0; j < 7; j++)
			{
				System.out.println("i = " + i + ", j = " + j);

				final MatchAnalysis analysis = OMalleyCount.matchInProgressCount(p, q, new CurrentMatchScore(i, i-1), new CurrentSetScore(j, j+1),
																					      new CurrentGameScore(),
																					      servingNext,
																				 numSetsToWin, 0, 0);

				System.out.println("Expected Mwp: " + analysis.mwp);
				System.out.println("Recursion Levels Remaining: " + analysis.levels);
				mwpSeries.add(games, analysis.mwp);

				final double modifiedMwp = OMalleyWRAnalytically.matchInProgressWithRetirement(p, q, new CurrentMatchScore(i, i-1),
																									 new CurrentSetScore(j, j+1),
																									 new CurrentGameScore(),
																							   servingNext, numSetsToWin, retirementRisk, analysis.levels, analysis.mwp);
				modifiedMwpSeries.add(games, modifiedMwp);

				mwpDifferenceSeries.add(games, analysis.mwp - modifiedMwp);
				riskDifferenceSeries.add(games, Math.abs((analysis.mwp - modifiedMwp) - retirementRisk));
				games++;
			}
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
	    final OMalleyWRMatchChart chart = new OMalleyWRMatchChart(0.55, 0.46, 0.01, true, 3, 0.2);
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
