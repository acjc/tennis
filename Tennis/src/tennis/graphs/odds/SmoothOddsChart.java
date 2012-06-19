package tennis.graphs.odds;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import tennis.graphs.helper.MatchOdds;
import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.helper.SetOdds;
import au.com.bytecode.opencsv.CSVReader;

public class SmoothOddsChart extends OddsChart
{
	public SmoothOddsChart(final PlayerOdds favourite, final PlayerOdds underdog) throws IOException
	{
		super("'Smoothed' evolution of Betfair odds data for " + favourite.getName() + " (" + favourite.getSurname() + " vs. " + underdog.getSurname() + ")", favourite, underdog);
	}

	@Override
	protected XYDataset createDataset() throws FileNotFoundException, IOException
	{
		final TimeSeriesCollection dataset = new TimeSeriesCollection();

		final TimeSeries matchOddsSeries = new TimeSeries("Match Odds Market");
		final CSVReader favouriteMatchOddsReader = favourite.getMatchOdds();

		final TimeSeries setBettingSeries = new TimeSeries("Set Betting Market");
		final List<CSVReader> favouriteSetOddsReaders = new ArrayList<CSVReader>();
		final List<CSVReader> underdogSetOddsReaders = new ArrayList<CSVReader>();
		favouriteSetOddsReaders.addAll(favourite.getSetOdds());
		underdogSetOddsReaders.addAll(underdog.getSetOdds());

		final TimeSeries oddsDifferenceSeries = new TimeSeries("Difference between Set Betting and Match Odds Markets");

//		final FileOutputStream fout = new FileOutputStream ("doc\\adam.txt");

		final List<MatchOdds> favouriteMatchOdds = parseMatchOdds(favouriteMatchOddsReader);
		final List<List<SetOdds>> favouriteSetOdds = parseSetOdds(favouriteSetOddsReaders);
		final List<List<SetOdds>> underdogSetOdds = parseSetOdds(underdogSetOddsReaders);

		final List<Double> matchOddsProbabilities = new ArrayList<Double>();
		final List<Double> setOddsProbabilities = new ArrayList<Double>();

	    for (int i = 0; i < favouriteMatchOdds.size(); i++)
	    {
	    	final long time = favouriteMatchOdds.get(i).getTime();
			matchOddsProbabilities.add(favouriteMatchOdds.get(i).getOddsProbability());
			setOddsProbabilities.add(calculateCorrectedSetOddsProbability(favouriteSetOdds, underdogSetOdds, time));
	    }

	    double matchOddsWindowSum = 0;
	    double setOddsWindowSum = 0;
	    for (int i = 0; i < 100; i++)
	    {
	    	matchOddsWindowSum += matchOddsProbabilities.get(i);
	    	setOddsWindowSum += setOddsProbabilities.get(i);
	    }

	    for (int i = 50; i < matchOddsProbabilities.size() - 50; i++)
		{
	    	matchOddsWindowSum += matchOddsProbabilities.get(i + 50);
	    	setOddsWindowSum += setOddsProbabilities.get(i + 50);

	    	final long time = favouriteMatchOdds.get(i).getTime();
			final Second second = new Second(new Date(time));

	    	final double matchOddsProbability = matchOddsWindowSum / 101.0;
	    	final double setOddsProbability = setOddsWindowSum / 101.0;
			matchOddsSeries.add(second, matchOddsProbability);
			setBettingSeries.add(second, setOddsProbability);

    		final double oddsDifference = setOddsProbability - matchOddsProbability;
			final double risk = oddsDifference >= 0 ? oddsDifference : 0;
    		oddsDifferenceSeries.add(second, risk);

	    	matchOddsWindowSum = matchOddsWindowSum - matchOddsProbabilities.get(i - 50);
	    	setOddsWindowSum = setOddsWindowSum - setOddsProbabilities.get(i - 50);
		}

//			new PrintStream(fout).println(favouriteMatchOddsData.get(0)[DATE_INDEX] + ", " + favouriteMatchOddsData.get(0)[LPM_INDEX]);

	    dataset.addSeries(matchOddsSeries);
	    dataset.addSeries(setBettingSeries);
	    dataset.addSeries(oddsDifferenceSeries);

		return dataset;
	}
}
