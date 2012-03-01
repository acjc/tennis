package tennis.graphs.lpm;

import static tennis.graphs.helper.PlayerOdds.LPM_INDEX;
import static tennis.graphs.helper.PlayerOdds.TIME_INDEX;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;

import tennis.graphs.helper.MatchOdds;
import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.helper.SetOdds;
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

	    ChartUtilities.saveChartAsPNG(new File("graphs\\matches\\" + title + ".png"), chart, 1000, 570);

	    return chart;
	}

	protected boolean updateOdds(final List<CSVReader> matchOddsReaders, final List<CSVReader> setOddsReaders, final List<String []> matchOdds, final List<String []> setOdds) throws IOException
	{
		matchOdds.clear();
		for (int i = 0; i < matchOddsReaders.size(); i++)
		{
			matchOdds.add(matchOddsReaders.get(i).readNext());
		}
		final Second matchOddsTime = new Second(new Date(Long.parseLong(matchOdds.get(0)[TIME_INDEX])));

		for (int i = 0; i < setOddsReaders.size(); i++)
		{
			setOdds.add(setOddsReaders.get(i).readNext());
		}
		final Second setOddsTime = new Second(new Date(Long.parseLong(setOdds.get(0)[TIME_INDEX])));

		return true;
	}

	private boolean endOfData(final List<String[]> oddsData)
	{
		for (int i = 0; i < oddsData.size(); i++)
		{
			if (oddsData.get(i) == null || oddsData.get(i)[LPM_INDEX].equals("-1"))
			{
				return true;
			}
		}

		return false;
	}

	private boolean endOfData(final String[] currentLine)
	{
		return currentLine == null || currentLine[LPM_INDEX].equals("-1");
	}

	protected List<MatchOdds> parseMatchOdds(final CSVReader matchOddsReader) throws IOException
	{
		final List<MatchOdds> result = new ArrayList<MatchOdds>();
		String [] currentLine = matchOddsReader.readNext();
		while (!endOfData(currentLine))
		{
			result.add(new MatchOdds(Long.parseLong(currentLine[TIME_INDEX]), Double.parseDouble(currentLine[LPM_INDEX])));
			currentLine = matchOddsReader.readNext();
		}

		return result;
	}

	protected List<SetOdds> parseSetOdds(final List<CSVReader> setOddsReaders) throws IOException
	{
		final List<SetOdds> result = new ArrayList<SetOdds>();
		final List<String []> currentLines = new ArrayList<String []>();
		for (final CSVReader reader : setOddsReaders)
		{
			currentLines.add(reader.readNext());
		}
		final double [] odds = new double[setOddsReaders.size()];
		while (!endOfData(currentLines))
		{
			for (int i = 0; i < odds.length; i++)
			{
				odds[i] = Double.parseDouble(currentLines.get(i)[LPM_INDEX]);
			}

			result.add(new SetOdds(Long.parseLong(currentLines.get(0)[TIME_INDEX]), odds));

			currentLines.clear();
			for (final CSVReader reader : setOddsReaders)
			{
				currentLines.add(reader.readNext());
			}
		}

		return result;
	}

	protected double [] getCorrectedMatchOdds(final List<String[]> matchOddsData)
	{
		double overround = 0;
		for (int i = 0; i < matchOddsData.size(); i++)
		{
			overround += 1 / Double.parseDouble(matchOddsData.get(i)[LPM_INDEX]);
		}

		final double [] result = new double[matchOddsData.size()];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = Double.parseDouble(matchOddsData.get(i)[LPM_INDEX]) * overround;
		}
		return result;
	}

	protected double [] getCorrectedSetOdds(final List<String[]> setOddsData)
	{
		double overround = 0;
		for (int i = 0; i < setOddsData.size(); i++)
		{
			overround += 1 / Double.parseDouble(setOddsData.get(i)[LPM_INDEX]);
		}

		final double [] result = new double[setOddsData.size()];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = Double.parseDouble(setOddsData.get(i)[LPM_INDEX]) * overround;
		}
		return result;
	}
}
