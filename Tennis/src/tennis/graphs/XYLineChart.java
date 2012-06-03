package tennis.graphs;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

public abstract class XYLineChart extends ApplicationFrame
{
	protected final String title;
	protected final String xLabel;
	protected final String yLabel;

	public XYLineChart(final String title, final String xLabel, final String yLabel) throws IOException
	{
	    super(title);
		this.title = title;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
	}

	protected void buildChart() throws IOException
	{
		final JFreeChart chart = createXYLineChart(createDataset());
		final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new Dimension(1000, 570));
	    setContentPane(chartPanel);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\" + title + ".png"), chart, 1000, 570);
	}

	protected abstract XYDataset createDataset() throws IOException;

	protected JFreeChart createXYLineChart(final XYDataset dataset) throws IOException
	{
		final JFreeChart chart = ChartFactory.createXYLineChart(
			title,
	        xLabel,
	        yLabel,
	        dataset,
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

	    final XYSplineRenderer renderer = new XYSplineRenderer();
	    renderer.setBaseShapesVisible(false);
	    plot.setRenderer(renderer);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\" + title + ".png"), chart, 1000, 570);

	    return chart;
	}
}
