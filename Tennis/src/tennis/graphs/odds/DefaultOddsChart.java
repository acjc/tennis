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

public class DefaultOddsChart extends OddsChart
{
	public DefaultOddsChart(final PlayerOdds favourite, final PlayerOdds underdog) throws IOException
	{
		super("Evolution of Betfair odds data for " + favourite.getName() + " (" + favourite.getSurname() + " vs. " + underdog.getSurname() + ")", "Time", "Implied Probability", favourite, underdog);
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

	    for (int i = 0; i < favouriteMatchOdds.size(); i++)
	    {
	    	final long time = favouriteMatchOdds.get(i).getTime();
			final Second second = new Second(new Date(time));
			final double matchOddsProbability = favouriteMatchOdds.get(i).getOddsProbability();
			final double setOddsProbability = calculateCorrectedSetOddsProbability(favouriteSetOdds, underdogSetOdds, time);

			matchOddsSeries.add(second, matchOddsProbability);
			setBettingSeries.add(second, setOddsProbability);

    		final double oddsDifference = setOddsProbability - matchOddsProbability;
			final double risk = oddsDifference >= 0 ? oddsDifference : 0;
    		oddsDifferenceSeries.add(second, risk);

//			new PrintStream(fout).println(favouriteMatchOddsData.get(0)[DATE_INDEX] + ", " + favouriteMatchOddsData.get(0)[LPM_INDEX]);
	    }

	    dataset.addSeries(setBettingSeries);
	    dataset.addSeries(matchOddsSeries);
	    dataset.addSeries(oddsDifferenceSeries);

		return dataset;
	}
}
