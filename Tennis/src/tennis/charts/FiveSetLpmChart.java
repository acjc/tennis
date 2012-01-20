package tennis.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import au.com.bytecode.opencsv.CSVReader;

public class FiveSetLpmChart extends ApplicationFrame
{
	private final String title;
	private final String matchOdds;
	private final String threeNil;
	private final String threeOne;
	private final String threeTwo;

	public FiveSetLpmChart(final String title, final String matchOdds, final String threeNil, final String threeOne, final String threeTwo) throws IOException
	{
		super(title);
		this.title = title;
		this.matchOdds = matchOdds;
		this.threeNil = threeNil;
		this.threeOne = threeOne;
		this.threeTwo = threeTwo;

		final ChartPanel chartPanel = new ChartPanel(createTimeSeriesChart());
	    chartPanel.setPreferredSize(new Dimension(1000, 520));
	    setContentPane(chartPanel);
	}

	private XYDataset createDataset() throws FileNotFoundException, IOException
	{
		final TimeSeriesCollection dataset = new TimeSeriesCollection();

		final TimeSeries matchOddsSeries = new TimeSeries("Match Odds");
		final CSVReader matchOddsReader = new CSVReader(new FileReader(matchOdds));

		final TimeSeries setBettingSeries = new TimeSeries("Set Betting");
		final CSVReader threeNilReader = new CSVReader(new FileReader(threeNil));
		final CSVReader threeOneReader = new CSVReader(new FileReader(threeOne));
		final CSVReader threeTwoReader = new CSVReader(new FileReader(threeTwo));

		final TimeSeries oddsDifferenceSeries = new TimeSeries("Odds Difference");

		final FileOutputStream fout = new FileOutputStream ("doc\\adam.txt");

	    String [] nextLine;
	    // Check match hasn't finished yet
	    while ((nextLine = matchOddsReader.readNext()) != null && Double.parseDouble(nextLine[6]) != -1)
	    {
	    	final double matchOddsPercentage = 100.0 / Double.parseDouble(nextLine[6]);
			matchOddsSeries.add(new Second(new Date(Long.parseLong(nextLine[0]))), matchOddsPercentage);

	    	final double threeNilPercentage = 100.0 / Double.parseDouble(threeNilReader.readNext()[6]);
	    	final double threeOnePercentage = 100.0 / Double.parseDouble(threeOneReader.readNext()[6]);
	    	final double threeTwoPercentage = 100.0 / Double.parseDouble(threeTwoReader.readNext()[6]);
	    	final double setBettingPercentage = threeNilPercentage + threeOnePercentage + threeTwoPercentage;
    		setBettingSeries.add(new Second(new Date(Long.parseLong(nextLine[0]))), setBettingPercentage);

    		final double oddsDifference = Math.abs(matchOddsPercentage - setBettingPercentage);
    		oddsDifferenceSeries.add(new Second(new Date(Long.parseLong(nextLine[0]))), oddsDifference);

			new PrintStream(fout).println(nextLine[1] + ": " + oddsDifference);
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

	    return chart;
	}

	public static void main(final String[] args) throws IOException
	{
	    final FiveSetLpmChart chart = new FiveSetLpmChart("Murray vs Berrer (French Open 2011 Third Round)",
	    									"doc\\murray_berrer\\Andy Murray.csv", "doc\\murray_berrer\\A Murray 3 - 0.csv",
	    																		   "doc\\murray_berrer\\A Murray 3 - 1.csv",
	    																		   "doc\\murray_berrer\\A Murray 3 - 2.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
