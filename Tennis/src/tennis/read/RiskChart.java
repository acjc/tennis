package tennis.read;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import tennis.graphs.XYLineChart;

public class RiskChart extends XYLineChart
{
	private final List<Double> pointLevelRisks;
	private final List<Double> risks;

	public RiskChart(final String favourite, final String favSurname, final String undSurname, final List<Double> pointLevelRisks, final List<Double> risks) throws IOException
	{
		super("Evolution of modelled retirement risk for " + favourite + " (" + favSurname + " vs. " + undSurname + ")", "Point", "Risk");
		this.pointLevelRisks = pointLevelRisks;
		this.risks = risks;
	}

	@Override
	public void buildChart() throws IOException
	{
		final JFreeChart chart = createChart();
		final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new Dimension(1000, 570));
	    setContentPane(chartPanel);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\matches\\" + title + ".png"), chart, 1000, 570);
	}

	@Override
	protected XYDataset createDataset() throws IOException
	{
		final XYSeries pointLevelRisksSeries = new XYSeries("Point-level Retirement Risk");
		final XYSeries risksSeries = new XYSeries("Match Remainder Retirement Risk");

		for (int i = 0; i < risks.size(); i++)
		{
			pointLevelRisksSeries.add(i, pointLevelRisks.get(i));
			risksSeries.add(i, risks.get(i));
		}

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(pointLevelRisksSeries);
	    dataset.addSeries(risksSeries);

	    return dataset;
	}

	@Override
	protected JFreeChart createChart() throws IOException
	{
		final JFreeChart chart = ChartFactory.createXYLineChart(
				title,
		        "Point",
		        "Implied Probability",
		        createDataset(),
		        PlotOrientation.VERTICAL,
		        true,                    			 // legend
		        true,                     			 // tooltips
		        false                     			// urls
		    );

	    chart.setBackgroundPaint(Color.white);

	    final XYPlot plot = chart.getXYPlot();
	    plot.setBackgroundPaint(Color.white);
	    plot.setDomainGridlinePaint(Color.lightGray);
	    plot.setRangeGridlinePaint(Color.lightGray);
	    plot.getRangeAxis().setRange(0.0, 1.0);

	    final XYItemRenderer renderer = plot.getRenderer();
	    renderer.setSeriesPaint(0, Color.MAGENTA);
	    renderer.setSeriesPaint(1, Color.GREEN);

	    return chart;
	}
}
