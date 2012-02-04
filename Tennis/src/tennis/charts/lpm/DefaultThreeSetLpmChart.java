package tennis.charts.lpm;

import static java.lang.Double.parseDouble;

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

public class DefaultThreeSetLpmChart extends LpmChart
{
	private final PlayerOdds favourite;
	private final PlayerOdds underdog;

	public DefaultThreeSetLpmChart(final PlayerOdds favourite, final PlayerOdds underdog) throws IOException
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
		final CSVReader favourtieTwoNilReader = new CSVReader(new FileReader(favourite.getTwoNil()));
		final CSVReader favouriteTwoOneReader = new CSVReader(new FileReader(favourite.getTwoOne()));
		final CSVReader underdogTwoNilReader = new CSVReader(new FileReader(underdog.getTwoNil()));
		final CSVReader underdogTwoOneReader = new CSVReader(new FileReader(underdog.getTwoOne()));

		final TimeSeries oddsDifferenceSeries = new TimeSeries("Odds Difference");

		final FileOutputStream fout = new FileOutputStream ("doc\\adam.txt");

	    String [] favouriteMatchOddsNextLine;
	    String [] underdogMatchOddsNextLine;
	    while ((favouriteMatchOddsNextLine = favouriteMatchOddsReader.readNext()) != null && (underdogMatchOddsNextLine = underdogMatchOddsReader.readNext()) != null)
	    {
	    	final String [] favouriteTwoNilNextLine = favourtieTwoNilReader.readNext();
	    	final String [] favouriteTwoOneNextLine = favouriteTwoOneReader.readNext();
	    	final String [] underdogTwoNilNextLine = underdogTwoNilReader.readNext();
	    	final String [] underdogTwoOneNextLine = underdogTwoOneReader.readNext();

	    	if(favouriteMatchOddsNextLine[6].equals("-1") || favouriteTwoNilNextLine[6].equals("-1") || favouriteTwoOneNextLine[6].equals("-1"))
	    	{
	    		break; // Stop if match over
	    	}

	    	final double matchOddsPercentage = getCorrectedMatchOddsPercentage(parseDouble(favouriteMatchOddsNextLine[6]), parseDouble(underdogMatchOddsNextLine[6]));
			matchOddsSeries.add(new Second(new Date(Long.parseLong(favouriteMatchOddsNextLine[0]))), matchOddsPercentage);

	    	final double setBettingPercentage = getCorrectedSetBettingPercentage(parseDouble(favouriteTwoNilNextLine[6]), parseDouble(favouriteTwoOneNextLine[6]),
	    																		 parseDouble(underdogTwoNilNextLine[6]), parseDouble(underdogTwoOneNextLine[6]));
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

	private double getCorrectedSetBettingPercentage(final double favouriteTwoNilOdds, final double favouriteTwoOneOdds, final double underdogTwoNilOdds, final double underdogTwoOneOdds)
	{
		final double overround = (1 / favouriteTwoNilOdds) + (1 / favouriteTwoOneOdds) + (1 / underdogTwoNilOdds) + (1 / underdogTwoOneOdds);
		return 100 / ((favouriteTwoNilOdds * overround) + (favouriteTwoOneOdds * overround));
	}

	private double getCorrectedMatchOddsPercentage(final double favouriteMatchOdds, final double underdogMatchOdds)
	{
		final double overround = (1 / favouriteMatchOdds) + (1 / underdogMatchOdds);
		return 100 / (favouriteMatchOdds * overround);
	}
}
