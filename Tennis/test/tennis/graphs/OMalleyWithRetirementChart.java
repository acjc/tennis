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
import tennis.omalley.OMalleyWithRetirement;

public class OMalleyWithRetirementChart extends XYLineChart
{
	private final double p;
	private final double q;
	private final boolean servingNext;
	private final int numSetsToWin;
	private final double retirementRisk;

	public OMalleyWithRetirementChart(final double p, final double q, final boolean servingNext, final int numSetsToWin, final double retirementRisk) throws IOException
	{
		super("OMalleyWithRetirement", "Target Games", "MWP");
		this.p = p;
		this.q = q;
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

		final double expectedPoints = OMalleyCount.matchInProgressCount(p, q, new CurrentMatchScore(),
																					    new CurrentSetScore(),
																						new CurrentGameScore(),
																				  (Math.random() < 0.5) ? true : false, numSetsToWin, 0, 0).points;
		System.out.println("Expected Points: " + expectedPoints);
		int games = 0;
		for (int i = 1; i < 3; i++)
		{
			for (int j = 0; j < 5; j++)
			{
				final MatchAnalysis analysis = OMalleyCount.matchInProgressCount(p, q, new CurrentMatchScore(i, i),
																								 new CurrentSetScore(j, j+1),
																								 new CurrentGameScore(),
																						   servingNext, numSetsToWin, 0, 0);

				System.out.println("Points Remaining: " + analysis.points);
				mwpSeries.add(games, analysis.mwp);

				final double modifiedMwp = OMalleyWithRetirement.matchInProgressWithRetirement(p, q, new CurrentMatchScore(i, i-1),
																									 new CurrentSetScore(j, j+1),
																									 new CurrentGameScore(),
																							   servingNext, numSetsToWin, retirementRisk, analysis.points, expectedPoints);
				modifiedMwpSeries.add(games, modifiedMwp);

				mwpDifferenceSeries.add(games, Math.abs(analysis.mwp - modifiedMwp));
				riskDifferenceSeries.add(games, Math.abs(Math.abs(analysis.mwp - modifiedMwp) - retirementRisk));
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
	    final OMalleyWithRetirementChart chart = new OMalleyWithRetirementChart(0.55, 0.46, true, 3, 0.2);
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
