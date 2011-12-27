package tennis.omalley.charts;

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
	private final String playerOneFile;
	private final String playerTwoFile;

	public LpmChart(final String title, final String playerOneFile, final String playerTwoFile) throws IOException
	{
		super(title);
		this.title = title;
		this.playerOneFile = playerOneFile;
		this.playerTwoFile = playerTwoFile;

		final ChartPanel chartPanel = new ChartPanel(createTimeSeriesChart());
	    chartPanel.setPreferredSize(new Dimension(500, 270));
	    setContentPane(chartPanel);
	}

	protected XYDataset createDataset() throws IOException
	{
		final TimeSeriesCollection dataset = new TimeSeriesCollection();
	    dataset.addSeries(createSeries(playerOneFile));
	    dataset.addSeries(createSeries(playerTwoFile));
	    return dataset;
	}

	private TimeSeries createSeries(final String playerFile) throws FileNotFoundException, IOException
	{
		final TimeSeries playerOneLpm = new TimeSeries("LPM");
		final CSVReader reader = new CSVReader(new FileReader(playerFile));
	    reader.readNext(); reader.readNext(); reader.readNext(); // header

	    String [] nextLine;
	    while ((nextLine = reader.readNext()) != null && Double.parseDouble(nextLine[10]) != -1)
	    {
    		playerOneLpm.add(new Second(new Date(Long.parseLong(nextLine[0]))), Double.parseDouble(nextLine[10]));
	    }

		return playerOneLpm;
	}

	private JFreeChart createTimeSeriesChart() throws IOException
	{
	    final JFreeChart chart = ChartFactory.createTimeSeriesChart(
	    	title,
	    	"Time",
	    	"LPM",
	        createDataset(),
	        false,                    			 // legend
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
	    final LpmChart chart = new LpmChart("Sharapova vs Azarenka", "doc\\sharapova.csv", "doc\\azarenka.csv");
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
