package tennis.simulator;

import java.awt.Dimension;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import tennis.graphs.XYLineChart;
import tennis.omalley.CurrentGameScore;
import tennis.omalley.CurrentMatchScore;
import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalley;
import au.com.bytecode.opencsv.CSVReader;

public class PlayArtificialMatch extends XYLineChart
{
	public PlayArtificialMatch() throws IOException
	{
		super("Artificial Match", "Point", "MWP");
	}

	@Override
	protected void buildChart() throws IOException
	{
		final JFreeChart chart = createXYLineChart(createDataset());
		final ChartPanel chartPanel = new ChartPanel(chart);
		((XYPlot) chart.getPlot()).getRangeAxis().setRange(0, 1);
	    chartPanel.setPreferredSize(new Dimension(1000, 570));
	    setContentPane(chartPanel);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\" + title + ".png"), chart, 1000, 570);
	}

	@Override
	protected XYDataset createDataset() throws IOException
	{
		final XYSeries series = new XYSeries("Artificial Match");
		final XYSeries seriesWR = new XYSeries("Artificial Match WR");
		final CSVReader reader = new CSVReader(new FileReader("doc\\testmatch.csv"));
		final SimulatorWRExp simulator = new SimulatorWRExp(100000, 0.85, true);
		int index = 0;
		String [] nextLine;
	    while ((nextLine = reader.readNext()) != null)
	    {
	    	final int targetSets = Integer.parseInt(nextLine[0]);
	    	final int opponentSets = Integer.parseInt(nextLine[1]);
	    	final int targetGames = Integer.parseInt(nextLine[2]);
	    	final int opponentGames = Integer.parseInt(nextLine[3]);
	    	final int targetPoints = Integer.parseInt(nextLine[4]);
	    	final int opponentPoints = Integer.parseInt(nextLine[5]);
	    	final boolean servingNext = nextLine[6].equals("1") ? true : false;
	    	final double point = Double.parseDouble(nextLine[7]);
	    	final double ra = Double.parseDouble(nextLine[8]);
	    	final double rb = Double.parseDouble(nextLine[9]);

	    	final double mwp = OMalley.matchInProgress(0.60, 0.60, new CurrentMatchScore(targetSets, opponentSets), new CurrentSetScore(targetGames, opponentGames), new CurrentGameScore(targetPoints, opponentPoints), servingNext, 3);

	    	final MatchState initialState = new MatchState(targetSets, opponentSets, new SetState(targetGames, opponentGames), new GameState(targetPoints, opponentPoints, true), 3);
			final SimulationOutcomes outcomes = simulator.simulate(0.60, 0.60, new RetirementRisk(ra, rb), initialState, true, 10000);

			System.out.println("(" + targetSets + ", " + opponentSets + "), " + "(" + targetGames + ", " + opponentGames + "), " + "(" + targetPoints + ", " + opponentPoints + ")");
			System.out.println("MWP = " + outcomes.proportionTargetWon());

			series.add(index, mwp);
	    	seriesWR.add(index, outcomes.proportionTargetWon());
	    	index++;
	    }
	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(series);
	    dataset.addSeries(seriesWR);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
	    final PlayArtificialMatch chart = new PlayArtificialMatch();
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
