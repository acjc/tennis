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
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import tennis.graphs.helper.MatchOdds;
import tennis.graphs.helper.PlayerOdds;
import tennis.graphs.helper.SetOdds;
import tennis.graphs.odds.OddsChart;
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
	private final double avgPwp = 0.645;
	private final int points;
	private final int numSetsToWin;
	private final String match;
	private final String savename;
	private final boolean hasRetirement;
	private final double chance = 0.000115;
	private final double lambda = 10.0;
	private final double decay = 0.95;
	public final List<Double> pointLevelRisks = new ArrayList<Double>();
	public final List<Double> risks = new ArrayList<Double>();

	public ReadMatch(final int points, final int numSetsToWin, final String match, final String savename, final PlayerOdds favourite, final PlayerOdds underdog, final boolean hasRetirement) throws IOException
	{
		super("Evolution of modelled Match Odds markets for " + favourite.getName() + " (" + favourite.getSurname() + " vs. " + underdog.getSurname() + ")", favourite, underdog);
		this.points = points;
		this.numSetsToWin = numSetsToWin;
		this.match = match;
		this.savename = savename;
		this.hasRetirement = hasRetirement;
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
	    plot.getRangeAxis().setRange(0.0, 1.0);

	    final XYItemRenderer renderer = plot.getRenderer();
	    renderer.setSeriesPaint(0, Color.BLUE);
	    renderer.setSeriesPaint(1, Color.RED);
	    renderer.setSeriesPaint(2, Color.ORANGE);

	    ChartUtilities.saveChartAsPNG(new File("graphs\\matches\\" + savename + ".png"), chart, 1000, 570);

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
		final int oddsSize = favouriteMatchOdds.size();
		for (int i = 0; i < oddsSize; i++)
		{
			final long time = favouriteMatchOdds.get(i).getTime();
			final double matchOddsProbability = favouriteMatchOdds.get(i).getOddsProbability();
			final double setOddsProbability = calculateCorrectedSetOddsProbability(favouriteSetOdds, underdogSetOdds, time);

			setOddsProbabilities.add(setOddsProbability);
			retirementRisks.add(setOddsProbability - matchOddsProbability);
		}

		final List<Double> simpleRetirementRisks = new ArrayList<Double>();
		final List<Double> simpleSetOddsProbabilities = new ArrayList<Double>();
		final int jump = oddsSize / points;
		for (int i = 0; i < points; i++)
		{
			simpleSetOddsProbabilities.add(setOddsProbabilities.get(i * jump));
			simpleRetirementRisks.add(retirementRisks.get(i * jump) >= 0 ? retirementRisks.get(i * jump) : 0);
		}

		simpleSetOddsProbabilities.add(setOddsProbabilities.get(oddsSize - 1));
		simpleRetirementRisks.add(retirementRisks.get(oddsSize - 1) >= 0 ? retirementRisks.get(oddsSize - 1) : 0);

		System.out.println("Processing match...\n");
		final XYSeries mwpNoRiskSeries = new XYSeries("No Risk Market");
		final XYSeries mwpWRAfterFirstSetSeries = new XYSeries("Payout After First Set Market");
		final XYSeries mwpWRAfterOneBallSeries = new XYSeries("Payout After One Ball Market");

		final CSVReader reader = new CSVReader(new FileReader(match));
		final SimulatorWRHyperExp simulator = new SimulatorWRHyperExp(chance, lambda, decay, true);

	    int targetSets = 0, opponentSets = 0, targetGames = 0, opponentGames = 0, targetPoints = 0, opponentPoints = 0;
	    boolean servingNext = true;
	    int index = 0;
	    String [] nextLine;
		while ((nextLine = reader.readNext()) != null)
	    {
	    	targetSets = Integer.parseInt(nextLine[0]);
	    	opponentSets = Integer.parseInt(nextLine[1]);
	    	targetGames = Integer.parseInt(nextLine[2]);
	    	opponentGames = Integer.parseInt(nextLine[3]);
	    	targetPoints = Integer.parseInt(nextLine[4]);
	    	opponentPoints = Integer.parseInt(nextLine[5]);
	    	servingNext = nextLine[6].equals("1") ? true : false;

	    	System.out.println(index + ":");
	    	final double oddsMwp = simpleSetOddsProbabilities.get(index) <= 1.0 ? simpleSetOddsProbabilities.get(index) : 1.0;
	    	System.out.println("Odds MWP = " + oddsMwp);
	    	final double pwpGap = new PbGapSearch(oddsMwp, targetSets, opponentSets, targetGames, opponentGames, targetPoints, opponentPoints, servingNext, numSetsToWin).search();

    		final double pa = avgPwp + (pwpGap / 2.0);
    		final double pb = avgPwp - (pwpGap / 2.0);
	    	System.out.println("PA = " + pa + ", PB = " + pb);
	    	System.out.println("Gap = " + simpleRetirementRisks.get(index));

	    	double ra = new PointLevelRetirementRiskBinarySearch(pa, pb, oddsMwp, simpleRetirementRisks.get(index), 0, targetSets, opponentSets, targetGames, opponentGames, targetPoints, opponentPoints, servingNext, numSetsToWin).search();
	    	final double mwp = OMalley.matchInProgress(pa, pb, new CurrentMatchScore(targetSets, opponentSets), new CurrentSetScore(targetGames, opponentGames), new CurrentGameScore(targetPoints, opponentPoints), servingNext, numSetsToWin);
	    	// Match has been won
	    	if(mwp == 1.0 && index == points - 1)
	    	{
	    		ra = 0.0;
	    	}
	    	System.out.println("ra = " + ra);
	    	pointLevelRisks.add(index, ra);

	    	final MatchState initialState = new MatchState(targetSets, opponentSets, new SetState(targetGames, opponentGames), new GameState(targetPoints, opponentPoints, servingNext), numSetsToWin);
			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, new RetirementRisk(ra, 0), initialState, true, 25000);

			risks.add(index, outcomes.proportionTargetRetirements());

			final double targetMwpNormalWin = outcomes.proportionTargetWon();
			final double opponentMwpNormalWin = outcomes.proportionOpponentWon();
			final double targetMwpAfterFirstSet = (targetMwpNormalWin + outcomes.proportionOpponentRetirementsAfterFirstSet()) / (targetMwpNormalWin + opponentMwpNormalWin + outcomes.proportionTargetRetirementsAfterFirstSet() + outcomes.proportionOpponentRetirementsAfterFirstSet());
			final double targetMwpAfterOneBall = (targetMwpNormalWin + outcomes.proportionOpponentRetirements()) / (targetMwpNormalWin + opponentMwpNormalWin + outcomes.proportionTargetRetirements() + outcomes.proportionOpponentRetirements());

			System.out.println("MWP After First Set = " + targetMwpAfterFirstSet);
			System.out.println("MWP After One Ball = " + targetMwpAfterOneBall);
			System.out.println("(" + targetSets + ", " + opponentSets + "), " + "(" + targetGames + ", " + opponentGames + "), " + "(" + targetPoints + ", " + opponentPoints + "), " + servingNext);
			outcomes.minPrint(favourite.getSurname(), underdog.getSurname());
			System.out.println();

			mwpNoRiskSeries.add(index, oddsMwp);
			mwpWRAfterFirstSetSeries.add(index, targetMwpAfterFirstSet);
			mwpWRAfterOneBallSeries.add(index, targetMwpAfterOneBall);

		    index++;
	    }

	    /******************************In case of retirement*****************************/

		if(hasRetirement)
		{
			final double oddsMwp = simpleSetOddsProbabilities.get(index);
			System.out.println("Odds MWP = " + oddsMwp);
			final double pwpGap = new PbGapSearch(oddsMwp, targetSets, opponentSets, targetGames, opponentGames, targetPoints, opponentPoints, servingNext, numSetsToWin).search();

			final double pa = avgPwp + (pwpGap / 2.0);
			final double pb = avgPwp - (pwpGap / 2.0);
			System.out.println("PA = " + pa + ", PB = " + pb);
			System.out.println("Gap = " + simpleRetirementRisks.get(index));

			double ra = new PointLevelRetirementRiskBinarySearch(pa, pb, oddsMwp, simpleRetirementRisks.get(index), 0, targetSets, opponentSets, targetGames, opponentGames, targetPoints, opponentPoints, servingNext, numSetsToWin).search();
			final double mwp = OMalley.matchInProgress(pa, pb, new CurrentMatchScore(targetSets, opponentSets), new CurrentSetScore(targetGames, opponentGames), new CurrentGameScore(targetPoints, opponentPoints), servingNext, 3);
			// Match has been won
			if(mwp == 1.0 && index == points - 1)
			{
				ra = 0.0;
			}
			System.out.println("ra = " + ra);
			pointLevelRisks.add(index, ra);

			final MatchState initialState = new MatchState(targetSets, opponentSets, new SetState(targetGames, opponentGames), new GameState(targetPoints, opponentPoints, servingNext), 3);
			final SimulationOutcomes outcomes = simulator.simulate(pa, pb, new RetirementRisk(ra, 0), initialState, true, 10000);

			risks.add(index, outcomes.proportionTargetRetirements());

			final double targetMwpNormalWin = outcomes.proportionTargetWon();
			final double opponentMwpNormalWin = outcomes.proportionOpponentWon();
			final double targetMwpAfterFirstSet = (targetMwpNormalWin + outcomes.proportionOpponentRetirementsAfterFirstSet()) / (targetMwpNormalWin + opponentMwpNormalWin + outcomes.proportionTargetRetirementsAfterFirstSet() + outcomes.proportionOpponentRetirementsAfterFirstSet());
			final double targetMwpAfterOneBall = (targetMwpNormalWin + outcomes.proportionOpponentRetirements()) / (targetMwpNormalWin + opponentMwpNormalWin + outcomes.proportionTargetRetirements() + outcomes.proportionOpponentRetirements());

			System.out.println("MWP After First Set = " + targetMwpAfterFirstSet);
			System.out.println("MWP After One Ball = " + targetMwpAfterOneBall);
			System.out.println("(" + targetSets + ", " + opponentSets + "), " + "(" + targetGames + ", " + opponentGames + "), " + "(" + targetPoints + ", " + opponentPoints + ")");
			outcomes.minPrint(favourite.getSurname(), underdog.getSurname());
			System.out.println();

			mwpNoRiskSeries.add(index, mwp);
			mwpWRAfterFirstSetSeries.add(index, targetMwpAfterFirstSet);
			mwpWRAfterOneBallSeries.add(index, targetMwpAfterOneBall);
		}

		/*********************************************************************************/

	    final XYSeriesCollection dataset = new XYSeriesCollection();
	    dataset.addSeries(mwpNoRiskSeries);
	    dataset.addSeries(mwpWRAfterFirstSetSeries);
	    dataset.addSeries(mwpWRAfterOneBallSeries);

	    return dataset;
	}
}
