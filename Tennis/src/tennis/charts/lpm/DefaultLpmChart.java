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

	    	final double matchOddsPercentage = getCorrectedMatchOddsPercentage(matchOdds);
			matchOddsSeries.add(time, matchOddsPercentage);

	    	final double setBettingPercentage = getCorrectedSetBettingPercentage(setOdds);
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
}
