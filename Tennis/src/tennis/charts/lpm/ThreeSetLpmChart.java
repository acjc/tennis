package tennis.charts.lpm;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

import tennis.charts.helper.PlayerOdds;

public abstract class ThreeSetLpmChart extends ApplicationFrame
{
	protected final String title;
	protected final PlayerOdds player;

	public ThreeSetLpmChart(final String targetPlayer, final PlayerOdds playerOdds) throws IOException
	{
		super(playerOdds.getTitle());
		this.title = playerOdds.getTitle() + " (" + targetPlayer + ")";
		this.player = playerOdds;

		final ChartPanel chartPanel = new ChartPanel(createTimeSeriesChart());
	    chartPanel.setPreferredSize(new Dimension(1000, 570));
	    setContentPane(chartPanel);
	}

	protected abstract XYDataset createDataset() throws FileNotFoundException, IOException;

	private JFreeChart createTimeSeriesChart() throws IOException
	{
	    final JFreeChart chart = ChartFactory.createTimeSeriesChart(
	    	title,
	    	"Time",
	    	"Implied Probability",
	        createDataset(),
	        true,                    			 // legend
	        true,                     			 // tooltips
	        false                     			// urls
	    );

	    chart.setBackgroundPaint(Color.white);

	    final XYPlot plot = chart.getXYPlot();
	    plot.setBackgroundPaint(Color.lightGray);
	    plot.setDomainGridlinePaint(Color.white);
	    plot.setRangeGridlinePaint(Color.white);
	    plot.getRangeAxis().setRange(0.0, 125.0);

	    ChartUtilities.saveChartAsPNG(new File("doc\\" + title + ".png"), chart, 1000, 570);

	    return chart;
	}
}
