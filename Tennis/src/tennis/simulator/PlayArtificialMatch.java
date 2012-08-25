package tennis.simulator;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import tennis.graphs.XYLineChart;
import tennis.omalley.CurrentGameScore;
import tennis.omalley.CurrentMatchScore;
import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalley;
import tennis.read.RiskChart;
import au.com.bytecode.opencsv.CSVReader;

public class PlayArtificialMatch extends XYLineChart
{
	private final double pa = 0.6;
	private final double pb = 0.6;
	private final double chance = 0.000115;
	private final double lambda = 10.0;
	private final double decay = 0.95;
	private final String file;
	private final String savename;
	private final List<Double> pointLevelRisks = new ArrayList<Double>();
	private final List<Double> risks = new ArrayList<Double>();

	public PlayArtificialMatch(final String file, final String savename) throws IOException
	{
		super("Evolution of modelled Match Odds markets for an artificial match", "Point", "Match-winning Probability");
		this.file = file;
		this.savename = savename;
	}

	@Override
	public void buildChart() throws IOException
	{
		final JFreeChart chart = createChart();
		final ChartPanel chartPanel = new ChartPanel(chart);
	    final XYItemRenderer renderer = ((XYPlot) chart.getPlot()).getRenderer();
	    renderer.setSeriesPaint(0, Color.BLUE);
	    renderer.setSeriesPaint(1, Color.RED);
	    renderer.setSeriesPaint(2, new Color(205, 102, 0));
	    chartPanel.setPreferredSize(new Dimension(1200, 670));
	    setContentPane(chartPanel);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\" + savename + ".png"), chart, 1200, 670);
	}

	@Override
	protected XYDataset createDataset() throws IOException
	{
		final XYSeries mwpNoRiskSeries = new XYSeries("No Risk Market");
		final XYSeries mwpWRAfterFirstSetSeries = new XYSeries("Payout After First Set Market");
		final XYSeries mwpWRAfterOneBallSeries = new XYSeries("Payout After One Ball Market");
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

	    	final MatchState initialState = new MatchState(targetSets, opponentSets, new SetState(targetGames, opponentGames), new GameState(targetPoints, opponentPoints, servingNext), 3);
			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, new RetirementRisk(ra, rb), initialState, true, 10000);

			final double targetMwpNormalWin = outcomes.proportionTargetWon();
			final double opponentMwpNormalWin = outcomes.proportionOpponentWon();
			final double targetMwpAfterFirstSet = (targetMwpNormalWin + outcomes.proportionOpponentRetirementsAfterFirstSet()) / (targetMwpNormalWin + opponentMwpNormalWin + outcomes.proportionTargetRetirementsAfterFirstSet() + outcomes.proportionOpponentRetirementsAfterFirstSet());
			final double targetMwpAfterOneBall = (targetMwpNormalWin + outcomes.proportionOpponentRetirements()) / (targetMwpNormalWin + opponentMwpNormalWin + outcomes.proportionTargetRetirements() + outcomes.proportionOpponentRetirements());
			System.out.println("ra = " + ra + ", rb = " + rb);
			System.out.println(index + ": (" + targetSets + ", " + opponentSets + "), " + "(" + targetGames + ", " + opponentGames + "), " + "(" + targetPoints + ", " + opponentPoints + ")");
			System.out.println("MWP = " + mwp);
			outcomes.minPrint("A", "B");
			System.out.println();

			pointLevelRisks.add(index, ra);
			risks.add(index, outcomes.proportionTargetRetirements());

			mwpNoRiskSeries.add(index, mwp);
			mwpWRAfterFirstSetSeries.add(index, targetMwpAfterFirstSet);
			mwpWRAfterOneBallSeries.add(index, targetMwpAfterOneBall);
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
		final PlayArtificialMatch retirement = new PlayArtificialMatch("doc\\retirement.csv", "artificialretirement");
		final PlayArtificialMatch firstSet = new PlayArtificialMatch("doc\\firstset.csv", "artificialfirstset");
		final PlayArtificialMatch win = new PlayArtificialMatch("doc\\realism.csv", "artificialwin");
		retirement.buildChart();
		firstSet.buildChart();
		win.buildChart();

	    final RiskChart retirementRisks = new RiskChart("artificialretirementrisk", retirement.pointLevelRisks, retirement.risks);
	    retirementRisks.buildChart();
	    final RiskChart firstSetRisks = new RiskChart("artificialfirstsetrisk", firstSet.pointLevelRisks, firstSet.risks);
	    firstSetRisks.buildChart();
	    final RiskChart winRisks = new RiskChart("artificialwinrisk", win.pointLevelRisks, win.risks);
	    retirementRisks.buildChart();
	    firstSetRisks.buildChart();
	    winRisks.buildChart();
	}
}
