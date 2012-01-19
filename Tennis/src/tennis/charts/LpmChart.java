package tennis.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

public class LpmChart extends ApplicationFrame
{
	private final String title;
	private final String matchOdds;
	private final String threeNil;
	private final String threeOne;
	private final String threeTwo;

	public LpmChart(final String title, final String matchOdds, final String threeNil, final String threeOne, final String threeTwo) throws IOException
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

	protected XYDataset createDataset() throws IOException
	{
		final TimeSeriesCollection dataset = new TimeSeriesCollection();
	    dataset.addSeries(createSeries(matchOdds, "Match Odds"));
	    dataset.addSeries(createSeries(threeNil, "3-0"));
	    dataset.addSeries(createSeries(threeOne, "3-1"));
	    dataset.addSeries(createSeries(threeTwo, "3-2"));
	    return dataset;
	}

	private TimeSeries createSeries(final String file, final String name) throws FileNotFoundException, IOException
	{
		final TimeSeries series = new TimeSeries(name);
		final CSVReader reader = new CSVReader(new FileReader(file));
	    reader.readNext(); reader.readNext(); reader.readNext(); // header

	    String [] nextLine;
	    while ((nextLine = reader.readNext()) != null && Double.parseDouble(nextLine[10]) != -1)
	    {
    		series.add(new Second(new Date(Long.parseLong(nextLine[0]))), Double.parseDouble(nextLine[10]));
	    }

		return series;
	}

	private JFreeChart createTimeSeriesChart() throws IOException
	{
	    final JFreeChart chart = ChartFactory.createTimeSeriesChart(
	    	title,
	    	"Time",
	    	"LPM",
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
	    plot.getRangeAxis().setRange(0, 8);

	    return chart;
	}

	public static void main(final String[] args) throws IOException
	{
	    final LpmChart chart = new LpmChart("Murray vs Berrer (French Open 2011 Third Round)",
	    									"doc\\murrayberrer\\Andy Murray.csv", "doc\\murrayberrer\\A Murray 3 - 0.csv",
	    																		  "doc\\murrayberrer\\A Murray 3 - 1.csv",
	    																		  "doc\\murrayberrer\\A Murray 3 - 2.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
