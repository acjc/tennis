package tennis.read;

import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import tennis.graphs.helper.MatchOdds;
import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.helper.SetOdds;
import tennis.graphs.odds.OddsChart;
import tennis.neldermead.PbSearch;
import tennis.neldermead.PointLevelRetirementRiskSearch;
import tennis.omalley.CurrentGameScore;
import tennis.omalley.CurrentMatchScore;
import tennis.omalley.CurrentSetScore;
import tennis.omalley.OMalley;
import tennis.simulator.GameState;
import tennis.simulator.MatchState;
import tennis.simulator.RetirementRisk;
import tennis.simulator.SetState;
import tennis.simulator.SimulationOutcomes;
import tennis.simulator.SimulatorWRHyperExp;
import au.com.bytecode.opencsv.CSVReader;

public class ReadMatch extends OddsChart
{
	private final double pa = 0.645;
	private double pb;
	private final int points;
	private final String match;
	private final double chance = 0.000115;
	private final double lambda = 10.0;
	private final double decay = 0.95;

	public ReadMatch(final String title, final double pb, final int points, final String match, final PlayerOdds favourite, final PlayerOdds underdog) throws IOException
	{
		super(title, favourite, underdog);
		this.pb = pb;
		this.points = points;
		this.match = match;
	}

	@Override
	protected JFreeChart createChart() throws IOException
	{
		final JFreeChart chart = ChartFactory.createXYLineChart(
				title,
		        "Point",
		        "Implied Probability",
		        createDataset(),
		        PlotOrientation.VERTICAL,
		        true,                    			 // legend
		        true,                     			 // tooltips
		        false                     			// urls
		    );

	    chart.setBackgroundPaint(Color.white);

	    final XYPlot plot = chart.getXYPlot();
	    plot.setBackgroundPaint(Color.white);
	    plot.setDomainGridlinePaint(Color.lightGray);
	    plot.setRangeGridlinePaint(Color.lightGray);
	    plot.getRangeAxis().setRange(-0.2, 1.0);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\matches\\" + title + ".png"), chart, 1000, 570);

	    return chart;
	}

	@Override
	protected XYDataset createDataset() throws IOException
	{
		System.out.println("Processing odds data...");
		final CSVReader favouriteMatchOddsReader = favourite.getMatchOdds();
		final List<CSVReader> favouriteSetOddsReaders = new ArrayList<CSVReader>();
		final List<CSVReader> underdogSetOddsReaders = new ArrayList<CSVReader>();
		favouriteSetOddsReaders.addAll(favourite.getSetOdds());
		underdogSetOddsReaders.addAll(underdog.getSetOdds());

		final List<MatchOdds> favouriteMatchOdds = parseMatchOdds(favouriteMatchOddsReader);
		final List<List<SetOdds>> favouriteSetOdds = parseSetOdds(favouriteSetOddsReaders);
		final List<List<SetOdds>> underdogSetOdds = parseSetOdds(underdogSetOddsReaders);

		final List<Double> retirementRisks = new ArrayList<Double>();
		final List<Double> setOddsProbabilities = new ArrayList<Double>();
		for (int i = 0; i < favouriteMatchOdds.size(); i++)
		{
			final long time = favouriteMatchOdds.get(i).getTime();
			final double matchOddsProbability = favouriteMatchOdds.get(i).getOddsProbability();
			final double setOddsProbability = calculateCorrectedSetOddsProbability(favouriteSetOdds, underdogSetOdds, time);

			setOddsProbabilities.add(setOddsProbability);
			retirementRisks.add(setOddsProbability - matchOddsProbability);
		}

		final List<Double> simpleRetirementRisks = new ArrayList<Double>();
		final List<Double> simplesetOddsProbabilities = new ArrayList<Double>();
		final int jump = favouriteMatchOdds.size() / points;
		for (int i = 0; i < points; i++)
		{
			simplesetOddsProbabilities.add(setOddsProbabilities.get(i * jump));
			simpleRetirementRisks.add(retirementRisks.get(i * jump) >= 0 ? retirementRisks.get(i * jump) : 0);
		}

		System.out.println("Processing match...");
		final XYSeries mwpNoRiskSeries = new XYSeries("No Risk MWP");
		final XYSeries mwpNormalWinSeries = new XYSeries("Normal Win MWP");
		final XYSeries mwpWRAfterFirstSetSeries = new XYSeries("MWP Payout After First Set");
		final XYSeries mwpWRAfterOneBallSeries = new XYSeries("MWP Payout After One Ball");
		final CSVReader reader = new CSVReader(new FileReader(match));
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

	    	pb = new PbSearch(setOddsProbabilities.get(index)).search();

	    	final double mwp = OMalley.matchInProgress(pa, pb, new CurrentMatchScore(targetSets, opponentSets), new CurrentSetScore(targetGames, opponentGames), new CurrentGameScore(targetPoints, opponentPoints), servingNext, 3);

	    	final double ra = new PointLevelRetirementRiskSearch(pa, pb, simpleRetirementRisks.get(index), 0, targetSets, opponentSets, targetGames, opponentGames, targetPoints, opponentPoints, servingNext).search();

	    	final MatchState initialState = new MatchState(targetSets, opponentSets, new SetState(targetGames, opponentGames), new GameState(targetPoints, opponentPoints, true), 3);
			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, new RetirementRisk(ra, 0), initialState, true, 10000);

			final double targetMwpNormalWin = outcomes.proportionTargetWon();
			final double opponentMwpNormalWin = outcomes.proportionOpponentWon();
			final double targetMwpRiskAfterFirstSet = (targetMwpNormalWin + outcomes.proportionOpponentRetirementsAfterFirstSet()) / (targetMwpNormalWin + opponentMwpNormalWin + outcomes.proportionTargetRetirementsAfterFirstSet() + outcomes.proportionOpponentRetirementsAfterFirstSet());
			final double targetMwpRiskAfterOneBall = (targetMwpNormalWin + outcomes.proportionOpponentRetirements()) / (targetMwpNormalWin + opponentMwpNormalWin + outcomes.proportionTargetRetirements() + outcomes.proportionOpponentRetirements());

			System.out.println(index + ": (" + targetSets + ", " + opponentSets + "), " + "(" + targetGames + ", " + opponentGames + "), " + "(" + targetPoints + ", " + opponentPoints + ")");
			System.out.println("MWP = " + mwp);
			System.out.println("MWP Normal Win = " + targetMwpNormalWin);
			System.out.println();

			mwpNoRiskSeries.add(index, mwp);
			mwpNormalWinSeries.add(index, targetMwpNormalWin);
			mwpWRAfterFirstSetSeries.add(index, targetMwpRiskAfterFirstSet);
			mwpWRAfterOneBallSeries.add(index, targetMwpRiskAfterOneBall);

		    index++;
	    }

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(mwpNoRiskSeries);
	    dataset.addSeries(mwpNormalWinSeries);
	    dataset.addSeries(mwpWRAfterFirstSetSeries);
	    dataset.addSeries(mwpWRAfterOneBallSeries);

	    return dataset;
	}
}
