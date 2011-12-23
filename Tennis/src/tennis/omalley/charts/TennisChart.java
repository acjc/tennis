package tennis.omalley.charts;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

public abstract class TennisChart extends ApplicationFrame{

	private final String title;
	private final String xLabel;
	private final String yLabel;

	public TennisChart(final String title, final String xLabel, final String yLabel)
	{
	    super(title);
		this.title = title;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		final ChartPanel chartPanel = new ChartPanel(createChart());
	    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	    setContentPane(chartPanel);
	}

	protected abstract XYDataset createDataset();

	private JFreeChart createChart()
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

	    final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	    renderer.setSeriesLinesVisible(0, true);
	    plot.setRenderer(renderer);

	    return chart;
	}
}
