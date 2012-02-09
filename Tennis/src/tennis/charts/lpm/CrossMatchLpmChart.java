package tennis.charts.lpm;

import static tennis.charts.helper.PlayerOdds.DATE_INDEX;
import static tennis.charts.helper.PlayerOdds.TIME_INDEX;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import tennis.charts.helper.PlayerOdds;
import au.com.bytecode.opencsv.CSVReader;

public class CrossMatchLpmChart extends LpmChart
{
	public CrossMatchLpmChart(final PlayerOdds favourite, final PlayerOdds underdog) throws IOException
	{
		super(favourite.getTitle() + " (" + underdog.getSurname() + ")", favourite, underdog);
	}

	@Override
	protected XYDataset createDataset() throws FileNotFoundException, IOException
	{
		final TimeSeriesCollection dataset = new TimeSeriesCollection();

		final TimeSeries matchOddsSeries = new TimeSeries("Match Odds");
		final List<CSVReader> matchOddsReaders = new ArrayList<CSVReader>();
		matchOddsReaders.add(favourite.getMatchOdds());
		matchOddsReaders.add(underdog.getMatchOdds());

		final TimeSeries setBettingSeries = new TimeSeries("Set Betting");
		final List<CSVReader> setOddsReaders = new ArrayList<CSVReader>();
		setOddsReaders.addAll(favourite.getSetOdds());
		setOddsReaders.addAll(underdog.getSetOdds());

		final TimeSeries oddsDifferenceSeries = new TimeSeries("Odds Difference");

		final FileOutputStream fout = new FileOutputStream ("doc\\adam.txt");

		final List<String []> matchOdds = new ArrayList<String []>();
		final List<String []> setOdds = new ArrayList<String []>();

	    while (updateOdds(matchOddsReaders, setOddsReaders, matchOdds, setOdds))
	    {
	    	final Second time = new Second(new Date(Long.parseLong(matchOdds.get(0)[TIME_INDEX])));

	    	final double matchOddsPercentage = getCrossMatchedMatchOddsPercentage(getCorrectedMatchOdds(matchOdds));
			matchOddsSeries.add(time, matchOddsPercentage);

	    	final double setBettingPercentage = getCrossMatchedSetBettingPercentage(getCorrectedSetOdds(setOdds));
    		setBettingSeries.add(time, setBettingPercentage);

    		final double oddsDifference = Math.abs(matchOddsPercentage - setBettingPercentage);
    		oddsDifferenceSeries.add(time, oddsDifference);

			new PrintStream(fout).println(matchOdds.get(0)[DATE_INDEX] + ": " + oddsDifference);
	    }

	    dataset.addSeries(matchOddsSeries);
	    dataset.addSeries(setBettingSeries);
	    dataset.addSeries(oddsDifferenceSeries);

		return dataset;
	}

	private double getCrossMatchedSetBettingPercentage(final double[] setOdds)
	{
		double favouriteSum = 0;
		for (int i = 0; i < setOdds.length / 2; i++)
		{
			favouriteSum += 1 / setOdds[i];
		}

		double result = 0;
		for (int i = setOdds.length / 2; i < setOdds.length; i++)
		{
			double sum = favouriteSum;
			for (int j = setOdds.length / 2; j < setOdds.length; j++)
			{
				if (i != j)
				{
					sum += 1 / setOdds[i];
				}
			}
			result += 100 / (1 / (1 - sum));
		}

		return result;
	}

	private double getCrossMatchedMatchOddsPercentage(final double[] matchOdds)
	{
		return 100.0 / (1.0 / (1.0 - (1.0 / matchOdds[0])));
	}
}
