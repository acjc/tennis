package tennis.graphs.lpm;

import static tennis.graphs.helper.PlayerOdds.*;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
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
//		matchOdds.clear();
//		for (int i = 0; i < matchOddsReaders.size(); i++)
//		{
//			matchOdds.add(matchOddsReaders.get(i).readNext());
//		}
//		final Second matchOddsTime = new Second(new Date(Long.parseLong(matchOdds.get(0)[TIME_INDEX])));
//
//		for (int i = 0; i < setOddsReaders.size(); i++)
//		{
//			setOdds.add(setOddsReaders.get(i).readNext());
//		}
//		final Second setOddsTime = new Second(new Date(Long.parseLong(setOdds.get(0)[TIME_INDEX])));

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

	protected List<List<SetOdds>> parseSetOdds(final List<CSVReader> setOddsReaders) throws IOException
	{
		final List<List<SetOdds>> result = new ArrayList<List<SetOdds>>();
		for (int i = 0; i < setOddsReaders.size(); i++)
		{
			result.add(new ArrayList<SetOdds>());
		}

		final List<String []> currentLines = new ArrayList<String []>();
		for (final CSVReader reader : setOddsReaders)
		{
			currentLines.add(reader.readNext());
		}

		while (!endOfData(currentLines))
		{
			for (int i = 0; i < currentLines.size(); i++)
			{
				final String[] line = currentLines.get(i);
				result.get(i).add(new SetOdds(Long.parseLong(line[TIME_INDEX]),
											  line[DATE_INDEX],
											  Double.parseDouble(line[LPM_INDEX]),
											  Double.parseDouble(line[BACK_INDEX]),
											  false));
			}

			currentLines.clear();
			for (final CSVReader reader : setOddsReaders)
			{
				currentLines.add(reader.readNext());
			}
		}

		return result;
	}

	protected double calculateSetOddsPercentage(final List<List<SetOdds>> favouriteSetOdds, final List<List<SetOdds>> underdogSetOdds, final long time)
	{
		final List<SetOdds> bets = findMatchingBets(favouriteSetOdds, underdogSetOdds, time);
		final int numSetsToWin = bets.size() / 2;

		double normalisedProbability = 1.0;
		for (int i = 0; i < numSetsToWin; i++)
		{
			if (bets.get(i).isMatchedBet())
			{
				normalisedProbability -= bets.get(i).getOddsProbability();
			}
		}

		double crossmatchedPercentage = 0;
		for (int i = 0; i < numSetsToWin; i++)
		{
			if (!bets.get(i).isMatchedBet())
			{
				double sum = 0;
				for (int j = 0; j < bets.size(); j++)
				{
					if (i != j && !bets.get(j).isMatchedBet())
					{
						sum += 1 / bets.get(j).getLayPrice();
					}
				}
				crossmatchedPercentage += 100 / (1 / (normalisedProbability - sum));
			}
		}

		final double result = crossmatchedPercentage + (100 * (1 - normalisedProbability));
		return result;
	}

	private List<SetOdds> findMatchingBets(final List<List<SetOdds>> favouriteSetOdds, final List<List<SetOdds>> underdogSetOdds, final long time)
	{
		// Ideally want a recent (compared to the time of the given match odds bet) matched bet for each possible scoreline
		final List<List<SetOdds>> lpmSetBets = getLpmSetBets(favouriteSetOdds);
		final List<SetOdds> matchingBets = new ArrayList<SetOdds>();
		for (int i = 0; i < favouriteSetOdds.size(); i++)
		{
			SetOdds candidateBet = null;
			for (final SetOdds bet : lpmSetBets.get(i))
			{
				// We want the bet made closest in time to the given match odds bet...
				if (isCandidateBet(bet, time) && (candidateBet == null || Math.abs(time - bet.getTime()) < Math.abs(time - candidateBet.getTime())))
				{
					candidateBet = bet;
				}
			}
			if (candidateBet != null)
			{
				matchingBets.add(candidateBet);
			}
			else // Otherwise, find the best lay price at the given time
			{
				matchingBets.add(findMatchingLayOffer(favouriteSetOdds.get(i), time));
			}
		}

		for (final List<SetOdds> odds : underdogSetOdds)
		{
			matchingBets.add(findMatchingLayOffer(odds, time));
		}

		return matchingBets;
	}

	private List<List<SetOdds>> getLpmSetBets(final List<List<SetOdds>> setOdds)
	{
		final List<List<SetOdds>> result = new ArrayList<List<SetOdds>>();
		for (int i = 0; i < setOdds.size(); i++)
		{
			result.add(new ArrayList<SetOdds>());
		}

		for (int i = 0; i < setOdds.size(); i++)
		{
			SetOdds currentBet = setOdds.get(i).get(0);
			for (final SetOdds bet : setOdds.get(i))
			{
				if (bet.getOdds() != currentBet.getOdds())
				{
					result.get(i).add(new SetOdds(bet.getTime(), bet.getDate(), bet.getOdds(), bet.getLayPrice(), true));
				}
				currentBet = bet;
			}
		}

		return result;
	}

	private boolean isCandidateBet(final SetOdds bet, final long time)
	{
		final long interval = 300000; // 5 minutes
		return Math.abs(time - bet.getTime()) <= interval || (bet.getTime() < time && bet.getOdds() == 1000);
	}

	private SetOdds findMatchingLayOffer(final List<SetOdds> setOdds, final long time)
	{
		SetOdds candidateBet = null;
		for (final SetOdds bet : setOdds)
		{
			if (candidateBet == null || Math.abs(time - bet.getTime()) < Math.abs(time - candidateBet.getTime()))
			{
				candidateBet = bet;
			}
			if (candidateBet != null && bet.getTime() - time > Math.abs(time - candidateBet.getTime()))
			{
				return candidateBet;
			}
		}

		return candidateBet;
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
