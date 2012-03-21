package tennis.graphs.lpm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
		super(favourite.getTitle() + " (" + favourite.getSurname() + ")", favourite, underdog);
	}

	@Override
	protected XYDataset createDataset() throws FileNotFoundException, IOException
	{
		final TimeSeriesCollection dataset = new TimeSeriesCollection();

		final TimeSeries matchOddsSeries = new TimeSeries("Match Odds");
		final CSVReader favouriteMatchOddsReader = favourite.getMatchOdds();
		final CSVReader underdogMatchOddsReader = underdog.getMatchOdds();

		final TimeSeries setBettingSeries = new TimeSeries("Set Odds");
		final List<CSVReader> favouriteSetOddsReaders = new ArrayList<CSVReader>();
		final List<CSVReader> underdogSetOddsReaders = new ArrayList<CSVReader>();
		favouriteSetOddsReaders.addAll(favourite.getSetOdds());
		underdogSetOddsReaders.addAll(underdog.getSetOdds());

		final TimeSeries oddsDifferenceSeries = new TimeSeries("Odds Difference");

		final FileOutputStream fout = new FileOutputStream ("doc\\adam.txt");

		final List<MatchOdds> favouriteMatchOdds = parseMatchOdds(favouriteMatchOddsReader);
		final List<MatchOdds> underdogMatchOdds = parseMatchOdds(underdogMatchOddsReader);
		final List<List<SetOdds>> favouriteSetOdds = parseSetOdds(favouriteSetOddsReaders);
		final List<List<SetOdds>> underdogSetOdds = parseSetOdds(underdogSetOddsReaders);

	    for (int i = 0; i < favouriteMatchOdds.size(); i++)
	    {
	    	final long time = favouriteMatchOdds.get(i).getTime();
			final Second second = new Second(new Date(time));
			final double matchOddsPercentage = favouriteMatchOdds.get(i).getOddsPercentage();
			final double setOddsPercentage = calculateCorrectedSetOddsPercentage(favouriteSetOdds, underdogSetOdds, time);

			matchOddsSeries.add(second, matchOddsPercentage);
			setBettingSeries.add(second, setOddsPercentage);

    		final double oddsDifference = Math.abs(matchOddsPercentage - setOddsPercentage);
    		oddsDifferenceSeries.add(second, oddsDifference);

//			new PrintStream(fout).println(favouriteMatchOddsData.get(0)[DATE_INDEX] + ", " + favouriteMatchOddsData.get(0)[LPM_INDEX]);
	    }

	    dataset.addSeries(matchOddsSeries);
	    dataset.addSeries(setBettingSeries);
	    dataset.addSeries(oddsDifferenceSeries);

		return dataset;
	}
}
