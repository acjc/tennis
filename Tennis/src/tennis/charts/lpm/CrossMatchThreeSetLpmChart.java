package tennis.charts.lpm;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import tennis.charts.helper.PlayerOdds;
import au.com.bytecode.opencsv.CSVReader;

public class CrossMatchThreeSetLpmChart extends ThreeSetLpmChart
{
	public CrossMatchThreeSetLpmChart(final String targetPlayer, final PlayerOdds playerOdds) throws IOException
	{
		super(targetPlayer, playerOdds);

		final ChartPanel chartPanel = new ChartPanel(createTimeSeriesChart());
	    chartPanel.setPreferredSize(new Dimension(1000, 570));
	    setContentPane(chartPanel);
	}

	@Override
	protected XYDataset createDataset() throws FileNotFoundException, IOException
	{
		final TimeSeriesCollection dataset = new TimeSeriesCollection();

		final TimeSeries matchOddsSeries = new TimeSeries("Match Odds");
		final CSVReader favouriteMatchOddsReader = new CSVReader(new FileReader(player.getMatchOdds()));

		final TimeSeries setBettingSeries = new TimeSeries("Set Betting");
		final CSVReader twoNilReader = new CSVReader(new FileReader(player.getTwoNil()));
		final CSVReader twoOneReader = new CSVReader(new FileReader(player.getTwoOne()));

		final TimeSeries oddsDifferenceSeries = new TimeSeries("Odds Difference");

		final FileOutputStream fout = new FileOutputStream ("doc\\adam.txt");

	    String [] matchOddsNextLine;
	    while ((matchOddsNextLine = favouriteMatchOddsReader.readNext()) != null)
	    {
	    	final String[] twoNilNextLine = twoNilReader.readNext();
	    	final String[] twoOneNextLine = twoOneReader.readNext();
	    	// Check match hasn't finished yet
	    	if(matchOddsNextLine[6].equals("-1") || twoNilNextLine[6].equals("-1") || twoOneNextLine[6].equals("-1"))
	    	{
	    		break;
	    	}

	    	final double crossMatchedOdds = 1.0 / (1.0 - (1.0 / Double.parseDouble(matchOddsNextLine[6])));
			final double matchOddsPercentage = 100.0 / crossMatchedOdds;
			matchOddsSeries.add(new Second(new Date(Long.parseLong(matchOddsNextLine[0]))), matchOddsPercentage);

			final double twoNilPercentage = 100.0 / Double.parseDouble(twoNilNextLine[6]);
			final double twoOnePercentage = 100.0 / Double.parseDouble(twoOneNextLine[6]);
	    	final double setBettingPercentage = twoNilPercentage + twoOnePercentage;
    		setBettingSeries.add(new Second(new Date(Long.parseLong(matchOddsNextLine[0]))), setBettingPercentage);

    		final double oddsDifference = Math.abs(matchOddsPercentage - setBettingPercentage);
    		oddsDifferenceSeries.add(new Second(new Date(Long.parseLong(matchOddsNextLine[0]))), oddsDifference);

			new PrintStream(fout).println(matchOddsNextLine[1] + ": " + oddsDifference);
	    }

	    dataset.addSeries(matchOddsSeries);
	    dataset.addSeries(setBettingSeries);
	    dataset.addSeries(oddsDifferenceSeries);

		return dataset;
	}

	private JFreeChart createTimeSeriesChart() throws IOException
	{
	    final JFreeChart chart = ChartFactory.createTimeSeriesChart(
	    	title,
	    	"Time",
	    	"Implied Probability",
	        createDataset(),
	        true,                    			 // legend
	        true,                     			 // tooltips
	        false                     			// urls
	    );

	    chart.setBackgroundPaint(Color.white);

	    final XYPlot plot = chart.getXYPlot();
	    plot.setBackgroundPaint(Color.lightGray);
	    plot.setDomainGridlinePaint(Color.white);
	    plot.setRangeGridlinePaint(Color.white);
	    plot.getRangeAxis().setRange(0.0, 125.0);

	    ChartUtilities.saveChartAsPNG(new File("doc\\" + title + ".png"), chart, 1000, 570);

	    return chart;
	}
}