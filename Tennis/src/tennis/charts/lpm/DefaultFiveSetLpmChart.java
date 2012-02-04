package tennis.charts.lpm;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import org.jfree.chart.ChartPanel;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import tennis.charts.helper.PlayerOdds;
import au.com.bytecode.opencsv.CSVReader;

public class DefaultFiveSetLpmChart extends LpmChart
{
	private final PlayerOdds favourite;
	private final PlayerOdds underdog;

	public DefaultFiveSetLpmChart(final PlayerOdds favourite, final PlayerOdds underdog) throws IOException
	{
		super(favourite.getTitle() + " (" + favourite.getSurname() + ")");
		this.favourite = favourite;
		this.underdog = underdog;

		final ChartPanel chartPanel = new ChartPanel(createTimeSeriesChart());
	    chartPanel.setPreferredSize(new Dimension(1000, 570));
	    setContentPane(chartPanel);
	}

	@Override
	protected XYDataset createDataset() throws FileNotFoundException, IOException
	{
		final TimeSeriesCollection dataset = new TimeSeriesCollection();

		final TimeSeries matchOddsSeries = new TimeSeries("Match Odds");
		final CSVReader favouriteMatchOddsReader = new CSVReader(new FileReader(favourite.getMatchOdds()));
		final CSVReader underdogMatchOddsReader = new CSVReader(new FileReader(underdog.getMatchOdds()));

		final TimeSeries setBettingSeries = new TimeSeries("Set Betting");
		final CSVReader favouriteThreeNilReader = new CSVReader(new FileReader(favourite.getThreeNil()));
		final CSVReader favouriteThreeOneReader = new CSVReader(new FileReader(favourite.getThreeOne()));
		final CSVReader favouriteThreeTwoReader = new CSVReader(new FileReader(favourite.getThreeTwo()));
		final CSVReader underdogThreeNilReader = new CSVReader(new FileReader(underdog.getThreeNil()));
		final CSVReader underdogThreeOneReader = new CSVReader(new FileReader(underdog.getThreeOne()));
		final CSVReader underdogThreeTwoReader = new CSVReader(new FileReader(underdog.getThreeTwo()));

		final TimeSeries oddsDifferenceSeries = new TimeSeries("Odds Difference");

		final FileOutputStream fout = new FileOutputStream ("doc\\adam.txt");

	    String [] favouriteMatchOddsNextLine;
	    while ((favouriteMatchOddsNextLine = favouriteMatchOddsReader.readNext()) != null)
	    {
	    	final String [] underdogMatchOddsNextLine = underdogMatchOddsReader.readNext();
	    	final String [] favouriteThreeNilNextLine = favouriteThreeNilReader.readNext();
	    	final String [] favouriteThreeOneNextLine = favouriteThreeOneReader.readNext();
	    	final String [] favouriteThreeTwoNextLine = favouriteThreeTwoReader.readNext();
	    	final String [] underdogThreeNilNextLine = underdogThreeNilReader.readNext();
	    	final String [] underdogThreeOneNextLine = underdogThreeOneReader.readNext();
	    	final String [] underdogThreeTwoNextLine = underdogThreeTwoReader.readNext();

	    	if(favouriteMatchOddsNextLine[6].equals("-1") || underdogMatchOddsNextLine[6].equals("-1")
	    	   || favouriteThreeNilNextLine[6].equals("-1") || favouriteThreeOneNextLine[6].equals("-1") || favouriteThreeTwoNextLine[6].equals("-1")
	    	   || underdogThreeNilNextLine[6].equals("-1") || underdogThreeOneNextLine[6].equals("-1") || underdogThreeTwoNextLine[6].equals("-1"))
	    	{
	    		break; // Stop if match over
	    	}

	    	final double matchOddsPercentage = getCorrectedMatchOddsPercentage(Double.parseDouble(favouriteMatchOddsNextLine[6]), Double.parseDouble(underdogMatchOddsNextLine[6]));
			matchOddsSeries.add(new Second(new Date(Long.parseLong(favouriteMatchOddsNextLine[0]))), matchOddsPercentage);

	    	final double threeNilPercentage = 100.0 / Double.parseDouble(favouriteThreeNilNextLine[6]);
	    	final double threeOnePercentage = 100.0 / Double.parseDouble(favouriteThreeOneNextLine[6]);
			final double threeTwoPercentage = 100.0 / Double.parseDouble(favouriteThreeTwoNextLine[6]);
	    	final double setBettingPercentage = threeNilPercentage + threeOnePercentage + threeTwoPercentage;
    		setBettingSeries.add(new Second(new Date(Long.parseLong(favouriteMatchOddsNextLine[0]))), setBettingPercentage);

    		final double oddsDifference = Math.abs(matchOddsPercentage - setBettingPercentage);
    		oddsDifferenceSeries.add(new Second(new Date(Long.parseLong(favouriteMatchOddsNextLine[0]))), oddsDifference);

			new PrintStream(fout).println(favouriteMatchOddsNextLine[1] + ": " + oddsDifference);
	    }

	    dataset.addSeries(matchOddsSeries);
	    dataset.addSeries(setBettingSeries);
	    dataset.addSeries(oddsDifferenceSeries);

		return dataset;
	}

	private double getCorrectedMatchOddsPercentage(final double favouriteMatchOdds, final double underdogMatchOdds)
	{
		final double overround = (1 / favouriteMatchOdds) + (1 / underdogMatchOdds);
		return 100 / (favouriteMatchOdds * overround);
	}
}
