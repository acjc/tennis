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

	public void buildChart() throws IOException
	{
		final JFreeChart chart = createChart();
		final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new Dimension(1200, 670));
	    setContentPane(chartPanel);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\" + title + ".png"), chart, 1200, 670);
	}

	protected abstract XYDataset createDataset() throws IOException;

	protected JFreeChart createChart() throws IOException
	{
		final XYDataset dataset = createDataset();
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

//	    final TextTitle chartTitle = chart.getTitle();
//	    chartTitle.setFont(new Font("sansserif", Font.BOLD, 28));
//
//		final LegendTitle legend = chart.getLegend();
//		legend.setItemFont(new Font("serif", Font.BOLD, 24));
//		legend.setLegendItemGraphicPadding(new RectangleInsets(5, 10, 5, 0));
//		legend.setItemLabelPadding(new RectangleInsets(5, 10, 5, 10));

	    final XYPlot plot = chart.getXYPlot();
	    plot.setBackgroundPaint(Color.white);
	    plot.setDomainGridlinePaint(Color.lightGray);
	    plot.setRangeGridlinePaint(Color.lightGray);
	    plot.getRangeAxis().setRange(0.0, 1.0);
//	    plot.getRangeAxis().setLabelFont(new Font("sansserif", Font.BOLD, 24));
//	    plot.getRangeAxis().setTickLabelFont(new Font("sansserif", Font.PLAIN, 20));
//	    plot.getDomainAxis().setLabelFont(new Font("sansserif", Font.BOLD, 24));
//	    plot.getDomainAxis().setTickLabelFont(new Font("sansserif", Font.PLAIN, 20));
//
	    final XYSplineRenderer renderer = new XYSplineRenderer();
//	    final Stroke stroke = new BasicStroke(3.0f);
//	    for(int i = 0; i < dataset.getSeriesCount(); i++)
//	    {
//	         renderer.setSeriesStroke(i, stroke);
//	    }
//
//	    renderer.setLegendLine(new Rectangle(30, 15));
	    renderer.setBaseShapesVisible(false);

	    plot.setRenderer(renderer);

	    return chart;
	}
}
