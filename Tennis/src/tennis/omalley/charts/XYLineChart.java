package tennis.omalley.charts;

import java.awt.Color;
import java.awt.Dimension;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

public abstract class XYLineChart extends ApplicationFrame
{
	private final String title;
	private final String xLabel;
	private final String yLabel;

	public XYLineChart(final String title, final String xLabel, final String yLabel)
	{
	    super(title);
		this.title = title;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		final ChartPanel chartPanel = new ChartPanel(createXYLineChart());
	    chartPanel.setPreferredSize(new Dimension(500, 270));
	    setContentPane(chartPanel);
	}

	protected abstract XYDataset createDataset();

	private JFreeChart createXYLineChart()
	{
	    final JFreeChart chart = ChartFactory.createXYLineChart(
	        title,
	        xLabel,
	        yLabel,
	        createDataset(),
	        PlotOrientation.VERTICAL,
	        false,                    			 // legend
	        true,                     			 // tooltips
	        false                     			// urls
	    );

	    chart.setBackgroundPaint(Color.white);

	    final XYPlot plot = chart.getXYPlot();
	    plot.setBackgroundPaint(Color.lightGray);
	    plot.setDomainGridlinePaint(Color.white);
	    plot.setRangeGridlinePaint(Color.white);
	    plot.getDomainAxis().setMinorTickCount(10);

	    final XYSplineRenderer renderer = new XYSplineRenderer();
	    renderer.setSeriesLinesVisible(0, true);
	    renderer.setSeriesShapesVisible(0, false);
	    plot.setRenderer(renderer);

	    return chart;
	}
}
