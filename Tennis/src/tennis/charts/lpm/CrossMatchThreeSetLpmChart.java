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

public class CrossMatchThreeSetLpmChart extends LpmChart
{
	private final PlayerOdds favourite;
	private final PlayerOdds underdog;

	public CrossMatchThreeSetLpmChart(final String targetPlayer, final PlayerOdds favourite, final PlayerOdds underdog) throws IOException
	{
		super(underdog.getTitle() + " (" + underdog.getSurname() + ")");
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

		final TimeSeries setBettingSeries = new TimeSeries("Set Betting");
		final CSVReader favouriteTwoNilReader = new CSVReader(new FileReader(favourite.getTwoNil()));
		final CSVReader favouriteTwoOneReader = new CSVReader(new FileReader(favourite.getTwoOne()));
		final CSVReader underdogTwoNilReader = new CSVReader(new FileReader(underdog.getTwoNil()));
		final CSVReader underdogTwoOneReader = new CSVReader(new FileReader(underdog.getTwoOne()));

		final TimeSeries oddsDifferenceSeries = new TimeSeries("Odds Difference");

		final FileOutputStream fout = new FileOutputStream ("doc\\adam.txt");

	    String [] matchOddsNextLine;
	    while ((matchOddsNextLine = favouriteMatchOddsReader.readNext()) != null)
	    {
	    	final String[] favouriteTwoNilNextLine = favouriteTwoNilReader.readNext();
	    	final String[] favouriteTwoOneNextLine = favouriteTwoOneReader.readNext();
	    	final String[] underdogTwoNilNextLine = underdogTwoNilReader.readNext();
	    	final String[] underdogTwoOneNextLine = underdogTwoOneReader.readNext();

	    	if(matchOddsNextLine[6].equals("-1") || favouriteTwoNilNextLine[6].equals("-1") || favouriteTwoOneNextLine[6].equals("-1")
	    	   || underdogTwoNilNextLine[6].equals("-1") || underdogTwoOneNextLine[6].equals("-1"))
	    	{
	    		break; // Stop if match over
	    	}

	    	final double crossMatchedMatchOddsPercentage = crossMatchMatchOddsPercentage(Double.parseDouble(matchOddsNextLine[6]));
			matchOddsSeries.add(new Second(new Date(Long.parseLong(matchOddsNextLine[0]))), crossMatchedMatchOddsPercentage);

			final double crossMatchedSetBettingPercentage = crossMatchSetBettingPercentage(Double.parseDouble(favouriteTwoNilNextLine[6]),
																					 	   Double.parseDouble(favouriteTwoOneNextLine[6]),
																					 	   Double.parseDouble(underdogTwoNilNextLine[6]),
																					 	   Double.parseDouble(underdogTwoOneNextLine[6]));
    		setBettingSeries.add(new Second(new Date(Long.parseLong(matchOddsNextLine[0]))), crossMatchedSetBettingPercentage);

    		final double oddsDifference = Math.abs(crossMatchedMatchOddsPercentage - crossMatchedSetBettingPercentage);
    		oddsDifferenceSeries.add(new Second(new Date(Long.parseLong(matchOddsNextLine[0]))), oddsDifference);

			new PrintStream(fout).println(matchOddsNextLine[1] + ": " + oddsDifference);
	    }

	    dataset.addSeries(matchOddsSeries);
	    dataset.addSeries(setBettingSeries);
	    dataset.addSeries(oddsDifferenceSeries);

		return dataset;
	}

	private double crossMatchSetBettingPercentage(final double favouriteTwoNil, final double favouriteTwoOne, final double underdogTwoNil, final double underdogTwoOne)
	{
		double favouriteSum = (1 / favouriteTwoNil) + (1 / favouriteTwoOne);
		double sum = favouriteSum + (1 / underdogTwoOne);
		final double underdogTwoNilPercentage = 100 / (1 / (1 - sum));

		sum = favouriteSum + (1 / underdogTwoNil);
		final double underdogTwoOnePercentage = 100 / (1 / (1 - sum));

		return underdogTwoNilPercentage + underdogTwoOnePercentage;
	}

	private double crossMatchMatchOddsPercentage(final double matchOdds)
	{
		return 100.0 / (1.0 / (1.0 - (1.0 / matchOdds)));
	}
}
