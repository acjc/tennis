package tennis.graphs.odds;

import static tennis.graphs.helper.PlayerOdds.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;

import tennis.graphs.helper.MatchOdds;
import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.helper.SetOdds;
import au.com.bytecode.opencsv.CSVReader;

public abstract class OddsChart extends ApplicationFrame
{
	protected final String title;
	protected final PlayerOdds favourite;
	protected final PlayerOdds underdog;
	private final String xLabel;
	private final String yLabel;

	public OddsChart(final String title, final String xLabel, final String yLabel, final PlayerOdds favourite, final PlayerOdds underdog) throws IOException
	{
		super(title);
		this.title = title;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
		this.favourite = favourite;
		this.underdog = underdog;
	}

	public void buildChart() throws IOException
	{
		final JFreeChart chart = createTimeChart();
		final XYItemRenderer renderer = ((XYPlot) chart.getPlot()).getRenderer();
	    renderer.setSeriesPaint(0, Color.BLUE);
	    renderer.setSeriesPaint(1, Color.RED);
	    renderer.setSeriesPaint(2, new Color(87, 107, 47));
		final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new Dimension(1200, 670));
	    setContentPane(chartPanel);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\matches\\" + title + ".png"), chart, 1200, 670);
	}

	protected abstract XYDataset createDataset() throws FileNotFoundException, IOException;

	protected JFreeChart createTimeChart() throws IOException
	{
	    final XYDataset dataset = createDataset();
		final JFreeChart chart = ChartFactory.createTimeSeriesChart(
	    	title,
	    	xLabel,
	    	yLabel,
	        dataset,
	        true,                    			 // legend
	        true,                     			 // tooltips
	        false                     			// urls
	    );

	    prepareChart(dataset, chart);

	    return chart;
	}

	protected JFreeChart createXYChart() throws IOException
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

	    prepareChart(dataset, chart);

	    return chart;
	}

	private void prepareChart(final XYDataset dataset, final JFreeChart chart)
	{
	    final TextTitle chartTitle = chart.getTitle();
	    chartTitle.setFont(new Font("sansserif", Font.BOLD, 28));
		chart.setBackgroundPaint(new Color(255, 255, 255, 0));
		chart.setBackgroundImageAlpha(0.0f);

		final LegendTitle legend = chart.getLegend();
		legend.setItemFont(new Font("serif", Font.BOLD, 24));
		legend.setLegendItemGraphicPadding(new RectangleInsets(5, 10, 5, 0));
		legend.setItemLabelPadding(new RectangleInsets(5, 10, 5, 10));
		legend.setBackgroundPaint(new Color(255, 255, 255, 0));

	    final XYPlot plot = chart.getXYPlot();
	    plot.setBackgroundPaint(new Color(255, 255, 255, 0));
	    plot.setBackgroundImageAlpha(0.0f);
	    plot.setDomainGridlinePaint(Color.lightGray);
	    plot.setRangeGridlinePaint(Color.lightGray);

	    plot.getRangeAxis().setRange(0.0, 1.0);
	    plot.getRangeAxis().setLabelFont(new Font("sansserif", Font.BOLD, 24));
	    plot.getRangeAxis().setTickLabelFont(new Font("sansserif", Font.PLAIN, 20));
	    plot.getDomainAxis().setLabelFont(new Font("sansserif", Font.BOLD, 24));
	    plot.getDomainAxis().setTickLabelFont(new Font("sansserif", Font.PLAIN, 20));

	    final XYSplineRenderer renderer = new XYSplineRenderer();

	    final Stroke stroke = new  BasicStroke(3.0f);
	    for(int i = 0; i < dataset.getSeriesCount(); i++)
	    {
	         renderer.setSeriesStroke(i, stroke);
	    }
	    renderer.setLegendLine(new Rectangle(30, 10));

	    renderer.setBaseShapesVisible(false);
	    plot.setRenderer(renderer);
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

	protected double calculateCorrectedSetOddsProbability(final List<List<SetOdds>> favouriteSetOdds, final List<List<SetOdds>> underdogSetOdds, final long time)
	{
		final List<SetOdds> bets = findMatchingBets(favouriteSetOdds, underdogSetOdds, time);
		final int numSetsToWin = bets.size() / 2;

		// Add up recent LPM bets
		double lpmProbability = 0;
		for (int i = 0; i < numSetsToWin; i++)
		{
			if (bets.get(i).isMatchedBet())
			{
				lpmProbability += bets.get(i).getOddsProbability();
			}
		}
		// Probability can't be greater than 1!...
		lpmProbability = lpmProbability > 1 ? 1.0 : lpmProbability;

		// Subtract recent LPM bets from total probability
		double remainingProbability = 1.0;
		for (int i = 0; i < bets.size(); i++)
		{
			if (bets.get(i).isMatchedBet())
			{
				remainingProbability -= bets.get(i).getOddsProbability();
			}
		}
		remainingProbability = remainingProbability < 0 ? 0.0 : remainingProbability;

		final double overround = calculateOverround(bets, remainingProbability);

		// Crossmatch while taking into account overround
		double crossmatchedProbability = 0;
		for (int i = 0; i < numSetsToWin; i++)
		{
			if (!bets.get(i).isMatchedBet())
			{
				final double bestBackPrice = bets.get(i).getBestBackPrice();
				double newOdds = bestBackPrice;
				double sum = 0;
				for (int j = 0; j < bets.size(); j++)
				{
					if (i != j && !bets.get(j).isMatchedBet())
					{
						final double bestLayPrice = bets.get(j).getBestLayPrice();
						if (bestLayPrice == -1) // If no lay price for some other non-recent selection, forget crossmatching, have to use best back price
						{
							if (bestBackPrice == -1)
							{
								newOdds = bets.get(i).getOdds();
							}
							else
							{
								newOdds = bestBackPrice;
							}
							break;
						}
						else
						{
							sum += 1 / bestLayPrice;
							newOdds = 1 / (remainingProbability - sum);
						}
					}
				}
				crossmatchedProbability += 1 / (newOdds * overround);
			}
		}

		return crossmatchedProbability + lpmProbability;
	}

	private double calculateOverround(final List<SetOdds> bets, final double remainingProbability)
	{
		double actualRemainingProbability = 0.0;
		for (int i = 0; i < bets.size(); i++)
		{
			if (!bets.get(i).isMatchedBet())
			{
				final double bestBackPrice = bets.get(i).getBestBackPrice();
				double newProbability = 1 / bestBackPrice;
				double sum = 0;
				for (int j = 0; j < bets.size(); j++)
				{
					if (i != j && !bets.get(j).isMatchedBet())
					{
						final double bestLayPrice = bets.get(j).getBestLayPrice();
						if (bestLayPrice == -1)
						{
							if (bestBackPrice == -1)
							{
								newProbability = bets.get(i).getOddsProbability();
							}
							else
							{
								newProbability = 1 / bestBackPrice;
							}
							break;
						}
						else
						{
							sum += 1 / bestLayPrice;
							newProbability = remainingProbability - sum;
						}
					}
				}
				actualRemainingProbability += newProbability;
			}
		}

		return remainingProbability == 0 ? 1.0 : actualRemainingProbability / remainingProbability;
	}

	private List<SetOdds> findMatchingBets(final List<List<SetOdds>> favouriteSetOdds, final List<List<SetOdds>> underdogSetOdds, final long time)
	{
		final List<List<SetOdds>> setOdds = new ArrayList<List<SetOdds>>();
		setOdds.addAll(favouriteSetOdds);
		setOdds.addAll(underdogSetOdds);

		// Ideally want a recent (compared to the time of the given match odds bet) matched bet for each possible scoreline
		final List<List<SetOdds>> lpmSetBets = getLpmSetBets(setOdds);

		final List<SetOdds> matchingBets = new ArrayList<SetOdds>();
		for (int i = 0; i < setOdds.size(); i++)
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
				matchingBets.add(findMatchingLayOffer(setOdds.get(i), time));
			}
		}

		return matchingBets;
	}

	private List<List<SetOdds>> getLpmSetBets(final List<List<SetOdds>> setOdds)
	{
		// Initialise results list
		final List<List<SetOdds>> result = new ArrayList<List<SetOdds>>();
		for (int i = 0; i < setOdds.size(); i++)
		{
			result.add(new ArrayList<SetOdds>());
		}

		for (int i = 0; i < setOdds.size(); i++)
		{
			// First bet
			SetOdds currentBet = setOdds.get(i).get(0);
			result.get(i).add(new SetOdds(currentBet.getTime(), currentBet.getDate(), currentBet.getOdds(), currentBet.getBestBackPrice(), currentBet.getBestLayPrice(), true));
			for (final SetOdds bet : setOdds.get(i))
			{
				// Anytime the LPM value changes means a new matched bet has occurred
				if (bet.getOdds() != currentBet.getOdds())
				{
					currentBet = bet;
					result.get(i).add(new SetOdds(currentBet.getTime(), currentBet.getDate(), currentBet.getOdds(), currentBet.getBestBackPrice(), currentBet.getBestLayPrice(), true));
				}
			}
		}

		return result;
	}

	private boolean isCandidateBet(final SetOdds bet, final long time)
	{
		final long interval = 300000; // 5 minutes
		// Time difference is less than 'interval' milliseconds or market has stopped trading
		return Math.abs(time - bet.getTime()) <= interval || (bet.getTime() > time && bet.getOdds() >= 600);
	}

	private SetOdds findMatchingLayOffer(final List<SetOdds> setOdds, final long time)
	{
		SetOdds candidateBet = null;
		for (final SetOdds bet : setOdds)
		{
			// Found closer odds entry
			if (candidateBet == null || Math.abs(time - bet.getTime()) < Math.abs(time - candidateBet.getTime()))
			{
				candidateBet = bet;
			}
			// Definitely no closer odds entry exists
			if (candidateBet != null && (bet.getTime() - time) > Math.abs(time - candidateBet.getTime()))
			{
				return candidateBet;
			}
		}

		return candidateBet;
	}
}
