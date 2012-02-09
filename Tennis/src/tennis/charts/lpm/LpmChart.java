package tennis.charts.lpm;

import static tennis.charts.helper.PlayerOdds.LPM_INDEX;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

import tennis.charts.helper.PlayerOdds;
import au.com.bytecode.opencsv.CSVReader;

public abstract class LpmChart extends ApplicationFrame
{
	protected final String title;
	protected final PlayerOdds favourite;
	protected final PlayerOdds underdog;

	public LpmChart(final String title, final PlayerOdds favourite, final PlayerOdds underdog) throws IOException
	{
		super(title);
		this.title = title;
		this.favourite = favourite;
		this.underdog = underdog;

		final ChartPanel chartPanel = new ChartPanel(createTimeSeriesChart());
	    chartPanel.setPreferredSize(new Dimension(1000, 570));
	    setContentPane(chartPanel);
	}

	protected abstract XYDataset createDataset() throws FileNotFoundException, IOException;

	protected JFreeChart createTimeSeriesChart() throws IOException
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

	protected boolean updateOdds(final List<CSVReader> matchOddsReaders, final List<CSVReader> setOddsReaders, final List<String []> matchOdds, final List<String []> setOdds) throws IOException
	{
		matchOdds.clear();
		for (int i = 0; i < matchOddsReaders.size(); i++)
		{
			matchOdds.add(matchOddsReaders.get(i).readNext());
		}

		setOdds.clear();
		for (int i = 0; i < setOddsReaders.size(); i++)
		{
			setOdds.add(setOddsReaders.get(i).readNext());
		}

		for (int i = 0; i < matchOddsReaders.size(); i++)
		{
			if (matchOdds.get(i)[LPM_INDEX].equals("-1"))
			{
				return false;
			}
		}
		for (int i = 0; i < setOddsReaders.size(); i++)
		{
			if (setOdds.get(i)[LPM_INDEX].equals("-1"))
			{
				return false;
			}
		}

		return true;
	}

	protected double [] getCorrectedMatchOdds(final List<String[]> matchOdds)
	{
		double overround = 0;
		for (int i = 0; i < matchOdds.size(); i++)
		{
			overround += 1 / Double.parseDouble(matchOdds.get(i)[LPM_INDEX]);
		}

		final double [] result = new double[matchOdds.size()];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = Double.parseDouble(matchOdds.get(i)[LPM_INDEX]) * overround;
		}
		return result;
	}

	protected double [] getCorrectedSetOdds(final List<String[]> setOdds)
	{
		double overround = 0;
		for (int i = 0; i < setOdds.size(); i++)
		{
			overround += 1 / Double.parseDouble(setOdds.get(i)[LPM_INDEX]);
		}

		final double [] result = new double[setOdds.size()];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = Double.parseDouble(setOdds.get(i)[LPM_INDEX]) * overround;
		}
		return result;
	}
}
