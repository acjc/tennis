package tennis.read;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import tennis.graphs.XYLineChart;

public class RiskChart extends XYLineChart
{
	private final String savename;
	private final List<Double> pointLevelRisks;
	private final List<Double> matchLevelRisks;

	public RiskChart(final String favourite, final String favSurname, final String undSurname, final String savename, final List<Double> pointLevelRisks, final List<Double> risks) throws IOException
	{
		super("Evolution of modelled retirement risk for " + favourite + " (" + favSurname + " vs. " + undSurname + ")", "Point", "Risk");
		this.savename = savename;
		this.pointLevelRisks = pointLevelRisks;
		this.matchLevelRisks = risks;
	}

	public RiskChart(final String savename, final List<Double> pointLevelRisks, final List<Double> risks) throws IOException
	{
		super("Evolution of modelled retirement risks for an artificial match", "Point", "Risk");
		this.savename = savename;
		this.pointLevelRisks = pointLevelRisks;
		this.matchLevelRisks = risks;
	}

	@Override
	public void buildChart() throws IOException
	{
		final JFreeChart chart = createChart();
		final ChartPanel chartPanel = new ChartPanel(chart);
		final XYItemRenderer renderer = ((XYPlot) chart.getPlot()).getRenderer();
	    renderer.setSeriesPaint(0, new Color(175, 0, 175));
	    renderer.setSeriesPaint(1, new Color(48, 128, 20));
	    chartPanel.setPreferredSize(new Dimension(1200, 670));
	    setContentPane(chartPanel);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\matches\\" + savename + ".png"), chart, 1200, 670);
	}

	@Override
	protected XYDataset createDataset() throws IOException
	{
		final XYSeries pointLevelRisksSeries = new XYSeries("Point-level Retirement Risk");
		final XYSeries matchLevelRisksSeries = new XYSeries("Match Remainder Retirement Risk");

		for (int i = 0; i < matchLevelRisks.size(); i++)
		{
			pointLevelRisksSeries.add(i, pointLevelRisks.get(i));
			matchLevelRisksSeries.add(i, matchLevelRisks.get(i));
		}

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(pointLevelRisksSeries);
	    dataset.addSeries(matchLevelRisksSeries);

	    return dataset;
	}
}
