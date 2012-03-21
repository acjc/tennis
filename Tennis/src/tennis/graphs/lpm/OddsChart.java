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

public abstract class OddsChart extends ApplicationFrame
{
	protected final String title;
	protected final PlayerOdds favourite;
	protected final PlayerOdds underdog;

	public OddsChart(final String title, final PlayerOdds favourite, final PlayerOdds underdog) throws IOException
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
											  Double.parseDouble(line[LAY_INDEX]),
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

	protected double calculateCorrectedSetOddsPercentage(final List<List<SetOdds>> favouriteSetOdds, final List<List<SetOdds>> underdogSetOdds, final long time)
	{
		final List<SetOdds> bets = findMatchingBets(favouriteSetOdds, underdogSetOdds, time);
		return calculateSetOddsProbability(bets) * 100;
	}

	private double calculateSetOddsProbability(final List<SetOdds> bets)
	{
		final int numSetsToWin = bets.size() / 2;

		double normalisedProbability = 1.0;
		for (int i = 0; i < numSetsToWin; i++)
		{
			if (bets.get(i).isMatchedBet())
			{
				normalisedProbability -= bets.get(i).getOddsProbability();
			}
		}

		final double overround = calculateOverround(bets, normalisedProbability);

		double crossmatchedProbability = 0;
		for (int i = 0; i < numSetsToWin; i++)
		{
			if (!bets.get(i).isMatchedBet())
			{
				double sum = 0;
				double newProbability = 0;
				for (int j = 0; j < bets.size(); j++)
				{
					if (i != j && !bets.get(j).isMatchedBet())
					{
						final double bestLayPrice = bets.get(j).getBestLayPrice();
						if (bestLayPrice == -1)
						{
							newProbability = 1 / (bets.get(i).getBestBackPrice() * overround);
							break;
						}
						else
						{
							sum += 1 / (bestLayPrice * overround);
							newProbability = normalisedProbability - sum;
						}
					}
				}
				crossmatchedProbability += newProbability;
			}
		}

		return crossmatchedProbability + (1 - normalisedProbability);
	}

	private double calculateOverround(final List<SetOdds> bets, final double normalisedProbability)
	{
		double remainingProbability = 0.0;
		for (int i = 0; i < bets.size(); i++)
		{
			if (!bets.get(i).isMatchedBet())
			{
				double sum = 0;
				double newProbability = 0;
				for (int j = 0; j < bets.size(); j++)
				{
					if (i != j && !bets.get(j).isMatchedBet())
					{
						final double bestLayPrice = bets.get(j).getBestLayPrice();
						if (bestLayPrice == -1)
						{
							newProbability = 1 / bets.get(i).getBestBackPrice();
							break;
						}
						else
						{
							sum += 1 / bestLayPrice;
							newProbability = normalisedProbability - sum;
						}
					}
				}
				remainingProbability += newProbability;
			}
		}

		return remainingProbability / normalisedProbability;
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
					result.get(i).add(new SetOdds(bet.getTime(), bet.getDate(), bet.getOdds(), bet.getBestBackPrice(), bet.getBestLayPrice(), true));
				}
				currentBet = bet;
			}
		}

		return result;
	}

	private boolean isCandidateBet(final SetOdds bet, final long time)
	{
		final long interval = 300000; // 5 minutes
		return Math.abs(time - bet.getTime()) <= interval;
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
}
