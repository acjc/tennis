package tennis.graphs;

import java.awt.Dimension;
import java.io.IOException;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.omalley.CurrentGameScore;
import tennis.omalley.OMalleyCount;
import tennis.omalley.OMalleyCount.MatchAnalysis;
import tennis.omalley.OMalleyWithRetirement;

public class OMalleyWRGameChart extends XYLineChart
{
	private final double p;
	private final double r;
	private final double retirementRisk;

	public OMalleyWRGameChart(final double p, final double r, final double retirementRisk) throws IOException
	{
		super("OMalleyWithRetirementLevelsGame", "Target Games", "MWP");
		this.p = p;
		this.r = r;
		this.retirementRisk = retirementRisk;
	}

	@Override
	protected void buildChart() throws IOException
	{
		final JFreeChart chart = createXYLineChart(createDataset());
		((XYPlot) chart.getPlot()).getRangeAxis().setRange(0, 1);
		final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new Dimension(1000, 570));
	    setContentPane(chartPanel);
	}

	@Override
	protected XYDataset createDataset()
	{
		final XYSeries mwpSeries = new XYSeries("MWP");
		final XYSeries modifiedMwpSeries = new XYSeries("Modified MWP");
		final XYSeries mwpDifferenceSeries = new XYSeries("MWP Difference");
		final XYSeries riskDifferenceSeries = new XYSeries("Risk Difference");

		for (int i = 0; i <= 100; i++)
		{
			System.out.println(i);

			final int targetScore = (int) (Math.random() * 4);
			final int opponentScore = (int) (Math.random() * 4);
			System.out.println("(" + targetScore + ", " + opponentScore + ")");

			final MatchAnalysis analysis = OMalleyCount.gameInProgressCount(p, r, new CurrentGameScore(targetScore, opponentScore), 0, 0, 0);

			System.out.println("Expected Mwp: " + analysis.mwp);
			System.out.println("Recursion Levels Remaining: " + analysis.levels);
			mwpSeries.add(i, analysis.mwp);

			final double modifiedMwp = OMalleyWithRetirement.gameInProgressWithRetirement(p, new CurrentGameScore(targetScore, opponentScore), retirementRisk, analysis.levels, analysis.mwp);
			modifiedMwpSeries.add(i, modifiedMwp);

			final double mwpDifference = analysis.mwp - modifiedMwp;
			mwpDifferenceSeries.add(i, mwpDifference);
			riskDifferenceSeries.add(i, Math.abs(mwpDifference - retirementRisk));

			System.out.println();
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
	    final OMalleyWRGameChart chart = new OMalleyWRGameChart(0.55, 0.01, 0.2);
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
