package tennis.graphs.lpm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import tennis.graphs.helper.MatchOdds;
import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.helper.SetOdds;
import au.com.bytecode.opencsv.CSVReader;

public class DefaultLpmChart extends LpmChart
{
	public DefaultLpmChart(final PlayerOdds favourite, final PlayerOdds underdog) throws IOException
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
		final List<SetOdds> favouriteSetOdds = parseSetOdds(favouriteSetOddsReaders);
		final List<SetOdds> underdogSetOdds = parseSetOdds(underdogSetOddsReaders);

	    for (int i = 0; i < favouriteMatchOdds.size(); i++)
	    {
	    	matchOddsSeries.add(favouriteMatchOdds.get(i).getTime(), favouriteMatchOdds.get(i).getOddsPercentage());

//    		final double oddsDifference = Math.abs(matchOddsPercentage - setOddsPercentage);
//    		oddsDifferenceSeries.add(time, oddsDifference);

//			new PrintStream(fout).println(favouriteMatchOddsData.get(0)[DATE_INDEX] + ", " + favouriteMatchOddsData.get(0)[LPM_INDEX]);
	    }

    	for (int i = 0; i < favouriteSetOdds.size(); i++)
		{
			setBettingSeries.add(favouriteSetOdds.get(i).getTime(), favouriteSetOdds.get(i).getOddsPercentage());
		}

	    dataset.addSeries(matchOddsSeries);
	    dataset.addSeries(setBettingSeries);
	    dataset.addSeries(oddsDifferenceSeries);

		return dataset;
	}
}
