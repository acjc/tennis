package tennis.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
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
	private final double pa = 0.6;
	private final double pb = 0.6;
	private final double chance = 0.000115;
	private final double lambda = 10.0;
	private final double decay = 0.95;
	private final String file;

	public PlayArtificialMatch(final String file) throws IOException
	{
		super("Evolution of modelled Match Odds markets for an artificial match", "Point", "Match-winning Probability (MWP)");
		this.file = file;
	}

	@Override
	public void buildChart() throws IOException
	{
		final JFreeChart chart = createChart();
		final ChartPanel chartPanel = new ChartPanel(chart);
		((XYPlot) chart.getPlot()).getRangeAxis().setRange(0, 1);
	    final XYItemRenderer renderer = ((XYPlot) chart.getPlot()).getRenderer();
	    renderer.setSeriesPaint(0, Color.BLUE);
	    renderer.setSeriesPaint(1, Color.RED);
	    renderer.setSeriesPaint(2, Color.YELLOW);
	    chartPanel.setPreferredSize(new Dimension(1000, 570));
	    setContentPane(chartPanel);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\" + title + ".png"), chart, 1000, 570);
	}

	@Override
	protected XYDataset createDataset() throws IOException
	{
		final XYSeries mwpNoRiskSeries = new XYSeries("No Risk MWP");
		final XYSeries mwpWRAfterFirstSetSeries = new XYSeries("Payout After First Set MWP");
		final XYSeries mwpWRAfterOneBallSeries = new XYSeries("Payout After One Ball MWP");
		final CSVReader reader = new CSVReader(new FileReader(file));
		final SimulatorWRHyperExp simulator = new SimulatorWRHyperExp(chance, lambda, decay, true);
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
	    	final double ra = Double.parseDouble(nextLine[7]);
	    	final double rb = Double.parseDouble(nextLine[8]);

	    	final double mwp = OMalley.matchInProgress(pa, pb, new CurrentMatchScore(targetSets, opponentSets), new CurrentSetScore(targetGames, opponentGames), new CurrentGameScore(targetPoints, opponentPoints), servingNext, 3);

	    	final MatchState initialState = new MatchState(targetSets, opponentSets, new SetState(targetGames, opponentGames), new GameState(targetPoints, opponentPoints, true), 3);
			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, new RetirementRisk(ra, rb), initialState, true, 10000);

			final double targetMwpNormalWin = outcomes.proportionTargetWon();
			final double opponentMwpNormalWin = outcomes.proportionOpponentWon();
			final double targetMwpRiskAfterFirstSet = (targetMwpNormalWin + outcomes.proportionOpponentRetirementsAfterFirstSet()) / (targetMwpNormalWin + opponentMwpNormalWin + outcomes.proportionTargetRetirementsAfterFirstSet() + outcomes.proportionOpponentRetirementsAfterFirstSet());
			final double targetMwpRiskAfterOneBall = (targetMwpNormalWin + outcomes.proportionOpponentRetirements()) / (targetMwpNormalWin + opponentMwpNormalWin + outcomes.proportionTargetRetirements() + outcomes.proportionOpponentRetirements());
			System.out.println("ra = " + ra + ", rb = " + rb);
			System.out.println(index + ": (" + targetSets + ", " + opponentSets + "), " + "(" + targetGames + ", " + opponentGames + "), " + "(" + targetPoints + ", " + opponentPoints + ")");
			System.out.println("MWP = " + mwp);
			outcomes.minPrint("A", "B");
			System.out.println();

			mwpNoRiskSeries.add(index, mwp);
			mwpWRAfterFirstSetSeries.add(index, targetMwpRiskAfterFirstSet);
			mwpWRAfterOneBallSeries.add(index, targetMwpRiskAfterOneBall);
	    	index++;
	    }
	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(mwpNoRiskSeries);
	    dataset.addSeries(mwpWRAfterFirstSetSeries);
	    dataset.addSeries(mwpWRAfterOneBallSeries);

	    return dataset;
	}

	public static void main(final String[] args) throws IOException
	{
//		final PlayArtificialMatch chart = new PlayArtificialMatch("doc\\realism.csv");
		final PlayArtificialMatch chart = new PlayArtificialMatch("doc\\firstset.csv");
//		final PlayArtificialMatch chart = new PlayArtificialMatch("doc\\retirement.csv");
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
