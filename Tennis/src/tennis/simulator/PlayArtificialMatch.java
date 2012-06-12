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
	private final double chance;
	private final double lambda;
	private final double decay;
	private final String file;

	public PlayArtificialMatch(final String file, final double chance, final double lambda, final double decay) throws IOException
	{
		super("Artificial Match" + " (Chance = " + chance + ", Lambda = " + lambda + ", Rho = " + decay + ")", "Point", "MWP");
		this.file = file;
		this.chance = chance;
		this.lambda = lambda;
		this.decay = decay;
	}

	@Override
	protected void buildChart() throws IOException
	{
		final JFreeChart chart = createXYLineChart(createDataset());
		final ChartPanel chartPanel = new ChartPanel(chart);
		((XYPlot) chart.getPlot()).getRangeAxis().setRange(0, 1);
	    final XYItemRenderer renderer = ((XYPlot) chart.getPlot()).getRenderer();
	    renderer.setSeriesPaint(0, Color.blue);
	    renderer.setSeriesPaint(1, Color.yellow);
	    renderer.setSeriesPaint(2, Color.red);
	    chartPanel.setPreferredSize(new Dimension(1000, 570));
	    setContentPane(chartPanel);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\" + title + ".png"), chart, 1000, 570);
	}

	@Override
	protected XYDataset createDataset() throws IOException
	{
		final XYSeries mwpNoRiskSeries = new XYSeries("No Risk MWP");
		final XYSeries mwpWRAfterFirstSetSeries = new XYSeries("MWP Payout After First Set");
		final XYSeries mwpWRAfterOneBallSeries = new XYSeries("MWP Payout After One Ball");
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
		final PlayArtificialMatch chart = new PlayArtificialMatch("doc\\realism.csv", 0.000115, 10.0, 0.95);
//		final PlayArtificialMatch chart = new PlayArtificialMatch("doc\\firstset.csv", 0.000115, 10.0, 0.95);
//		final PlayArtificialMatch chart = new PlayArtificialMatch("doc\\retirement.csv", 0.000115, 10.0, 0.95);
	    chart.buildChart();
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);
	}
}
