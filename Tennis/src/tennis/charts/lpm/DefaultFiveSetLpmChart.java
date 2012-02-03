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

public class DefaultFiveSetLpmChart extends FiveSetLpmChart
{
	public DefaultFiveSetLpmChart(final String targetPlayer, final PlayerOdds playerOdds) throws IOException
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
		final CSVReader matchOddsReader = new CSVReader(new FileReader(player.getMatchOdds()));

		final TimeSeries setBettingSeries = new TimeSeries("Set Betting");
		final CSVReader threeNilReader = new CSVReader(new FileReader(player.getThreeNil()));
		final CSVReader threeOneReader = new CSVReader(new FileReader(player.getThreeOne()));
		final CSVReader threeTwoReader = new CSVReader(new FileReader(player.getThreeTwo()));

		final TimeSeries oddsDifferenceSeries = new TimeSeries("Odds Difference");

		final FileOutputStream fout = new FileOutputStream ("doc\\adam.txt");

	    String [] matchOddsNextLine;
	    while ((matchOddsNextLine = matchOddsReader.readNext()) != null)
	    {
	    	final String[] threeNilNextLine = threeNilReader.readNext();
	    	final String[] threeOneNextLine = threeOneReader.readNext();
	    	final String[] threeTwoNextLine = threeTwoReader.readNext();
	    	// Check match hasn't finished yet
	    	if(matchOddsNextLine[6].equals("-1") || threeNilNextLine[6].equals("-1") || threeOneNextLine[6].equals("-1") || threeTwoNextLine[6].equals("-1"))
	    	{
	    		break;
	    	}

	    	final double matchOddsPercentage = 100.0 / Double.parseDouble(matchOddsNextLine[6]);
			matchOddsSeries.add(new Second(new Date(Long.parseLong(matchOddsNextLine[0]))), matchOddsPercentage);

	    	final double threeNilPercentage = 100.0 / Double.parseDouble(threeNilNextLine[6]);
	    	final double threeOnePercentage = 100.0 / Double.parseDouble(threeOneNextLine[6]);
			final double threeTwoPercentage = 100.0 / Double.parseDouble(threeTwoNextLine[6]);
	    	final double setBettingPercentage = threeNilPercentage + threeOnePercentage + threeTwoPercentage;
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
